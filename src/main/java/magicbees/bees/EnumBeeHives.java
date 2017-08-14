package magicbees.bees;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import elec332.core.compat.forestry.ForestryAlleles;
import elec332.core.compat.forestry.bee.HiveDrop;
import elec332.core.compat.forestry.bee.IHiveEnum;
import elec332.core.java.JavaHelper;
import elec332.core.world.WorldHelper;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IHiveDrop;
import forestry.api.apiculture.hives.IHiveDescription;
import forestry.api.apiculture.hives.IHiveGen;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.EnumTolerance;
import forestry.api.genetics.IAllele;
import magicbees.MagicBees;
import magicbees.init.BlockRegister;
import magicbees.init.ItemRegister;
import magicbees.item.types.EnumCombType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 20-8-2016.
 */
public enum EnumBeeHives implements IHiveEnum {

    CURIOUS(EnumBeeSpecies.MYSTICAL, 12, false, EnumHiveGen.CURIOUS){

        @Override
        public void registerDrops() {
            addDefaultDrops(15);
        }

    },
    UNUSUAL(EnumBeeSpecies.UNUSUAL, 12, false, EnumHiveGen.UNUSUAL) {

        @Override
        public void registerDrops() {
            addDefaultDrops(15);
        }

    },
    RESONANT(EnumBeeSpecies.SORCEROUS, 12, false, EnumHiveGen.RESONANT) {

        @Override
        public void registerDrops() {
            addDefaultDrops(20);
        }

    },
    DEEP(EnumBeeSpecies.ATTUNED, 4, true, EnumHiveGen.DEEP){

        @Override
        public void registerDrops() {
            addDefaultDrops(20);
        }

        @Override
        public boolean showInTab() {
            return false;
        }

    },
    INFERNAL(EnumBeeSpecies.INFERNAL, 15, true, EnumHiveGen.INFERNAL, EnumHiveGen.INFERNAL_OVERWORLD){

        @Override
        public void registerDrops() {
            ItemStack[] combs = new ItemStack[]{
                    ItemRegister.combItem.getStackFromType(EnumCombType.MOLTEN), new ItemStack(Items.GLOWSTONE_DUST, 6)
            };
            addDrop(new HiveDrop(getBeeType(), 80, combs).setIgnobleChance(0.5f));
            addDrop(new HiveDrop(BeeManager.beeRoot.templateAsGenome(EnumBeeSpecies.getForestrySpeciesTemplate("Steadfast")), 3, combs));
        }

        @Override
        public boolean showInTab() {
            return false;
        }

    },
    OBLIVION(EnumBeeSpecies.OBLIVION, 7, true, EnumHiveGen.OBLIVION, EnumHiveGen.OBLIVION_OVERWORLD){

        @Override
        public void registerDrops() {
            ItemStack[] combs = new ItemStack[]{
                    ItemRegister.combItem.getStackFromType(EnumCombType.FORGOTTEN), new ItemStack(Items.ENDER_PEARL)
            };
            addDrop(new HiveDrop(getBeeType(), 80, combs));
            addDrop(new HiveDrop(BeeManager.beeRoot.templateAsGenome(EnumBeeSpecies.getForestrySpeciesTemplate("Steadfast")), 9, combs));
        }

        @Override
        public boolean showInTab() {
            return false;
        }

    }
    ;

    EnumBeeHives(EnumBeeSpecies beeType, int light, boolean ignoreClimate, EnumHiveGen... genTypes){
        this.bee = beeType;
        this.ignoreClimate = ignoreClimate;
        this.light = light;
        this.genTypes = Lists.newArrayList(Preconditions.checkNotNull(genTypes)).stream().map((Function<EnumHiveGen, IHiveDescription>) Desc::new).collect(Collectors.toList());
    }

    private final EnumBeeSpecies bee;
    private final boolean ignoreClimate;
    private final int light;
    private final List<IHiveDescription> genTypes;


    @Override
    @SuppressWarnings("all")
    public IHiveDescription getHiveDescription() {
        return genTypes.get(0);
    }

    @Nonnull
    @Override
    public List<IHiveDescription> getHiveDescriptions() {
        return genTypes;
    }

    @Override
    public String getUid(IHiveDescription desc) {
        return MagicBees.modid + ".hive." + ((Desc) desc).gen.name();
    }

    @Override
    public int getMeta() {
        return ordinal();
    }

    @Override
    public boolean showInTab(){
        return true;
    }

    @Override
    public int getLight(){
        return light;
    }

    public abstract void registerDrops();

    protected final EnumBeeSpecies getBeeType(){
        return bee;
    }

    protected void addDefaultDrops(int rr){
        ItemStack comb = ItemRegister.combItem.getStackFromType(EnumCombType.MUNDANE);
        IHiveDrop valiantDrop = new HiveDrop(addRainResist(EnumBeeSpecies.getForestrySpeciesTemplate("Valiant")), 0.05, comb);

        addDrop(new HiveDrop(bee, 80, comb).setIgnobleChance(0.7f));
        addDrop(new HiveDrop(addRainResist(bee), rr, comb));
        addDrop(valiantDrop);
    }

    private class Desc implements IHiveDescription {

        private Desc(EnumHiveGen gen){
            this.gen = gen;
        }

        private final EnumHiveGen gen;

        @Nonnull
        @Override
        public IHiveGen getHiveGen() {
            return gen.hiveGen;
        }

        @Nonnull
        @Override
        public IBlockState getBlockState() {
            return BlockRegister.hiveBlock.getStateFromHive(EnumBeeHives.this);
        }

        @Override
        public boolean isGoodBiome(@Nonnull Biome biome) {
            return JavaHelper.hasAtLeastOneMatch(Lists.newArrayList(WorldHelper.getTypes(biome)), gen.biomes);
        }

        @Override
        public boolean isGoodHumidity(@Nonnull EnumHumidity humidity) {
            if (ignoreClimate){
                return true;
            }
            EnumHumidity idealHumidity = bee.getSpecies().getHumidity();
            EnumTolerance humidityTolerance = bee.getGenome().getToleranceHumid();
            return AlleleManager.climateHelper.isWithinLimits(humidity, idealHumidity, humidityTolerance);

        }

        @Override
        public boolean isGoodTemperature(@Nonnull EnumTemperature temperature) {
            if (ignoreClimate){
                return true;
            }
            EnumTemperature idealTemperature = bee.getSpecies().getTemperature();
            EnumTolerance temperatureTolerance = bee.getGenome().getToleranceTemp();
            return AlleleManager.climateHelper.isWithinLimits(temperature, idealTemperature, temperatureTolerance);

        }

        @Override
        public float getGenChance() {
            return gen.chance;
        }

        @Override
        public void postGen(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos blockPos) {
            gen.postGen(world, random, blockPos);
        }

    }

    private static IBeeGenome addRainResist(IAllele[] alleles){
        IAllele[] ret = alleles.clone();
        ret[EnumBeeChromosome.TOLERATES_RAIN.ordinal()] = ForestryAlleles.TRUE_RECESSIVE;
        return BeeManager.beeRoot.templateAsGenome(ret);
    }

    private static IBeeGenome addRainResist(EnumBeeSpecies species){
        return addRainResist(species.getAlleles());
    }

}
