package magicbees.tile;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import elec332.core.client.RenderHelper;
import elec332.core.client.util.GuiDraw;
import elec332.core.inventory.widget.Widget;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.ISimpleWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.InventoryHelper;
import elec332.core.util.ItemStackHelper;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBee;
import forestry.api.genetics.IEffectData;
import magicbees.tile.logic.EffectJarHousing;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 5-4-2017.
 */
public class TileEntityEffectJar extends TileEntity implements ITickable, ISimpleWindowFactory {

	public TileEntityEffectJar() {
		super();
		this.beeSlots = new BasicItemHandler(1);
	}

	private GameProfile ownerName;
	private BasicItemHandler beeSlots;
	private ItemStack queenStack = ItemStackHelper.NULL_STACK;
	private IEffectData[] effectData = new IEffectData[2];
	private int throttle;
	private int currentBeeHealth;
	private int currentBeeColour;

	public void setOwner(EntityPlayer player) {
		this.ownerName = player.getGameProfile();
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	public ItemStack getQueenStack() {
		return queenStack;
	}

	@Override
	public void update() {
		if (hasQueen()) {
			tickQueen();
		} else if (hasDrone()) {
			createQueenFromDrone();
		}
	}

	public void setQueenStack(ItemStack queenStack) {
		if (!ItemStackHelper.isStackValid(queenStack)){
			queenStack = ItemStackHelper.NULL_STACK;
		}
		this.queenStack = queenStack;
	}

	public GameProfile getOwner() {
		return this.ownerName;
	}

	private void createQueenFromDrone() {
		ItemStack droneStack = this.beeSlots.getStackInSlot(0);
		if (BeeManager.beeRoot.isDrone(droneStack)) {
			IBee bee = Preconditions.checkNotNull(BeeManager.beeRoot.getMember(droneStack));
			if (droneStack.stackSize == 1) {
				this.beeSlots.setStackInSlot(0, ItemStackHelper.NULL_STACK);
			} else {
				droneStack.stackSize--;
			}

			queenStack = droneStack.copy();
			queenStack.stackSize = 1;

			int current = bee.getHealth();
			int max = bee.getMaxHealth();
			currentBeeHealth = (current * 100) / max;
			currentBeeColour = bee.getGenome().getPrimary().getSpriteColour(0);
		}
		this.markDirty();
	}

	private boolean hasDrone() {
		return ItemStackHelper.isStackValid(beeSlots.getStackInSlot(0));
	}

	@SuppressWarnings("all")
	private void tickQueen() {
		IBee queen = BeeManager.beeRoot.getMember(queenStack);
		if (queenStack == null || queen == null){
			throw new RuntimeException();
		}

		currentBeeHealth = (queen.getHealth() * 100) / queen.getMaxHealth();
		currentBeeColour = queen.getGenome().getPrimary().getSpriteColour(0);

		EffectJarHousing housingLogic = new EffectJarHousing(this);
		this.effectData = queen.doEffect(this.effectData, housingLogic);
		if (this.getWorld().getWorldTime() % 5 == 0) {
			this.effectData = queen.doFX(this.effectData, housingLogic);
		}

		// run the queen
		if (throttle > 550) {
			throttle = 0;
			queen.age(this.getWorld(), 0.26f);

			if (queen.getHealth() == 0) {
				this.queenStack = ItemStackHelper.NULL_STACK;
				currentBeeHealth = 0;
				currentBeeColour = 0x0ffffff;
			} else {
				if (queenStack.getTagCompound() == null){
					queenStack.setTagCompound(new NBTTagCompound());
				}
				queen.writeToNBT(queenStack.getTagCompound());
			}
			this.markDirty();
		} else {
			throttle++;
		}
	}

	private boolean hasQueen() {
		return ItemStackHelper.isStackValid(queenStack);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagRoot) {
		super.readFromNBT(tagRoot);
		beeSlots.deserializeNBT(tagRoot);
		if (!tagRoot.hasKey("queenStack")){
			queenStack = ItemStackHelper.NULL_STACK;
		} else {
			List<ItemStack> l = Lists.newArrayList();
			InventoryHelper.readItemsFromNBT(tagRoot.getCompoundTag("queenStack"), l);
			queenStack = l.get(0);
		}
		this.currentBeeHealth = tagRoot.getInteger("currentBeeHealth");
		this.throttle = tagRoot.getInteger("throttle");
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound tagRoot) {
		super.writeToNBT(tagRoot);
		tagRoot = beeSlots.writeToNBT(tagRoot);
		if (ItemStackHelper.isStackValid(queenStack)){
			tagRoot.setTag("queenStack", InventoryHelper.writeItemsToNBT(Lists.newArrayList(queenStack)));
		}
		tagRoot.setInteger("currentBeeHealth", this.currentBeeHealth);
		tagRoot.setInteger("throttle", this.throttle);
		return tagRoot;
	}

	public ItemStack getDropStack(){
		return beeSlots.getStackInSlot(0);
	}

	@Override
	public void modifyWindow(Window window, Object... objects) {
		window.setBackground(MagicBeesResourceLocation.create("gui/effectJarGui"));
		window.addWidget(new WidgetSlot(beeSlots, 0, 80, 22));
		window.addWidget(new Widget(0, 0, 0, 0, 0, 0){

			private static final int BAR_HEIGHT = 40;

			@Override
			@SideOnly(Side.CLIENT)
			public void draw(Window window, int guiX, int guiY, int mouseX, int mouseY) {
				float r = ((currentBeeColour >> 16) & 255) / 255f;
				float g = ((currentBeeColour >> 8) & 255) / 255f;
				float b = (currentBeeColour & 255) / 255f;

				GlStateManager.color(r, g, b);
				RenderHelper.bindTexture(window.getBackgroundImageLocation());

				int value = BAR_HEIGHT - (currentBeeHealth * BAR_HEIGHT) / 100;
				GuiDraw.drawTexturedModalRect(window.guiLeft + 117, window.guiTop + value + 10, 176, 0, 10, BAR_HEIGHT - value);
			}

		});
		window.addPlayerInventoryToContainer();
	}

}
