package magicbees.init;

import elec332.core.compat.forestry.allele.AlleleEffectPotion;
import elec332.core.compat.forestry.allele.AlleleEffectSpawnMob;
import elec332.core.compat.forestry.allele.AlleleFlowerProvider;
import elec332.core.compat.forestry.bee.FlowerProvider;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAlleleFloat;
import forestry.core.genetics.alleles.AlleleFloat;
import magicbees.MagicBees;
import magicbees.bees.allele.AlleleEffectCrumbling;
import magicbees.bees.allele.AlleleEffectTransmuting;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 25-8-2016.
 */
public final class AlleleRegister {

    public static IAlleleFloat speedBlinding;
    public static AlleleFlowerProvider flowersBookshelf;
    public static IAlleleBeeEffect effectSlowSpeed, effectWithering, effectTransmuting, effectCrumbling, alleleInvisibility;
    public static AlleleEffectSpawnMob spawnWolf, spawnBats, spawnCow, spawnChicken, spawnPig,
            spawnSheep, spawnCat, spawnHorse, spawnGhast, spawnSpider, spawnBlaze, spawnZombie;

    public static void init(){
        flowersBookshelf = new AlleleFlowerProvider(MagicBeesResourceLocation.create("flowersBookshelf"), new FlowerProvider("flowersBookshelf"));
        flowersBookshelf.registerAcceptableFlower(Blocks.BOOKSHELF);
        effectSlowSpeed = new AlleleEffectPotion(MagicBeesResourceLocation.create("effectSlowSpeed"), new PotionEffect(MobEffects.SLOWNESS, 60));

        effectWithering = new AlleleEffectPotion(MagicBeesResourceLocation.create("effectWithering"), new PotionEffect(MobEffects.WITHER, 15));
        alleleInvisibility = new AlleleEffectPotion(MagicBeesResourceLocation.create("alleleInvisibility"), new PotionEffect(MobEffects.INVISIBILITY, 10)).setBypassesArmour();
        effectCrumbling = new AlleleEffectCrumbling(MagicBeesResourceLocation.create("crumbling"), MagicBees.crumblingHandler);
        effectTransmuting = new AlleleEffectTransmuting(MagicBeesResourceLocation.create("effectTransmuting"), MagicBees.transmutationController);

        spawnWolf = newMobEffect("Canine", false, "Wolf").setThrottle(650).setSpawnChance(40).setMaxMobsInArea(2);
        spawnBats = newMobEffect("Batty", false, "Bat").setThrottle(150);
        spawnCow = newMobEffect("Bovine", true, "Cow").setThrottle(640).setMaxMobsInArea(3);
        spawnChicken = newMobEffect("Chicken", true, "Chicken").setThrottle(20).setMaxMobsInArea(20);
        spawnPig = newMobEffect("Porcine", true, "Pig").setThrottle(350).setMaxMobsInArea(4);
        spawnSheep = newMobEffect("Sheep", false, "Sheep").setThrottle(450).setMaxMobsInArea(5);
        spawnCat = newMobEffect("Catty", false, "Ozelot").setThrottle(702).setSpawnChance(60).setMaxMobsInArea(2);
        spawnHorse = newMobEffect("Horse", false, "EntityHorse").setThrottle(450).setSpawnChance(59).setMaxMobsInArea(2);
        spawnGhast = newMobEffect("Ghastly", false, "Ghast").setThrottle(2060).setSpawnChance(10).setMaxMobsInArea(1);
        spawnSpider = newMobEffect("Spidery", false, "Spider").setThrottle(400).setSpawnChance(70).setMaxMobsInArea(4);
        spawnBlaze = newMobEffect("Ablaze", false, "Blaze").setThrottle(800).setSpawnChance(60).setMaxMobsInArea(2);
        spawnZombie = newMobEffect("Brainy", false, "Brainy").setAngryOnPlayers().setThrottle(800).setMaxMobsInArea(2);

        speedBlinding = new AlleleFloat(MagicBees.modid, "speed", "Blinding", 2.0f, false);
        AlleleManager.alleleRegistry.registerAllele(speedBlinding, EnumBeeChromosome.SPEED);

    }

    private static AlleleEffectSpawnMob newMobEffect(String name, boolean dominant, String mob){
        return new AlleleEffectSpawnMob(MagicBeesResourceLocation.create(name), new ResourceLocation(mob));
    }

}
