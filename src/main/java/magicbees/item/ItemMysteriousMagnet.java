package magicbees.item;

import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.item.AbstractTexturedItem;
import elec332.core.util.StatCollector;
import elec332.core.world.WorldHelper;
import magicbees.MagicBees;
import magicbees.util.Config;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 4-3-2017.
 */
public class ItemMysteriousMagnet extends AbstractTexturedItem {

    public ItemMysteriousMagnet() {
        super(MagicBeesResourceLocation.create("mysteriousMagnet"));
        this.setNoRepair();
        this.setHasSubtypes(true);
        this.setCreativeTab(MagicBees.creativeTab);
    }

    @SideOnly(Side.CLIENT)
    private IBakedModel[] models;
    private static final float FUDGE_FACTOR = 0.2f;

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationC(@Nonnull ItemStack stack, World world, List<String> tooltip, boolean advanced) {
        super.addInformationC(stack, world, tooltip, advanced);
        String s = String.format(StatCollector.translateToLocal("misc.level"), getMagnetLevel(stack));
        if (isMagnetActive(stack)) {
            tooltip.add(String.format(StatCollector.translateToLocal("misc.magnetActive"), s));
        } else {
            tooltip.add(String.format(StatCollector.translateToLocal("misc.magnetInactive"), s));
        }
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if (entity instanceof EntityLivingBase) {
            if (isMagnetActive(itemStack) && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)entity;
                float radius = getRadius(itemStack) - FUDGE_FACTOR;
                AxisAlignedBB bounds = player.getEntityBoundingBox().expand(radius, radius, radius);

                if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                    bounds.expand(FUDGE_FACTOR, FUDGE_FACTOR, FUDGE_FACTOR);

                    if (7 <= getMagnetLevel(itemStack)) {
                        List<EntityArrow> arrows = world.getEntitiesWithinAABB(EntityArrow.class, bounds);

                        for (EntityArrow arrow : arrows) {
                            if ((arrow.pickupStatus == EntityArrow.PickupStatus.ALLOWED || world.rand.nextFloat() < 0.3f) && arrow.shootingEntity != entity) {
                                EntityItem replacement = new EntityItem(world, arrow.posX, arrow.posY, arrow.posZ, new ItemStack(Items.ARROW));
                                WorldHelper.spawnEntityInWorld(world, replacement);
                            }
                            world.removeEntity(arrow);
                        }
                    }
                }

                List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, bounds);

                for (EntityItem e : list) {
                    if (e.age >= 10) {
                        double x = player.posX - e.posX;
                        double y = player.posY - e.posY;
                        double z = player.posZ - e.posZ;

                        double length = Math.sqrt(x * x + y * y + z * z) * 2;

                        x = x / length + player.motionX / 2;
                        y = y / length + player.motionY / 2;
                        z = z / length + player.motionZ / 2;

                        e.motionX = x;
                        e.motionY = y;
                        e.motionZ = z;
                        e.isAirBorne = true;

                        if (e.isCollidedHorizontally) {
                            e.motionY += 1;
                        }

                        if (world.rand.nextFloat() < 0.2f) {
                            float pitch = 0.85f - world.rand.nextFloat() * 3f / 10f;
                            if(Config.magnetSound) {
                                world.playSound(e.posX, e.posY, e.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 0.6f, pitch, false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void getSubItemsC(@Nonnull Item item, List<ItemStack> subItems, CreativeTabs creativeTab) {
        for (int i = 0; i <= getMaximumLevel(); i++) {
            subItems.add(new ItemStack(this, 1, i * 2));
        }
    }

    @Override
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return models[isMagnetActive(stack) ? 1 : 0];
    }

    @Override
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        models = new IBakedModel[textures.length];
        for (int i = 0; i < models.length; i++) {
            models[i] = modelBakery.itemModelForTextures(textures[i]);
        }
    }

    @Override
    protected ResourceLocation[] getTextureLocations() {
        return new ResourceLocation[]{
                MagicBeesResourceLocation.create("items/magnetinactive"),
                MagicBeesResourceLocation.create("items/magnetactive")
        };
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isMagnetActive(stack);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClickC(EntityPlayer player, @Nonnull EnumHand hand, World world) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            toggleActive(stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private void toggleActive(ItemStack itemStack) {
        itemStack.setItemDamage(itemStack.getItemDamage() ^ 1);
    }

    private boolean isMagnetActive(ItemStack itemStack) {
        return isMagnetActive(itemStack.getItemDamage());
    }

    private boolean isMagnetActive(int damage) {
        return (damage & 0x01) == 1;
    }

    private int getMaximumLevel() {
        return Config.magnetMaxLevel;
    }

    private int getMagnetLevel(ItemStack itemStack) {
        return itemStack.getItemDamage() >> 1;
    }

    private float getRadius(ItemStack itemStack) {
        return Config.magnetBaseRange + (Config.magnetLevelMultiplier * getMagnetLevel(itemStack));
    }

}
