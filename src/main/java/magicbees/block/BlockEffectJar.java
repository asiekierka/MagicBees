package magicbees.block;

import com.google.common.collect.Lists;
import elec332.core.client.model.loading.INoBlockStateJsonBlock;
import elec332.core.inventory.window.WindowManager;
import elec332.core.tile.AbstractBlock;
import elec332.core.world.WorldHelper;
import magicbees.MagicBees;
import magicbees.tile.TileEntityEffectJar;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
/**
 * Created by Elec332 on 5-4-2017.
 */
public class BlockEffectJar extends AbstractBlock implements ITileEntityProvider, INoBlockStateJsonBlock {

	public BlockEffectJar() {
		super(Material.GLASS);
		setCreativeTab(MagicBees.creativeTab);
		this.setHardness(0.1f);
		this.setResistance(1.5f);
	}

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25f, 0f, 0.25f, 0.75f, 0.81f, 0.74f);

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean onBlockActivatedC(World world, BlockPos pos, EntityPlayer player, EnumHand hand, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			WindowManager.openWindow(player, MagicBees.instance, world, pos);
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
		return new TileEntityEffectJar();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Nonnull
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileEntity tile = WorldHelper.getTileAt(world, pos);

		if (tile != null && tile instanceof TileEntityEffectJar) {
			TileEntityEffectJar jar = (TileEntityEffectJar) tile;

			// 0 is the only droppable stack.
			ItemStack stack = jar.getDropStack();

			if (stack != null && stack.stackSize > 0) {
				float pX = world.rand.nextFloat() * 0.8f + 0.1f;
				float pY = world.rand.nextFloat() * 0.8f + 0.1f;
				float pZ = world.rand.nextFloat() * 0.8f + 0.1f;

				EntityItem entityItem = new EntityItem(world, pos.getX() + pX, pos.getY() + pY, pos.getZ() + pZ, stack.copy());

				entityItem.motionX = world.rand.nextGaussian() * 0.05f;
				entityItem.motionY = world.rand.nextGaussian() * 0.05f + 0.2f;
				entityItem.motionZ = world.rand.nextGaussian() * 0.05f;

				WorldHelper.spawnEntityInWorld(world, entityItem);
				stack.stackSize = 0;
			}

		}

		super.breakBlock(world, pos, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public VariantList getVariantsFor(IBlockState state) {
		return new VariantList(Lists.newArrayList(new Variant(MagicBeesResourceLocation.create("models/effectjar.obj"), ModelRotation.X0_Y0, false, 0)));
	}

}
