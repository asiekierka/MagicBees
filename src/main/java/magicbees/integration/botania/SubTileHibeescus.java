package magicbees.integration.botania;

import elec332.core.util.ItemStackHelper;
import elec332.core.world.WorldHelper;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.subtile.SubTileFunctional;

import java.util.List;
import java.util.Random;

/**
 * Created by Elec332 on 18-5-2017.
 */
public class SubTileHibeescus extends SubTileFunctional {

	public static final String NAME = "hibeescus";
	private final int MANA_PER_OPERATION = 10000;
	// Ticks per second * seconds per minute * real-time minutes
	private final int OPERATION_TICKS_TIME = 20 * 60 * 15;
	private final double RANGE = 0.75;

	private ItemStack beeSlot = ItemStackHelper.NULL_STACK;
	private long operationTicksRemaining;
	private float manaCostRollover;

	@Override
	public int getColor() {
		return 0xF94F4F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (supertile.getWorld().isRemote) {
			return;
		}

		if (ItemStackHelper.isStackValid(beeSlot)) {
			progressOperation();
		} else if (isTimeToPickUpItem() && redstoneSignal == 0) {
			findBeeItemToHold();
		}
	}

	private boolean isTimeToPickUpItem() {
		return (this.supertile.getWorld().getTotalWorldTime() ^ supertile.getPos().getX() ^ supertile.getPos().getZ()) % 11 == 0;
	}

	private void progressOperation() {
		if (0 < operationTicksRemaining && 0 < this.mana) {
			operationTicksRemaining--;
			manaCostRollover += getManaPerOpTick();
			if (1f <= manaCostRollover) {
				int amount = (int)manaCostRollover;
				mana -= amount;
				manaCostRollover -= amount;
			}
		} else if (operationTicksRemaining <= 0) {
			IBee bee = BeeManager.beeRoot.getMember(beeSlot);
			if (bee == null){
				return;
			}
			bee.setIsNatural(true);
			EnumBeeType beeType = EnumBeeType.QUEEN;
			if (bee.getMate() == null) {
				beeType = EnumBeeType.PRINCESS;
			}
			ItemStack outputStack = BeeManager.beeRoot.getMemberStack(bee, beeType);
			EntityItem entity = dropItemStackInRange(outputStack);

			WorldHelper.spawnEntityInWorld(supertile.getWorld(), entity);
			beeSlot = ItemStackHelper.NULL_STACK;
		}
	}

	private EntityItem dropItemStackInRange(ItemStack outputStack) {
		Random r = supertile.getWorld().rand;
		EntityItem entity = new EntityItem(supertile.getWorld(), supertile.getPos().getX() - RANGE + r.nextInt((int)(RANGE * 2 + 1)), supertile.getPos().getY() + 1, supertile.getPos().getZ() - RANGE + r.nextInt((int)(RANGE * 2 + 1)), outputStack);
		entity.motionX = 0;
		entity.motionY = 0;
		entity.motionZ = 0;
		return entity;
	}

	private void findBeeItemToHold() {
		@SuppressWarnings("unchecked")
		List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, getSearchBoundingBox());

		for (EntityItem itemEntity : items) {
			ItemStack item = itemEntity.getItem();
			if (!itemEntity.isDead && isItemPrincessOrQueen(item)) {
				beeSlot = itemEntity.getItem().copy();
				beeSlot.stackSize = 1;
				operationTicksRemaining = (long)(OPERATION_TICKS_TIME * BotaniaIntegrationConfig.hibeescusTicksMultiplier) + supertile.getWorld().rand.nextInt(200);
				manaCostRollover = 0f;

				// Princesses and Queens don't stack, but YOU NEVER KNOW.
				item.stackSize -= 1;
				if (item.stackSize <= 0) {
					itemEntity.setDead();
				}

				// Leave for loop
				break;
			}
		}
	}

	private boolean isItemPrincessOrQueen(ItemStack stack) {
		boolean isBee = BeeManager.beeRoot.isMember(stack, EnumBeeType.PRINCESS) || BeeManager.beeRoot.isMember(stack, EnumBeeType.QUEEN);
		if (isBee) {
			IBee bee = BeeManager.beeRoot.getMember(stack);
			// Only process Ignoble bees.
			if (bee != null && !bee.isNatural()) {
				return true;
			}
		}
		return false;
	}

	private AxisAlignedBB getSearchBoundingBox() {
		BlockPos pos = supertile.getPos();
		int xCoord = pos.getX(), yCoord = pos.getY(), zCoord = pos.getZ();
		return new AxisAlignedBB(xCoord - RANGE, yCoord, zCoord - RANGE, xCoord + RANGE + 1, yCoord + 1, zCoord + RANGE + 1);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.hasKey("slot")) {
			NBTTagCompound itemTag = (NBTTagCompound)cmp.getTag("slot");
			beeSlot = ItemStackHelper.loadItemStackFromNBT(itemTag);
		}
		operationTicksRemaining = cmp.getLong("operationTicks");
		manaCostRollover = cmp.getFloat("manaRollover");
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		if (ItemStackHelper.isStackValid(beeSlot)) {
			NBTTagCompound itemTag = new NBTTagCompound();
			beeSlot.writeToNBT(itemTag);
			cmp.setTag("slot", itemTag);
		}
		cmp.setLong("operationTicks", operationTicksRemaining);
		cmp.setFloat("manaRollover", manaCostRollover);
	}

	@Override
	public List<ItemStack> getDrops(List<ItemStack> list) {
		if (ItemStackHelper.isStackValid(beeSlot)) {
			list.add(beeSlot);
		}
		return super.getDrops(list);
	}

	@Override
	public int getMaxMana() {
		return getFinalOperationManaCost() / 20;
	}

	private int getFinalOperationManaCost() {
		return (int)(MANA_PER_OPERATION * BotaniaIntegrationConfig.hibeescusManaCostMultiplier);
	}

	private float getManaPerOpTick() {
		return getFinalOperationManaCost() / OPERATION_TICKS_TIME;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

}