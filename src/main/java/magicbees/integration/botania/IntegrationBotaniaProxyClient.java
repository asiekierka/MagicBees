package magicbees.integration.botania;

import magicbees.util.MagicBeesResourceLocation;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.BotaniaAPIClient;

public class IntegrationBotaniaProxyClient extends IntegrationBotaniaProxy {
    @Override
    @Optional.Method(modid = "botania")
    public void preInit() {
        ModelResourceLocation mrl = new ModelResourceLocation(MagicBeesResourceLocation.create("beegonia"), "normal");
        BotaniaAPIClient.registerSubtileModel(SubTileBeegonia.class, mrl);
    }
}
