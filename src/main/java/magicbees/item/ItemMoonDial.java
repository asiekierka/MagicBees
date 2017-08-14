package magicbees.item;

import com.google.common.base.Preconditions;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.item.AbstractTexturedItem;
import elec332.core.util.ItemStackHelper;
import elec332.core.util.MoonPhase;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import magicbees.MagicBees;
import magicbees.bees.EnumBeeSpecies;
import magicbees.util.Config;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 4-3-2017.
 */
public class ItemMoonDial extends AbstractTexturedItem {

    public ItemMoonDial() {
        super(MagicBeesResourceLocation.create("moondial"));
        setCreativeTab(MagicBees.creativeTab);
    }

    @SideOnly(Side.CLIENT)
    private IBakedModel[] models;
    private static final ResourceLocation[] textureLocs;

    @Override
    public void getSubItemsC(@Nonnull Item item, List<ItemStack> subItems, CreativeTabs creativeTab) {
        super.getSubItemsC(item, subItems, creativeTab);
        if (!MagicBees.beesInitialized) return;

        for (EnumBeeType type : EnumBeeType.values()) {
            for (EnumBeeSpecies species : EnumBeeSpecies.values()) {
                IBee bee = species.getIndividual();
                if (bee.isSecret()) {
                    continue;
                }


                ItemStack beeStack = BeeManager.beeRoot.getMemberStack(bee, type);
                if (ItemStackHelper.isStackValid(beeStack)) {
                    subItems.add(beeStack);
                }
            }
        }
    }

    @Override
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        if (world == null && entity == null){
            return models[0];
        }
        if (world == null){
            world = entity.world;
        }
        Preconditions.checkNotNull(world);
        return models[MoonPhase.getMoonPhase(world).ordinal()];
    }

    @Override
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        models = new IBakedModel[textures.length];
        for (int i = 0; i < textures.length; i++) {
            models[i] = modelBakery.itemModelForTextures(textures[i]);
        }
    }

    @Override
    protected ResourceLocation[] getTextureLocations() {
        return textureLocs;
    }

    @Override
    public void addInformationC(@Nonnull ItemStack stack, World world, List<String> tooltip, boolean advanced) {
        super.addInformationC(stack, world, tooltip, advanced);
        EntityPlayer player = MagicBees.proxy.getClientPlayer();
        if (player != null && Config.moonDialShowsPhaseInText && ItemStackHelper.isStackValid(player.getHeldItemMainhand()) && player.getHeldItemMainhand().getItem() == this){
            tooltip.add("\u00A77" + MoonPhase.getMoonPhase(world).getLocalizedName());
        }
    }

    static {
        textureLocs = new ResourceLocation[MoonPhase.values().length];
        for (int i = 0; i < textureLocs.length; i++) {
            textureLocs[i] = MagicBeesResourceLocation.create("items/moondial."+i);
        }
    }

}
