package magicbees.item.types;

import elec332.core.item.IEnumItem;
import elec332.core.item.ItemEnumBased;
import magicbees.MagicBees;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 4-3-2017.
 */
public enum EnumResourceType implements IEnumItem {

    LORE_FRAGMENT("fragment", true),
    AROMATIC_LUMP("lump", true),
    EXTENDED_FERTILIZER("fertilizer", true),
    SKULL_CHIP("skullChip", true),
    SKULL_FRAGMENT("skullFragment", true),
    DRAGON_DUST("dragonDust", true),
    DRAGON_CHUNK("dragonChunk", true),
    ESSENCE_FALSE_LIFE("essenceLife", true),
    ESSENCE_SHALLOW_GRAVE("essenceDeath", true),
    ESSENCE_LOST_TIME("essenceTime", true),
    ESSENCE_EVERLASTING_DURABILITY("essenceDurability", true),
    ESSENCE_SCORNFUL_OBLIVION("essenceOblivion", true),
    ESSENCE_FICKLE_PERMANENCE("essenceMutable", true),
    DIMENSIONAL_SINGULARITY("dimensionalSingularity", true),
    ;

    EnumResourceType(String name, boolean show){
        this.name = name;
        this.show = show;
    }

    private final String name;
    private final boolean show;

    @Override
    public void initializeItem(ItemEnumBased<? extends IEnumItem> itemEnumBased) {
        itemEnumBased.setCreativeTab(MagicBees.creativeTab);
    }

    @Override
    public boolean shouldShow() {
        return show;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return MagicBeesResourceLocation.create("items/"+name.toLowerCase());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getItem().getUnlocalizedName(null)+"."+name;
    }

}
