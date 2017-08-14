package magicbees.item;

import elec332.core.item.AbstractTexturedItem;
import forestry.api.arboriculture.IToolGrafter;
import forestry.core.utils.Translator;
import magicbees.MagicBees;
import magicbees.bees.BeeIntegrationInterface;
import magicbees.util.MagicBeesResourceLocation;
import magicbees.util.ModNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 18-5-2017.
 */
@Optional.InterfaceList({
		@Optional.Interface(iface = "vazkii.botania.api.mana.IManaUsingItem", modid = ModNames.BOTANIA, striprefs = true)
})
public class ItemManaSteelGrafter extends AbstractTexturedItem implements IToolGrafter, IManaUsingItem {

	public static final int MANA_PER_DAMAGE = 90;

	public ItemManaSteelGrafter() {
		super(MagicBeesResourceLocation.create("manasteelgrafter"));
		setCreativeTab(MagicBees.creativeTab);
		setMaxDamage(15);
		setMaxStackSize(1);
		setHarvestLevel("grafter", 3);
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
	public boolean usesMana(ItemStack arg0) {
		return true;
	}

	@Override
	public void addInformationC(@Nonnull ItemStack stack, World world, List<String> tooltip, boolean advanced) {
		super.addInformationC(stack, world, tooltip, advanced);
		if (!stack.isItemDamaged()) {
			tooltip.add(Translator.translateToLocalFormatted("item.for.uses", stack.getMaxDamage() + 1));
		}
	}

	@Override
	public boolean canHarvestBlock(@Nonnull IBlockState state, ItemStack stack) {
		Block block = state.getBlock();
		return block instanceof BlockLeaves || state.getMaterial() == Material.LEAVES || super.canHarvestBlock(state, stack);
	}

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
	 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		return true;
	}

	@Override
	public float getSaplingModifier(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityPlayer player, @Nonnull BlockPos pos) {
		return 100.0f;
	}

}

