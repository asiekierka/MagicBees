package magicbees.integration.botania;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import magicbees.util.Utils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.subtile.SubTileGenerating;

import java.util.List;

/**
 * Created by Elec332 on 18-5-2017.
 */
public class SubTileBeegonia extends SubTileGenerating {

	public static final String NAME = "beegonia";

	private static final int DRONE_BURN_TIME_BASE = 100;
	private static final int ITEM_STACK_AGE_THRESHOLD = 60;
	private static final int RANGE = 3;

	private int burnTimeLeft = 0;

	private void searchForDroneStack() {
		if (!supertile.getWorld().isRemote && mana < getMaxMana()) {
			boolean needsSync = false;

			List<EntityItem> items = supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, Utils.getAABB(supertile.getPos(), RANGE, true));
			for (EntityItem item : items) {
				if (item.age >= ITEM_STACK_AGE_THRESHOLD && !item.isDead) {
					ItemStack stack = item.getItem();
					if (BeeManager.beeRoot.isDrone(stack)) {
						IBee bee = BeeManager.beeRoot.getMember(stack);
						if (bee == null){
							return;
						}

						if (stack.stackSize > 0) {
							stack.stackSize--;
							burnTimeLeft = (int)(DRONE_BURN_TIME_BASE * getSpeciesScaledComplexity(bee.getGenome().getPrimary()) * BotaniaIntegrationConfig.beegoniaManaMultiplier);
							needsSync = true;

							//todo? supertile.getWorld().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "botania:endoflame", 0.2F, 1F);
						}

						if (stack.stackSize <= 0) {
							item.setDead();
						}
					}
				}
			}

			if (needsSync) {
				sync();
			}
		}
	}

	private int getSpeciesScaledComplexity(IAlleleBeeSpecies species) {
		if (species == null) {
			return 0;
		} else {
			try {
				int halfComplexity = species.getComplexity() / 2;
				return (halfComplexity >= 1) ? halfComplexity : 1;
			}
			catch (NoSuchMethodError error) {
				return 1;
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (linkedCollector != null) {
			if (burnTimeLeft > 0) {
				burnTimeLeft--;
			}
			else {
				searchForDroneStack();
			}
		}
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		burnTimeLeft = cmp.getInteger("burnTimeLeft");
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger("burnTimeLeft", burnTimeLeft);
	}

	@Override
	public int getMaxMana() {
		return 600;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 1;
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTimeLeft > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xFFFF96;
	}

}