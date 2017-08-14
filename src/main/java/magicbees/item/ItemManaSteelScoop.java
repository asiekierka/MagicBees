package magicbees.item;

import elec332.core.item.AbstractTexturedItem;
import forestry.api.core.IToolScoop;
import magicbees.MagicBees;
import magicbees.bees.BeeIntegrationInterface;
import magicbees.util.MagicBeesResourceLocation;
import magicbees.util.ModNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

/**
 * Created by Elec332 on 18-5-2017.
 */

@Optional.InterfaceList({
		@Optional.Interface(iface = "vazkii.botania.api.mana.IManaUsingItem", modid = ModNames.BOTANIA, striprefs = true)
})
public class ItemManaSteelScoop extends AbstractTexturedItem implements IManaUsingItem, IToolScoop {

	public static final int MANA_PER_DAMAGE = 30;

	public ItemManaSteelScoop() {
		super(MagicBeesResourceLocation.create("manasteelscoop"));
		this.maxStackSize = 1;
		this.setMaxDamage(20);
		this.setMaxStackSize(1);
		this.setCreativeTab(MagicBees.creativeTab);
		this.setHarvestLevel("scoop", 3);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		stack.damageItem(1, entityLiving);
		return true;
	}

	@Override
	@Optional.Method(modid = ModNames.BOTANIA)
	public boolean getIsRepairable(ItemStack toolStack, ItemStack material) {
		return (material.getItem() == BeeIntegrationInterface.itemManaResource && material.getItemDamage() == 0) || super.getIsRepairable(toolStack, material);
	}

	@Override
	@Optional.Method(modid = ModNames.BOTANIA)
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		// Unsure if the server-side exclusion is necessary, but boy it looks disconcerting on the client if it's in.
		if (/*!world.isRemote && */stack.getItemDamage() > 0 && player instanceof EntityPlayer) {
			if (ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, MANA_PER_DAMAGE, true)) {
				stack.setItemDamage(stack.getItemDamage() - 1);
			}
		}
	}

	@Override
	@Optional.Method(modid = ModNames.BOTANIA)
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
