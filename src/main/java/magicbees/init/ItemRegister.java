package magicbees.init;

import elec332.core.item.AbstractTexturedItem;
import elec332.core.item.ItemEnumBased;
import elec332.core.util.RegistryHelper;
import elec332.core.util.recipes.RecipeHelper;
import magicbees.bees.EnumBeeModifiers;
import magicbees.item.*;
import magicbees.item.types.*;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by Elec332 on 4-3-2017.
 */
public final class ItemRegister {
    public static ItemEnumBased<EnumCombType> combItem;
    public static ItemEnumBased<EnumDropType> dropItem;
    public static ItemEnumBased<EnumPollenType> pollenItem;
    public static ItemEnumBased<EnumPropolisType> propolisItem;
    public static ItemEnumBased<EnumWaxType> waxItem;
    public static ItemEnumBased<EnumResourceType> resourceItem;
    public static ItemEnumBased<EnumNuggetType> orePartItem;
    //frames
    public static Item magicFrame, resilientFrame, gentleFrame, metabolicFrame, necroticFrame, temporalFrame, oblivionFrame;
    public static Item mysteriousMagnet, moonDial, ironNugget;
    public static Item manasteelgrafter, manasteelScoop;
    public static Item hiveBlockItem, effectJarItem;

    public static void preInit() {
        combItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("beeComb"), EnumCombType.class));
        dropItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("drop"), EnumDropType.class));
        pollenItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("pollen"), EnumPollenType.class));
        propolisItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("propolis"), EnumPropolisType.class));
        waxItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("wax"), EnumWaxType.class));
        resourceItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("resource"), EnumResourceType.class));
        orePartItem = (new ItemEnumBased<>(MagicBeesResourceLocation.create("orepart"), EnumNuggetType.class));

        magicFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.MAGIC));
        resilientFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.RESILIENT));
        gentleFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.GENTLE));
        metabolicFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.METABOLIC));
        necroticFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.NECROTIC));
        temporalFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.TEMPORAL));
        oblivionFrame = (new ItemMagicBeesFrame(EnumBeeModifiers.OBLIVION));

        mysteriousMagnet = (new ItemMysteriousMagnet());
        moonDial = (new ItemMoonDial());

        manasteelgrafter = (new ItemManaSteelGrafter());
        manasteelScoop = (new ItemManaSteelScoop());

        hiveBlockItem = BlockRegister.hiveBlock.createItemBlock();
        effectJarItem = new ItemBlock(BlockRegister.effectJar);
        effectJarItem.setRegistryName(BlockRegister.effectJar.getRegistryName());
    }

    public static void init() {
        for (EnumCombType comb : EnumCombType.values()) {
            OreDictionary.registerOre("beeComb", combItem.getStackFromType(comb));
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(combItem, dropItem, pollenItem, propolisItem, waxItem, resourceItem, orePartItem);

        event.getRegistry().registerAll(magicFrame, resilientFrame, gentleFrame, metabolicFrame, necroticFrame, temporalFrame, oblivionFrame);

        event.getRegistry().registerAll(mysteriousMagnet, moonDial);

        event.getRegistry().registerAll(manasteelgrafter, manasteelScoop);

        event.getRegistry().registerAll(hiveBlockItem, effectJarItem);
    }
}
