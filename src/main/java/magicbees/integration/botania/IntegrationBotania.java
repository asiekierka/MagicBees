package magicbees.integration.botania;

import com.google.common.collect.Lists;
import elec332.core.api.module.ElecModule;
import elec332.core.compat.forestry.allele.AlleleFlowerProvider;
import elec332.core.util.recipes.RecipeHelper;
import elec332.core.world.WorldHelper;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import magicbees.MagicBees;
import magicbees.api.ITransmutationHandler;
import magicbees.bees.BeeIntegrationInterface;
import magicbees.bees.EnumBeeSpecies;
import magicbees.bees.allele.AlleleEffectTransmuting;
import magicbees.init.ItemRegister;
import magicbees.util.DefaultTransmutationController;
import magicbees.util.MagicBeesResourceLocation;
import magicbees.util.ModNames;
import magicbees.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

/**
 * Created by Elec332 on 15-5-2017.
 */
@ElecModule(owner = MagicBees.modid, name = "Botania Integration", modDependencies = ModNames.BOTANIA)
public class IntegrationBotania {
	private Block livingRock, dreamWood;
	private Item itemPetal;
	private IRecipe manasteelScoopRecipe, manasteelGrafterRecipe;

	@SidedProxy(modId = MagicBees.modid, clientSide = "magicbees.integration.botania.IntegrationBotaniaProxyClient", serverSide = "magicbees.integration.botania.IntegrationBotaniaProxyClient")
	public static IntegrationBotaniaProxy proxy;

	@ElecModule.EventHandler
	@Optional.Method(modid = "botania")
	public void preInit(FMLPreInitializationEvent event){
		BotaniaAPI.registerSubTile(SubTileBeegonia.NAME, SubTileBeegonia.class);
		BotaniaAPI.registerSubTileSignature(SubTileBeegonia.class, new BotaniaSignature(SubTileBeegonia.NAME));
		BotaniaAPI.addSubTileToCreativeMenu(SubTileBeegonia.NAME);

		BotaniaAPI.registerSubTile(SubTileHiveacynth.NAME, SubTileHiveacynth.class);
		BotaniaAPI.registerSubTileSignature(SubTileHiveacynth.class, new BotaniaSignature(SubTileHiveacynth.NAME));
		BotaniaAPI.addSubTileToCreativeMenu(SubTileHiveacynth.NAME);

		BotaniaAPI.registerSubTile(SubTileHibeescus.NAME, SubTileHibeescus.class);
		BotaniaAPI.registerSubTileSignature(SubTileHibeescus.class, new BotaniaSignature(SubTileHibeescus.NAME));
		BotaniaAPI.addSubTileToCreativeMenu(SubTileHibeescus.NAME);

		proxy.preInit();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@ElecModule.EventHandler
	@Optional.Method(modid = "botania")
	public void init(FMLInitializationEvent event){
		IBlockState livingWood = BeeIntegrationInterface.livingWood = Utils.getBlock(ModNames.BOTANIA, "livingwood").getDefaultState();
		livingRock = Utils.getBlock(ModNames.BOTANIA, "livingrock");
		dreamWood = Utils.getBlock(ModNames.BOTANIA, "dreamwood");
		Block mysticalFlower = Utils.getBlock(ModNames.BOTANIA, "specialFlower");
		BeeIntegrationInterface.effectDreaming = new AlleleEffectTransmuting(BeeIntegrationInterface.bot_dreaming_name, new DefaultTransmutationController((ITransmutationHandler) (world, pos, block, biome) -> {

			int[] oreIDs = OreDictionary.getOreIDs(block);
			for (int oreId : oreIDs) {
				if (oreId == OreDictionary.getOreID("logWood")) {
					WorldHelper.setBlockState(world, pos, livingWood, 3);
					return true;
				} else if (oreId == OreDictionary.getOreID("stone")) {
					WorldHelper.setBlockState(world, pos, livingRock.getDefaultState(), 3);
					return true;
				} else if (oreId == OreDictionary.getOreID("livingwood")) {
					WorldHelper.setBlockState(world, pos, dreamWood.getDefaultState(), 3);
					return true;
				}
			}
			return false;

		}));
		BeeIntegrationInterface.flowersBotania = new AlleleFlowerProvider(BeeIntegrationInterface.bot_flowers_name, new FlowerProviderBotania("flowersBotania", mysticalFlower));
		BeeIntegrationInterface.flowersBotania.registerAcceptableFlower(mysticalFlower);

		BeeIntegrationInterface.itemPetal = itemPetal = Utils.getItem(ModNames.BOTANIA, "petal");
		BeeIntegrationInterface.itemPastureSeed = Utils.getItem(ModNames.BOTANIA, "grassSeeds");
		BeeIntegrationInterface.seedTypes = 9;
	}

	@SubscribeEvent
	@Optional.Method(modid = "botania")
	public void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
		manasteelScoopRecipe = new ShapedOreRecipe(ItemRegister.manasteelScoop.getRegistryName(), new ItemStack(ItemRegister.manasteelScoop), "twt", "tmt", " t ", 'm', new ItemStack(ModItems.manaResource, 1, 0), 'w', Blocks.WOOL, 't', "stickWood");
		manasteelGrafterRecipe = new ShapedOreRecipe(ItemRegister.manasteelgrafter.getRegistryName(), new ItemStack(ItemRegister.manasteelgrafter), "  m", " t ", "t  ", 'm', new ItemStack(ModItems.manaResource, 1, 0), 't', "stickWood");
		manasteelScoopRecipe.setRegistryName(ItemRegister.manasteelScoop.getRegistryName());
		manasteelGrafterRecipe.setRegistryName(ItemRegister.manasteelgrafter.getRegistryName());

		event.getRegistry().registerAll(manasteelScoopRecipe, manasteelGrafterRecipe);
	}

	@ElecModule.EventHandler
	@Optional.Method(modid = "botania")
	public void postInit(FMLPostInitializationEvent event){
		RecipeManaInfusion infusionBeeBotanical = new RecipeManaInfusionBeeSpecies(EnumBeeSpecies.BOT_BOTANIC, EnumBeeSpecies.BOT_ROOTED, 55000, EnumBeeType.DRONE);
		BotaniaAPI.manaInfusionRecipes.add(infusionBeeBotanical);

		RecipeManaInfusion infusionBeeVazbee = new RecipeManaInfusionBeeSpecies(EnumBeeSpecies.BOT_VAZBEE, EnumBeeSpecies.BOT_FLORAL, 167392, EnumBeeType.PRINCESS);
		infusionBeeVazbee.setAlchemy(true);
		BotaniaAPI.manaInfusionRecipes.add(infusionBeeVazbee);

		RecipeElvenTrade tradeBeeAelfheim = new RecipeElvenTradeBeeSpecies(EnumBeeSpecies.BOT_DREAMING, EnumBeeSpecies.BOT_ALFHEIM);
		BotaniaAPI.elvenTradeRecipes.add(tradeBeeAelfheim);

		LexiconEntry entryManasteelForestryTools = new BotaniaLexiconEntry("magicbees.botania.lexicon.manasteelTools.title", BotaniaAPI.categoryTools);
		entryManasteelForestryTools.setIcon(new ItemStack(ItemRegister.manasteelScoop));
		entryManasteelForestryTools.addPage(BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.manasteelTools.0"));
		entryManasteelForestryTools.addPage(BotaniaAPI.internalHandler.craftingRecipePage("magicbees.botania.lexicon.manasteelScoop", manasteelScoopRecipe.getRegistryName()));
		entryManasteelForestryTools.addPage(BotaniaAPI.internalHandler.craftingRecipePage("magicbees.botania.lexicon.manasteelGrafter", manasteelGrafterRecipe.getRegistryName()));

		LexiconEntry entryBeeTrade = new BotaniaLexiconEntry("magicbees.botania.lexicon.beeAlfheim.title", BotaniaAPI.categoryAlfhomancy);
		entryBeeTrade.setIcon(BeeManager.beeRoot.getMemberStack(EnumBeeSpecies.BOT_ALFHEIM.getIndividual(), EnumBeeType.PRINCESS));
		entryBeeTrade.setKnowledgeType(BotaniaAPI.elvenKnowledge);
		entryBeeTrade.addPage(BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.beeAlfheim.0"));
		entryBeeTrade.addPage(BotaniaAPI.internalHandler.elvenTradesPage("magicbees.botania.lexicon.beeAlfheim.trade", tradeBeeAelfheim));

		int manaPowder = Lists.newArrayList(LibItemNames.MANA_RESOURCE_NAMES).indexOf("manaPowder");
		int redstoneRoot = Lists.newArrayList(LibItemNames.MANA_RESOURCE_NAMES).indexOf("redstoneRoot");
		Item itemManaResource = ModItems.manaResource;

		RecipePetals beegoniaRecipe = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack(SubTileBeegonia.NAME),
				new ItemStack(itemPetal),
				new ItemStack(itemPetal, 1, 4),
				new ItemStack(itemPetal, 1, 4),
				new ItemStack(itemPetal, 1, 15),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(itemManaResource, 1, manaPowder)
		);
		LexiconEntry entryBeegonia = new BotaniaLexiconEntry("tile.botania:flower." + SubTileBeegonia.NAME + ".name", BotaniaAPI.categoryGenerationFlowers);
		entryBeegonia.setLexiconPages(
				BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.beegonia.0"),
				BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.beegonia.1"),
				BotaniaAPI.internalHandler.petalRecipePage("magicbees.botania.lexicon.beegonia.crafting", beegoniaRecipe),
				BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.beegonia.2")
		);

		RecipePetals hiveacynthRecipe = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack(SubTileHiveacynth.NAME),
				new ItemStack(itemPetal, 1, 3),
				new ItemStack(itemPetal, 1, 9),
				new ItemStack(itemPetal, 1, 9),
				new ItemStack(itemPetal, 1, 11),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(itemManaResource, 1, redstoneRoot)
		);
		LexiconEntry entryHiveacynth = new BotaniaLexiconEntry("tile.botania:flower." + SubTileHiveacynth.NAME + ".name", BotaniaAPI.categoryFunctionalFlowers);
		entryHiveacynth.setLexiconPages(BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.hiveacynth.0"),
				BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.hiveacynth.1"),
				BotaniaAPI.internalHandler.petalRecipePage("magicbees.botania.lexicon.hiveacynth.crafting", hiveacynthRecipe)
		);

		RecipePetals hibeescusRecipe = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack(SubTileHibeescus.NAME),
				new ItemStack(itemPetal, 1, 1),
				new ItemStack(itemPetal, 1, 2),
				new ItemStack(itemPetal, 1, 2),
				new ItemStack(itemPetal, 1, 2),
				new ItemStack(itemPetal, 1, 14),
				new ItemStack(itemManaResource, 1, manaPowder),
				new ItemStack(ModItems.rune, 1, 0),
				new ItemStack(ModItems.rune, 1, 5),
				new ItemStack(ModItems.rune, 1, 14),
				new ItemStack(ModItems.rune, 1, 11),
				new ItemStack(ModItems.rune, 1, 15),
				new ItemStack(itemManaResource, 1, redstoneRoot)
		);
		LexiconEntry entryHibeescus = new BotaniaLexiconEntry("tile.botania:flower." + SubTileHibeescus.NAME + ".name", BotaniaAPI.categoryFunctionalFlowers);
		entryHibeescus.setLexiconPages(BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.hibeescus.0"),
				BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.hibeescus.1"),
				BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.hibeescus.2"),
				BotaniaAPI.internalHandler.petalRecipePage("magicbees.botania.lexicon.hibeescus.crafting", hibeescusRecipe)
		);

		//todo
		//LexiconEntry entryManaApiaryBooster = new BotaniaLexiconEntry("tile.manaAuraProvider.name", BotaniaAPI.categoryDevices);
		//entryManaApiaryBooster.setLexiconPages(
		//		BotaniaAPI.internalHandler.textPage("magicbees.botania.lexicon.manaBooster.0"),
		//		BotaniaAPI.internalHandler.craftingRecipePage("magicbees.botania.lexicon.manaBooster.crafting", manaBoosterRecipe)
		//);
	}

}
