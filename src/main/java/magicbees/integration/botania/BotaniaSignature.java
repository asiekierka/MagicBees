package magicbees.integration.botania;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.subtile.signature.SubTileSignature;

/**
 * Created by Elec332 on 18-5-2017.
 */
public class BotaniaSignature implements SubTileSignature {

	public BotaniaSignature(String subtileName) {
		this.name = subtileName;
	}

	private String name;

	@Override
	public String getUnlocalizedNameForStack(ItemStack stack) {
		return "tile.botania:flower." + name;
	}

	@Override
	public String getUnlocalizedLoreTextForStack(ItemStack stack) {
		return "tile.botania:flower." + name + ".lore";
	}

}