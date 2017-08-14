package magicbees.bees;

import elec332.core.compat.forestry.bee.BeeGenomeTemplate;
import elec332.core.compat.forestry.bee.ForestryBeeEffects;
import forestry.api.apiculture.BeeManager;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleSpeciesBuilder;
import forestry.api.genetics.IClassification;
import magicbees.MagicBees;
import magicbees.init.AlleleRegister;
import magicbees.util.Config;
import magicbees.util.IMagicBeesBranch;
import magicbees.util.ModNames;
import magicbees.util.Utils;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.awt.*;

import static elec332.core.compat.forestry.ForestryAlleles.*;

/**
 * Created by Elec332 on 15-8-2016.
 */
public enum EnumBeeBranches implements IMagicBeesBranch {

    VEILED("Velatapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            //Nope
        }

        @Override
        public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
            //Nope
        }

    },
    ARCANE("Arcanapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setTemperatureTolerance(TOLERANCE_BOTH_1);
            genomeTemplate.setFloweringSpeed(FLOWERING_SLOW);
            genomeTemplate.setFertility(FERTILITY_HIGH);
        }

        @Override
        public Color getSecondaryColor() {
            return new Color(0xFF9D60);
        }

    },
    SUPERNATURAL("Occultapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setNeverSleeps(TRUE_RECESSIVE);
            genomeTemplate.setSpeed(SPEED_NORMAL);
            genomeTemplate.setFloweringSpeed(FLOWERING_SLOWEST);
        }

        @Override
        public Color getSecondaryColor() {
            return ARCANE.getSecondaryColor();
        }

    },
    SCHOLARLY("Doctapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setSpeed(SPEED_SLOW);
            genomeTemplate.setLifeSpan(LIFESPAN_ELONGATED);
            genomeTemplate.setTemperatureTolerance(TOLERANCE_UP_2);
            genomeTemplate.setCaveDwelling(TRUE_RECESSIVE);
            genomeTemplate.setNeverSleeps(TRUE_RECESSIVE);
            genomeTemplate.setFlowerProvider(AlleleRegister.flowersBookshelf);
        }

        @Override
        public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
            super.setIndividualProperties(speciesBuilder);
            speciesBuilder.setHumidity(EnumHumidity.ARID);
        }

    },
    SKULKING("Malevolenapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setSpeed(SPEED_FAST);
            genomeTemplate.setFloweringSpeed(FLOWERING_FASTER);
            genomeTemplate.setNeverSleeps(TRUE_RECESSIVE);
            genomeTemplate.setFertility(FERTILITY_NORMAL);
        }

        @Override
        public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
        }

        @Override
        public Color getSecondaryColor() {
            return new Color(0xe15236);
        }

    },
    MAGICAL("Magicapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setSpeed(SPEED_NORMAL);
            genomeTemplate.setLifeSpan(LIFESPAN_NORMAL);
        }

    },
    TIME("Tempestivapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setHumidityTolerance(TOLERANCE_BOTH_1);
            genomeTemplate.setFertility(FERTILITY_NORMAL);
            genomeTemplate.setSpeed(SPEED_NORMAL);
            genomeTemplate.setCaveDwelling(TRUE_RECESSIVE);
        }

    },
    SOUL("Animapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setHumidityTolerance(TOLERANCE_BOTH_1);
            genomeTemplate.setToleratesRain(TRUE_RECESSIVE);
        }

    },
    ABOMINABLE("Detestabilapis") {

        @Override
        public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
            super.setIndividualProperties(speciesBuilder);
            speciesBuilder.setTemperature(EnumTemperature.HELLISH);
            speciesBuilder.setHumidity(EnumHumidity.ARID);
        }

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setTemperatureTolerance(TOLERANCE_DOWN_2);
            genomeTemplate.setFlowerProvider(FLOWERS_NETHER);
            genomeTemplate.setNeverSleeps(TRUE_RECESSIVE);
            genomeTemplate.setCaveDwelling(TRUE_RECESSIVE);
            genomeTemplate.setEffect(ForestryBeeEffects.effectAggressive);
            genomeTemplate.setLifeSpan(LIFESPAN_SHORT);
        }

        @Override
        public Color getSecondaryColor() {
            return new Color(0x960F00);
        }

    },
    EXTRINSIC("Extrarapis") {

        @Override
        public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
            super.setIndividualProperties(speciesBuilder);
            speciesBuilder.setTemperature(EnumTemperature.COLD);
        }

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setTemperatureTolerance(TOLERANCE_UP_2);
            genomeTemplate.setEffect(ForestryBeeEffects.effectAggressive);
            genomeTemplate.setNeverSleeps(TRUE_RECESSIVE);
            genomeTemplate.setFlowerProvider(FLOWERS_END);
            genomeTemplate.setCaveDwelling(TRUE_RECESSIVE);
        }

        @Override
        public Color getSecondaryColor() {
            return new Color(0xF696FF);
        }

    },
    METALLIC("Metalliapis"){

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setSpeed(SPEED_SLOWEST);
            genomeTemplate.setFertility(FERTILITY_LOW);
            genomeTemplate.setNeverSleeps(TRUE_RECESSIVE);
            genomeTemplate.setCaveDwelling(TRUE_RECESSIVE);
        }

        @Override
        public boolean enabled() {
            return Config.Bees.enableGemBees;
        }

    },
    GEM("Lapidapis"){

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            METALLIC.setBranchProperties(genomeTemplate);
        }

        @Override
        public boolean enabled() {
            return Config.Bees.enableGemBees;
        }

    },
    TRANSMUTING("Transmutapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setLifeSpan(LIFESPAN_SHORTEST);
            genomeTemplate.setHumidityTolerance(TOLERANCE_UP_1);
            genomeTemplate.setTemperatureTolerance(TOLERANCE_DOWN_2);
        }

    },
    THAUMIC("Thaumiapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setFertility(FERTILITY_LOW);
            genomeTemplate.setFlowerProvider(null/*thaumcraft*/);
        }

    },
    FLESHY("Carnosapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            SKULKING.setBranchProperties(genomeTemplate);
        }

    },
    ALCHEMICAL("Alchimiapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            throw new IllegalStateException();
        }

    },
    ESSENTIAL("Essentiapis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            throw new IllegalStateException();
        }

    },
    THERMAL("Thermametallic") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate template) {
            BeeIntegrationInterface.getTemplateTE(template);
        }

        @Override
        public boolean enabled() {
            return Loader.isModLoaded(ModNames.THERMALFOUNDATION);
        }

    },
    ADORABLE("Amabilis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            throw new IllegalStateException();
        }

    },
    BLOODY("Sanguis") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            throw new IllegalStateException();
        }

    },
    BOTANICAL("Botanica") {

        @Override
        public void setBranchProperties(BeeGenomeTemplate genomeTemplate) {
            genomeTemplate.setTemperatureTolerance(TOLERANCE_BOTH_1);
            genomeTemplate.setHumidityTolerance(TOLERANCE_BOTH_1);
            genomeTemplate.setSpeed(SPEED_SLOWER);
        }

        @Override
        public Color getSecondaryColor() {
            return new Color(0xFFB2BB);
        }

        @Override
        public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
        }

        @Override
        public boolean enabled() {
            return Loader.isModLoaded(ModNames.BOTANIA);
        }

    };

    EnumBeeBranches(String scientificName){
        this.classification = BeeManager.beeFactory.createBranch(MagicBees.modid + ".branch." + name().toLowerCase(), scientificName);
    }

    public static void registerClassifications() {
        for (EnumBeeBranches branch : EnumBeeBranches.values()) {
            AlleleManager.alleleRegistry.getClassification("family.apidae").addMemberGroup(branch.classification);
        }
    }

    private final IClassification classification;

    @Override
    public abstract void setBranchProperties(BeeGenomeTemplate genomeTemplate);

    @Override
    public void setIndividualProperties(IAlleleSpeciesBuilder speciesBuilder) {
        Utils.setSecret(speciesBuilder);
    }

    @Nonnull
    @Override
    public IClassification getClassification() {
        return classification;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public Color getSecondaryColor() {
        return new Color(0xFF7C26);
    }

}
