package magicbees.block;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.client.model.loading.INoJsonBlock;
import forestry.api.apiculture.IHiveTile;
import forestry.apiculture.tiles.TileHive;
import magicbees.bees.EnumBeeHives;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 15-4-2017.
 */
public class BlockHive extends elec332.core.compat.forestry.bee.BlockHive<EnumBeeHives> implements INoJsonBlock, ITileEntityProvider {
	@SideOnly(Side.CLIENT)
	private TextureAtlasSprite[][] textures;
	@SideOnly(Side.CLIENT)
	private IBakedModel[] models;

	@Nonnull
	@Override
	public Class<EnumBeeHives> getHiveTypes() {
		return EnumBeeHives.class;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBakedModel getBlockModel(IBlockState iBlockState) {
		return models[getMetaFromState(iBlockState)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBakedModel getItemModel(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
		int i = itemStack.getItemDamage();
		if (i > EnumBeeHives.values().length){
			i = 0;
		}
		return models[i];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerTextures(IIconRegistrar iconRegistrar) {
		int types = EnumBeeHives.values().length;
		textures = new TextureAtlasSprite[types][EnumFacing.VALUES.length];
		for (int i = 0; i < types; i++) {
			for (EnumFacing facing : EnumFacing.VALUES){
				textures[i][facing.ordinal()] = iconRegistrar.registerSprite(MagicBeesResourceLocation.create("blocks/beehive."+i+(facing.getAxis().isVertical() ? ".top" : ".side")));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
		int types = EnumBeeHives.values().length;
		models = new IBakedModel[types];
		for (int i = 0; i < types; i++) {
			models[i] = modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures[i]).setTexture(textures[i][3]));
		}
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		super.onBlockClicked(world, pos, player);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IHiveTile) {
			IHiveTile hive = (IHiveTile) tile;
			hive.onAttack(world, pos, player);
		}
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IHiveTile) {
			IHiveTile hive = (IHiveTile) tile;
			hive.onBroken(world, pos, player, canHarvestBlock(world, pos, player));
		}
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
		return new TileHive();
	}

}
