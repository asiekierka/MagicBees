package magicbees.util;

import magicbees.MagicBees;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 23-8-2016.
 */
public class MagicBeesResourceLocation extends ResourceLocation {
    @Deprecated
    public MagicBeesResourceLocation(String resourceName) {
        super(MagicBees.modid, resourceName);
    }

    public static ResourceLocation create(String resourceName) {
        return new ResourceLocation(MagicBees.modid, resourceName);
    }
}
