package magicbees.init;

import magicbees.MagicBees;
import magicbees.block.BlockEffectJar;
import magicbees.block.BlockHive;
import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;


/**
 * Created by Elec332 on 4-3-2017.
 */
public final class BlockRegister {
    public static BlockHive hiveBlock;
    public static Block effectJar;

    public static void preInit() {
        hiveBlock = new BlockHive();
        hiveBlock.setRegistryName(MagicBeesResourceLocation.create("hiveBlock"));
        hiveBlock.setCreativeTab(MagicBees.creativeTab);

        effectJar = new BlockEffectJar();
        effectJar.setRegistryName(MagicBeesResourceLocation.create("effectJar"));
        effectJar.setCreativeTab(MagicBees.creativeTab);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(hiveBlock, effectJar);
    }

}
