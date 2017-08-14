package magicbees.bees;

import elec332.core.compat.forestry.ForestryAlleles;
import elec332.core.compat.forestry.allele.AlleleEffectThrottled;
import elec332.core.compat.forestry.allele.AlleleFlowerProvider;
import elec332.core.compat.forestry.bee.BeeGenomeTemplate;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpeciesBuilder;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IAlleleEffect;
import forestry.api.genetics.IEffectData;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static elec332.core.compat.forestry.ForestryAlleles.*;

/**
 * Created by Elec332 on 15-5-2017.
 */
public final class BeeIntegrationInterface {

	public static final ResourceLocation bot_flowers_name = MagicBeesResourceLocation.create("flowersBotania");
	public static final ResourceLocation bot_dreaming_name = MagicBeesResourceLocation.create("effectDreaming");
	public static final ResourceLocation te_spawnBlizz_name = MagicBeesResourceLocation.create("spawnBlizz");
	public static final ResourceLocation te_spawnBlitz_name = MagicBeesResourceLocation.create("spawnBlitz");
	public static final ResourceLocation te_spawnBasalz_name = MagicBeesResourceLocation.create("spawnBasalz");

	public static AlleleFlowerProvider flowersBotania;
	public static IAlleleEffect effectDreaming, effectSpawnBlizz, effectSpawnBlitz, effectSpawnBasalz;

	public static IBlockState blockRSAFluxedElectrum;
	public static ItemStack itemRSAFluxedElectrumNugget;
	public static IBlockState livingWood, aeSkyStone;
	public static Item itemPetal, itemPastureSeed, itemManaResource;
	public static int seedTypes;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	static {
		Item nullItem = Items.FLOWER_POT;
		IBlockState nullBlock = Blocks.YELLOW_FLOWER.getDefaultState();
		//prevent null items/blocks/alleles/effects/whatever
		flowersBotania = new AlleleFlowerProvider(bot_flowers_name, ForestryAlleles.FLOWERS_VANILLA.getProvider());
		effectDreaming = getPlaceholderEffect(bot_dreaming_name);
		effectSpawnBlizz = getPlaceholderEffect(te_spawnBlizz_name);
		effectSpawnBlitz = getPlaceholderEffect(te_spawnBlitz_name);
		effectSpawnBasalz = getPlaceholderEffect(te_spawnBasalz_name);
		blockRSAFluxedElectrum = livingWood = aeSkyStone = nullBlock;
		itemPetal = itemPastureSeed = itemManaResource = nullItem;
		itemRSAFluxedElectrumNugget = new ItemStack(nullItem);
	}

	public static void getTemplateTE(BeeGenomeTemplate template){
		BeeGenomeTemplate ret = new BeeGenomeTemplate();
		ret.setHumidityTolerance(TOLERANCE_BOTH_2);
		ret.setTemperatureTolerance(TOLERANCE_BOTH_2);
		ret.setToleratesRain(TRUE_RECESSIVE);
		ret.setSpeed(SPEED_FAST);
		ret.setFertility(FERTILITY_HIGH);
		ret.setLifeSpan(LIFESPAN_LONGER);
		for (int i = 1; i < ret.getAlleles().length; i++) {
			template.getAlleles()[i] = ret.getAlleles()[i];
		}
	}

	public static void getTemplateTENether(BeeGenomeTemplate template){
		getTemplateTE(template);
		template.setHumidityTolerance(TOLERANCE_NONE);
		template.setTemperatureTolerance(TOLERANCE_NONE);
		template.setCaveDwelling(TRUE_RECESSIVE);
		template.setSpeed(SPEED_SLOW);
		template.setFertility(FERTILITY_NORMAL);
		template.setFlowerProvider(FLOWERS_NETHER);
		template.setEffect((IAlleleEffect) EnumBeeSpecies.getForestryAllele("effectIgnition"));
		template.setNeverSleeps(TRUE_RECESSIVE);
	}

	public static void getTemplateTEEnd(BeeGenomeTemplate template){
		getTemplateTE(template);
		template.setNeverSleeps(TRUE_RECESSIVE);
		template.setCaveDwelling(TRUE_RECESSIVE);
		template.setSpeed(SPEED_FASTEST);
		template.setLifeSpan(LIFESPAN_LONGEST);
	}

	public static void addPetals(IAlleleBeeSpeciesBuilder species, float chance){
		if (itemPetal == null){
			return;
		}
		for (int i = 0; i < EnumDyeColor.values().length; i++) {
			species.addSpecialty(new ItemStack(itemPetal, 1, i), chance);
		}
	}

	private BeeIntegrationInterface(){
		throw new RuntimeException();
	}

	private static IAlleleBeeEffect getPlaceholderEffect(ResourceLocation name){
		return new AlleleEffectThrottled(name) {

			@Override
			public IEffectData doEffectThrottled(IBeeGenome beeGenome, IEffectData effectData, IBeeHousing beeHousing) {
				return effectData;
			}

			@Override
			public IEffectData validateStorage(IEffectData effectData) {
				return effectData;
			}

		};
	}

}
