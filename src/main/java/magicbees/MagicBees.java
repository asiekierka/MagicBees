package magicbees;

import com.google.common.eventbus.Subscribe;
import elec332.core.api.IElecCoreMod;
import elec332.core.api.module.IModuleController;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.compat.forestry.IndividualDefinitionRegistry;
import elec332.core.config.ConfigWrapper;
import elec332.core.inventory.window.IWindowFactory;
import elec332.core.inventory.window.IWindowHandler;
import elec332.core.inventory.window.Window;
import elec332.core.inventory.window.WindowManager;
import elec332.core.item.AbstractTexturedItem;
import elec332.core.util.AbstractCreativeTab;
import elec332.core.util.FMLUtil;
import elec332.core.util.LoadTimer;
import elec332.core.util.RegistryHelper;
import elec332.core.world.WorldHelper;
import magicbees.api.ICrumblingHandler;
import magicbees.api.ITransmutationController;
import magicbees.bees.EnumBeeBranches;
import magicbees.bees.EnumBeeHives;
import magicbees.bees.EnumBeeSpecies;
import magicbees.init.AlleleRegister;
import magicbees.init.BlockRegister;
import magicbees.init.ItemRegister;
import magicbees.init.RecipeRegister;
import magicbees.integration.botania.BotaniaIntegrationConfig;
import magicbees.proxy.ProxyCommon;
import magicbees.util.DefaultCrumblingHandler;
import magicbees.util.DefaultTransmutationController;
import magicbees.util.MagicBeesResourceLocation;
import magicbees.util.WorldGenBeeSpeciesCache;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Created by Elec332 on 16-8-2016.
 */
@Mod(modid = MagicBees.modid, name = MagicBees.modName, dependencies = "required-after:eleccore;required-after:forestry",
        acceptedMinecraftVersions = "[1.12,)", useMetadata = true)
public class MagicBees implements IElecCoreMod, IModuleController, IWindowHandler {

    public static final String modid = "magicbees";
    public static final String modName = "MagicBees";

    @SidedProxy(modId = MagicBees.modid, clientSide = "magicbees.proxy.ProxyClient", serverSide = "magicbees.proxy.ProxyCommon")
    public static ProxyCommon proxy;

    @Mod.Instance(modid)
    public static MagicBees instance;
    public static Logger logger;
    private static LoadTimer loadTimer;
    public static CreativeTabs creativeTab;
    public static ConfigWrapper config;
    public static boolean beesInitialized = false;

    public static ICrumblingHandler crumblingHandler;
    public static ITransmutationController transmutationController;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = LogManager.getLogger(modName);
        loadTimer = new LoadTimer(logger, modName);
        loadTimer.startPhase(event);
        crumblingHandler = new DefaultCrumblingHandler();
        transmutationController = new DefaultTransmutationController();
        creativeTab = AbstractCreativeTab.create(modid, new Supplier<ItemStack>() {

            @Override
            public ItemStack get() {
                return new ItemStack(RenderingRegistry.instance().registerFakeItem(new AbstractTexturedItem(MagicBeesResourceLocation.create("beezard")) {}));
            }

        });
        config = new ConfigWrapper(new Configuration(event.getSuggestedConfigurationFile()));
        config.registerConfig(new BotaniaIntegrationConfig());
        FMLUtil.getMainModBus().register(new Object(){

            @Subscribe
            public void init(Object event){
                if (event instanceof FMLInitializationEvent){
                    logger.info("Registering " + EnumBeeSpecies.values().length + " new bee species!");
                    IndividualDefinitionRegistry.registerBees(EnumBeeSpecies.class);
                }
            }

        });
        loadTimer.endPhase(event);

        MinecraftForge.EVENT_BUS.register(new BlockRegister());
        MinecraftForge.EVENT_BUS.register(new ItemRegister());
        MinecraftForge.EVENT_BUS.register(new RecipeRegister());

        BlockRegister.preInit();
        ItemRegister.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws Exception{
        loadTimer.startPhase(event);
        ItemRegister.init();
        AlleleRegister.init();
        EnumBeeBranches.registerClassifications();
        WindowManager.INSTANCE.register(this);
        beesInitialized = true;
        loadTimer.endPhase(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadTimer.startPhase(event);
        for (EnumBeeHives h : EnumBeeHives.values()){
            h.registerDrops();
        }
        WorldGenBeeSpeciesCache.populateSpeciesListRarity();
        loadTimer.endPhase(event);
    }

    @SubscribeEvent
    @SuppressWarnings("all")
    public void fixIronNugget(RegistryEvent.MissingMappings<Item> event){
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()){
            if (mapping.key.equals(MagicBeesResourceLocation.create("iron_nugget"))){
                mapping.remap(RegistryHelper.getItemRegistry().getValue(new ResourceLocation("iron_nugget")));
            }
        }
    }

    @Override
    public Window createWindow(byte b, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        TileEntity tile = WorldHelper.getTileAt(world, new BlockPos(x, y, z));
        if (tile instanceof IWindowFactory){
            return ((IWindowFactory) tile).createWindow();
        }
        return null;
    }

    @Override
    public boolean isModuleEnabled(String s) {
        return true;
    }

}
