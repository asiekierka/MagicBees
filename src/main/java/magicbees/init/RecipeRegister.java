package magicbees.init;

import com.google.common.collect.Maps;
import elec332.core.util.ItemStackHelper;
import forestry.api.recipes.RecipeManagers;
import forestry.apiculture.items.EnumPropolis;
import forestry.apiculture.items.ItemRegistryApiculture;
import forestry.core.fluids.Fluids;
import magicbees.item.types.*;
import magicbees.util.Config;
import magicbees.util.EnumOreResourceType;
import magicbees.util.MagicBeesResourceLocation;
import magicbees.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

import static magicbees.init.ItemRegister.*;

/**
 * Created by Elec332 on 13-2-2017.
 */
public final class RecipeRegister {
    private ItemStack beesWax, honeyDrop, honeyDew, magicWax, refractoryWax, pollen, royaljelly;

    @SubscribeEvent
    public void onRegisterRecipe(RegistryEvent.Register<IRecipe> event) {
        EnumOreResourceType.registerRecipes(event.getRegistry());

        ItemRegistryApiculture ai = Utils.getApicultureItems();
        beesWax = Utils.getCoreItems().beeswax.getItemStack();
        honeyDrop = ai.honeyDrop.getItemStack();
        honeyDew = ai.honeydew.getItemStack();
        refractoryWax = Utils.getCoreItems().refractoryWax.getItemStack();
        magicWax = getWax(EnumWaxType.MAGIC);
        pollen = ai.pollenCluster.getItemStack();
        royaljelly = ai.royalJelly.getItemStack();
        registerRecipes(event.getRegistry());
        registerForestryRecipes(event.getRegistry());
    }

    private void addRecipe(IForgeRegistry<IRecipe> registry, ItemStack stack, Object... objects) {
        addRecipe(registry, stack.getItem().getRegistryName(), stack, objects);
    }

    private void addRecipe(IForgeRegistry<IRecipe> registry, ResourceLocation location, ItemStack stack, Object... objects) {
        addRecipe(registry, location, location, stack, objects);
    }

    private void addRecipe(IForgeRegistry<IRecipe> registry, ResourceLocation location, ResourceLocation group, ItemStack stack, Object... objects) {
        ShapedOreRecipe recipe = new ShapedOreRecipe(group, stack, objects);
        recipe.setRegistryName(location);
        registry.register(recipe);
    }

    private void addRecipe(IForgeRegistry<IRecipe> registry, ResourceLocation location, IRecipe recipe) {
        addRecipe(registry, location, location, recipe);
    }

    private void addRecipe(IForgeRegistry<IRecipe> registry, ResourceLocation location, ResourceLocation group, IRecipe recipe) {
        recipe.setRegistryName(location);
        registry.register(recipe);
    }

    private void addShapelessRecipe(IForgeRegistry<IRecipe> registry, ItemStack stack, Object... objects) {
        addShapelessRecipe(registry, stack.getItem().getRegistryName(), stack, objects);
    }

    private void addShapelessRecipe(IForgeRegistry<IRecipe> registry, ResourceLocation location, ItemStack stack, Object... objects) {
        addShapelessRecipe(registry, location, location, stack, objects);
    }

    private void addShapelessRecipe(IForgeRegistry<IRecipe> registry, ResourceLocation location, ResourceLocation group, ItemStack stack, Object... objects) {
        ShapelessOreRecipe recipe = new ShapelessOreRecipe(group, stack, objects);
        recipe.setRegistryName(location);
        registry.register(recipe);
    }


    private void registerRecipes(IForgeRegistry<IRecipe> registry) {
        ItemStack input;
        ItemStack output;

        input = getResource(EnumResourceType.EXTENDED_FERTILIZER);
        output = Utils.getCoreItems().fertilizerCompound.getItemStack(6);
        addRecipe(registry, output,
                " S ", " F ", " S ",
                'F', input,
                'S', "sand"
        );

        addRecipe(registry, output,
                "   ", "SFS", "   ",
                'F', input,
                'S', "sand"
        );

        output = ItemStackHelper.copyItemStack(output);
        output.stackSize = 12;
        addRecipe(registry, output,
                "aaa", "aFa", "aaa",
                'F', input,
                'a', Utils.getCoreItems().ash
        );

        addRecipe(registry, new ItemStack(Items.EXPERIENCE_BOTTLE),
                "DDD", "DBD", "DDD",
                'D', getDrop(EnumDropType.INTELLECT),
                'B', Items.GLASS_BOTTLE
        );

        addRecipe(registry, new ItemStack(Blocks.SOUL_SAND, 4),
                "SwS", "wDw", "SwS",
                'S', "sand",
                'D', "dirt",
                'w', getWax(EnumWaxType.SOUL)
        );

        addRecipe(registry, new ItemStack(Blocks.SOUL_SAND, 4),
                "wSw", "SDS", "wSw",
                'S', "sand",
                'D', "dirt",
                'w', getWax(EnumWaxType.SOUL)
        );

        addRecipe(registry, new ItemStack(ItemRegister.moonDial),
                "DqD", "qrq", "DqD",
                'r', "dustRedstone",
                'q', "gemQuartz",
                'D', "dyeGreen"
        );

        addRecipe(registry, getResource(EnumResourceType.SKULL_FRAGMENT),
                "xxx", "xxx", "xxx",
                'x', getResource(EnumResourceType.SKULL_CHIP)
        );

        addRecipe(registry, new ItemStack(Items.SKULL, 1, 1),
                "xxx", "xxx",
                'x', getResource(EnumResourceType.SKULL_FRAGMENT)
        );

        addRecipe(registry, getResource(EnumResourceType.DRAGON_CHUNK),
                "xxx", "xxx",
                'x', getResource(EnumResourceType.DRAGON_DUST)
        );

        addRecipe(registry, new ItemStack(Blocks.DRAGON_EGG, 1),
                "ccc", "cec", "ccc",
                'c', getResource(EnumResourceType.DRAGON_CHUNK),
                'e', getResource(EnumResourceType.ESSENCE_FALSE_LIFE)
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_EVERLASTING_DURABILITY),
                "gwg", "wiw", "gwg",
                'g', "blockGlass",
                'w', "waxMagical",
                'i', "blockIron"
        );

        addRecipe(registry,getResource(EnumResourceType.ESSENCE_EVERLASTING_DURABILITY),
                "wgw", "gig", "wgw",
                'g', "blockGlass",
                'w', "waxMagical",
                'i', "blockIron"
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_FALSE_LIFE),
                "gwg", "wfw", "gwg",
                'g', "blockGlass",
                'w', getWax(EnumWaxType.SOUL),
                'f', Blocks.RED_FLOWER
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_FALSE_LIFE),
                "wgw", "gfg", "wgw",
                'g', "blockGlass",
                'w', getWax(EnumWaxType.SOUL),
                'f', Blocks.RED_FLOWER
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_SHALLOW_GRAVE),
                "gwg", "wfw", "gwg",
                'g', "blockGlass",
                'w', getWax(EnumWaxType.SOUL),
                'f', Items.ROTTEN_FLESH
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_SHALLOW_GRAVE),
                "wgw", "gfg", "wgw",
                'g', "blockGlass",
                'w', getWax(EnumWaxType.SOUL),
                'f', Items.ROTTEN_FLESH
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_LOST_TIME),
                "wgw", "gcg", "wgw",
                'g', "blockGlass",
                'w', getWax(EnumWaxType.SOUL),
                'c', Items.CLOCK
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_LOST_TIME),
                "gwg", "wcw", "gwg",
                'g', "blockGlass",
                'w', getWax(EnumWaxType.SOUL),
                'c', Items.CLOCK
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_FICKLE_PERMANENCE),
                "wew", "gcg", "wew",
                'w', getWax(EnumWaxType.SOUL),
                'c', Items.MAGMA_CREAM,
                'e', "egg",
                'g', "glowstone"
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_FICKLE_PERMANENCE),
                "wgw", "ece", "wgw",
                'w', getWax(EnumWaxType.SOUL),
                'c', Items.MAGMA_CREAM,
                'e', "egg",
                'g', "glowstone"
        );

        addRecipe(registry, getResource(EnumResourceType.ESSENCE_SCORNFUL_OBLIVION),
                "gst", "sEs", "tsg",
                'g', getResource(EnumResourceType.ESSENCE_SHALLOW_GRAVE),
                't', getResource(EnumResourceType.ESSENCE_LOST_TIME),
                's', new ItemStack(Items.SKULL, 1, 1),
                'E', Blocks.DRAGON_EGG
        );

        addRecipe(registry, new ItemStack(magicFrame),
                "www", "wfw", "www",
                'w', magicWax,
                'f', Utils.getApicultureItems().frameUntreated
        );

        addRecipe(registry, new ItemStack(temporalFrame),
                "sPs", "PfP", "sPs",
                's', "sand",
                'P', getPollen(EnumPollenType.PHASED),
                'f', magicFrame
        );

        input = new ItemStack(magicFrame);
        addShapelessRecipe(registry, new ItemStack(resilientFrame),
                getResource(EnumResourceType.ESSENCE_EVERLASTING_DURABILITY),
                input
        );

        addShapelessRecipe(registry, new ItemStack(gentleFrame),
                getResource(EnumResourceType.ESSENCE_FALSE_LIFE),
                input
        );

        addShapelessRecipe(registry, new ItemStack(necroticFrame),
                getResource(EnumResourceType.ESSENCE_SHALLOW_GRAVE),
                input
        );

        addShapelessRecipe(registry, new ItemStack(metabolicFrame),
                getResource(EnumResourceType.ESSENCE_FICKLE_PERMANENCE),
                input
        );

        addShapelessRecipe(registry, new ItemStack(temporalFrame),
                getResource(EnumResourceType.ESSENCE_LOST_TIME),
                input
        );

        addShapelessRecipe(registry, new ItemStack(oblivionFrame),
                getResource(EnumResourceType.ESSENCE_SCORNFUL_OBLIVION),
                Utils.getApicultureItems().frameProven.getItemStack()
        );


        addRecipe(registry, new ItemStack(BlockRegister.effectJar),
                "GSG", "QPQ", "GGG",
                'G', "blockGlass",
                'S', "slabWood",
                'P', getPollen(EnumPollenType.UNUSUAL),
                'Q', "gemQuartz"
        );
/* todo
        output = new ItemStack(Config.magicApiary);
        addShapelessRecipe(registry, output,
                Config.pollen.getStackForType(PollenType.UNUSUAL, 2),
                Config.drops.getStackForType(DropType.ENCHANTED, 2),
                new ItemStack(ForestryHelper.apicultureBlock, 1, ForestryHelper.ApicultureBlock.APIARY.ordinal())
        );

        addRecipe(registry, new ItemStack(Config.enchantedEarth),
                "d d", " e ", "d d",
                'd', new ItemStack("dirt", 1, OreDictionary.WILDCARD_VALUE),
                'e', Config.miscResources.getStackForType(ResourceType.ESSENCE_FALSE_LIFE)
        );

        addRecipe(registry, new ItemStack(Config.enchantedEarth),
                " d ", "ded", " d ",
                'd', new ItemStack("dirt", 1, OreDictionary.WILDCARD_VALUE),
                'e', Config.miscResources.getStackForType(ResourceType.ESSENCE_FALSE_LIFE)
        );
*/

/* todo: ores
        if (OreDictionary.getOres("ingotCopper").size() <= 0) {
            NuggetType.COPPER.setInactive();
        }
        else {
            addRecipe(registry, new ShapedOreRecipe(OreDictionary.getOres("ingotCopper").get(0),
                    "xxx", "xxx", "xxx",
                    'x', "nuggetCopper"
            ));
        }

        if (OreDictionary.getOres("ingotTin").size() <= 0) {
            NuggetType.TIN.setInactive();
        }
        else {
            addRecipe(registry, new ShapedOreRecipe(OreDictionary.getOres("ingotTin").get(0),
                    "xxx", "xxx", "xxx",
                    'x', "nuggetTin"
            ));
        }

        if (OreDictionary.getOres("ingotSilver").size() <= 0) {
            NuggetType.SILVER.setInactive();
        }
        else {
            addRecipe(registry, new ShapedOreRecipe(OreDictionary.getOres("ingotSilver").get(0),
                    "xxx", "xxx", "xxx",
                    'x', "nuggetSilver"
            ));
        }

        if (OreDictionary.getOres("ingotLead").size() <= 0) {
            NuggetType.LEAD.setInactive();
        }
        else {
            addRecipe(registry, new ShapedOreRecipe(OreDictionary.getOres("ingotLead").get(0),
                    "xxx", "xxx", "xxx",
                    'x', "nuggetLead"
            ));
        }

        addRecipe(registry, new ShapedOreRecipe(new ItemStack(Items.IRON_INGOT),
                "xxx", "xxx", "xxx",
                'x', "nuggetIron"
        ));

        addRecipe(registry, new ShapedOreRecipe(new ItemStack(Items.DIAMOND),
                "xxx", "xxx", "xxx",
                'x', "nuggetDiamond"
        ));

        addRecipe(registry, new ShapedOreRecipe(new ItemStack(Items.EMERALD),
                "xxx", "xxx", "xxx",
                'x', "nuggetEmerald"
        ));

        addRecipe(registry, new ShapedOreRecipe(ItemInterface.getItemStack("apatite"),
                "xxx", "xxx", "xxx",
                'x', Config.nuggets.getStackForType(NuggetType.APATITE)
        ));
*/
        output = getResource(EnumResourceType.DIMENSIONAL_SINGULARITY);
        addRecipe(registry, output,
                " G ", "QEQ", " W ",
                'E', Items.ENDER_EYE,
                'Q', Blocks.QUARTZ_BLOCK,
                'W', Blocks.END_STONE,
                'G', Blocks.GOLD_BLOCK
        );
/* todo
        Magic capsules
        output = new ItemStack(Config.magicCapsule);
        output.stackSize = 4;
        addRecipe(registry, new ShapedOreRecipe(output,
                "WWW",
                'W', "waxMagical"
        ));

        output = Config.voidCapsule.getCapsuleForLiquid(FluidType.EMPTY);
        output.stackSize = 4;
        addRecipe(registry, output,
                "T T", "GFG", "T T",
                'G', "blockGlass"_PANE,
                'F', Config.miscResources.getStackForType(ResourceType.DIMENSIONAL_SINGULARITY),
                'T', Items.GOLD_NUGGET
        );
*/
        ResourceLocation magnetGroup = MagicBeesResourceLocation.create("mysteriousMagnet");

        output = new ItemStack(ItemRegister.mysteriousMagnet);
        addRecipe(registry, MagicBeesResourceLocation.create("mysteriousMagnet_0"), magnetGroup, output,
                " i ", "cSc", " d ",
                'i', Items.IRON_INGOT,
                'c', Items.COMPASS,
                'd', Items.DIAMOND,
                'S', getResource(EnumResourceType.DIMENSIONAL_SINGULARITY)
        );
        for (int level = 1; level <= Config.magnetMaxLevel; level++) {
            output = new ItemStack(ItemRegister.mysteriousMagnet, 1, level * 2);
            addRecipe(registry,  MagicBeesResourceLocation.create("mysteriousMagnet_" + level), magnetGroup, output,
                    " d ", "mSm", " B ",
                    'd', Items.DIAMOND,
                    'm', new ItemStack(ItemRegister.mysteriousMagnet, 1, (level - 1) * 2),
                    'B', Blocks.REDSTONE_BLOCK,
                    'S', getResource(EnumResourceType.DIMENSIONAL_SINGULARITY)
            );
        }

    }

    private void registerForestryRecipes(IForgeRegistry<IRecipe> registry) {
        registerCentrifugeRecipes();
        registerCarpenterRecipes();
    }

    private void registerCentrifugeRecipes(){
        CombCentrifugeRecipe recipe;

        recipe = new CombCentrifugeRecipe(EnumCombType.MUNDANE);
        recipe.addProduct(beesWax, 0.90f);
        recipe.addProduct(honeyDrop, 0.60f);
        recipe.addProduct(magicWax, 0.10f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.MOLTEN);
        recipe.addProduct(refractoryWax, 0.86f);
        recipe.addProduct(honeyDrop, 0.087f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.FORGOTTEN);
        recipe.addProduct(getWax(EnumWaxType.AMNESIC), 0.50f);
        recipe.addProduct(getPropolis(EnumPropolis.PULSATING), 0.50f);
        recipe.addProduct(honeyDrop, 0.40f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.OCCULT);
        recipe.addProduct(magicWax, 1.0f);
        recipe.addProduct(honeyDrop, 0.60f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.OTHERWORLDLY);
        recipe.addProduct(beesWax, 0.50f);
        recipe.addProduct(magicWax, 0.22f);
        recipe.addProduct(honeyDrop, 1);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.PAPERY);
        recipe.addProduct(beesWax, 0.80f);
        recipe.addProduct(magicWax, 0.20f);
        recipe.addProduct(new ItemStack(Items.PAPER), 0.057f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.INTELLECT);
        recipe.addProduct(magicWax, 0.90f);
        recipe.addProduct(honeyDew, 0.40f);
        recipe.addProduct(getDrop(EnumDropType.INTELLECT), 0.10f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.FURTIVE);
        recipe.addProduct(beesWax, 0.90f);
        recipe.addProduct(getPropolis(EnumPropolis.NORMAL), 0.20f);
        recipe.addProduct(honeyDew, 0.35f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.SOUL);
        recipe.addProduct(getWax(EnumWaxType.SOUL), 0.95f);
        recipe.addProduct(honeyDew, 0.26f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.TEMPORAL);
        recipe.addProduct(magicWax, 1);
        recipe.addProduct(getPollen(EnumPollenType.PHASED), 0.055f);
        recipe.addProduct(honeyDew, 0.60f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.TRANSMUTED);
        recipe.addProduct(beesWax, 0.80f);
        recipe.addProduct(magicWax, 0.80f);
        recipe.addProduct(getPropolis(EnumPropolisType.UNSTABLE), 0.15f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.AIRY);
        recipe.addProduct(magicWax, 1);
        recipe.addProduct(new ItemStack(Items.FEATHER), 0.60f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.FIREY);
        recipe.addProduct(magicWax, 1);
        recipe.addProduct(new ItemStack(Items.BLAZE_POWDER), 0.60f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.WATERY);
        recipe.addProduct(magicWax, 1);
        recipe.addProduct(new ItemStack(Items.DYE), 0.60f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.EARTHY);
        recipe.addProduct(magicWax, 1);
        recipe.addProduct(new ItemStack(Items.CLAY_BALL), 0.60f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.TE_DESTABILIZED);
        recipe.addProduct(getWax(EnumWaxType.MAGIC), 0.55f);
        recipe.addProduct(getDrop(EnumDropType.DESTABILIZED), 0.22f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.TE_CARBON);
        recipe.addProduct(getWax(EnumWaxType.MAGIC), 0.55f);
        recipe.addProduct(getDrop(EnumDropType.CARBON), 0.22f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.TE_LUX);
        recipe.addProduct(getWax(EnumWaxType.MAGIC), 0.55f);
        recipe.addProduct(getDrop(EnumDropType.LUX), 0.22f);
        recipe.register(20);

        recipe = new CombCentrifugeRecipe(EnumCombType.TE_ENDEARING);
        recipe.addProduct(getWax(EnumWaxType.MAGIC), 0.55f);
        recipe.addProduct(getDrop(EnumDropType.ENDEARING), 0.22f);
        recipe.register(20);
    }

    private void registerCarpenterRecipes(){
        ItemStack input;
        ItemStack output;

        output = Utils.getApicultureBlocks().candle.getUnlitCandle(24);
        RecipeManagers.carpenterManager.addRecipe(30, new FluidStack(FluidRegistry.WATER, 600), ItemStackHelper.NULL_STACK, output,
                " S ", "WWW", "WWW",
                'W', waxItem,
                'S', Items.STRING
        );

        output.stackSize = 6;
        input = Utils.getCoreItems().craftingMaterial.getSilkWisp();
        RecipeManagers.carpenterManager.addRecipe(30, new FluidStack(FluidRegistry.WATER, 600), ItemStackHelper.NULL_STACK, output,
                "WSW",
                'W', waxItem,
                'S', input
        );

        output = resourceItem.getStackFromType(EnumResourceType.AROMATIC_LUMP, 2);
        RecipeManagers.carpenterManager.addRecipe(30, Fluids.FOR_HONEY.getFluid(1000), ItemStackHelper.NULL_STACK, output,
                " P ", "JDJ", " P ",
                'P', pollen,
                'J', royaljelly,
                'D', getDrop(EnumDropType.ENCHANTED)
        );

        RecipeManagers.carpenterManager.addRecipe(30, Fluids.FOR_HONEY.getFluid(1000), ItemStackHelper.NULL_STACK, output,
                " J ", "PDP", " J ",
                'P', pollen,
                'J', royaljelly,
                'D', getDrop(EnumDropType.ENCHANTED)
        );
    }

    private static ItemStack getWax(EnumWaxType wax){
        return waxItem.getStackFromType(wax);
    }

    private static ItemStack getDrop(EnumDropType drop){
        return dropItem.getStackFromType(drop);
    }

    private static ItemStack getPollen(EnumPollenType pollen){
        return pollenItem.getStackFromType(pollen);
    }

    private static ItemStack getPropolis(EnumPropolisType propolis){
        return propolisItem.getStackFromType(propolis);
    }

    private static ItemStack getResource(EnumResourceType resource){
        return resourceItem.getStackFromType(resource);
    }

    private static ItemStack getPropolis(EnumPropolis propolis){
        return Utils.getApicultureItems().propolis.get(propolis, 1);
    }

    private static class CombCentrifugeRecipe {

        private CombCentrifugeRecipe(EnumCombType comb){
            this.comb = comb;
            this.products = Maps.newHashMap();
        }

        private EnumCombType comb;
        private final Map<ItemStack, Float> products;

        protected void addProduct(ItemStack stack, float chance){
            products.put(ItemStackHelper.copyItemStack(stack), chance);
        }

        protected void register(int time){
            RecipeManagers.centrifugeManager.addRecipe(time, combItem.getStackFromType(comb), products);
        }

    }

}
