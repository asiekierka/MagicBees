package magicbees.util;

import elec332.core.util.ItemStackHelper;
import elec332.core.util.OredictHelper;
import magicbees.init.ItemRegister;
import magicbees.item.types.EnumNuggetType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Elec332 on 15-5-2017.
 */
public enum EnumOreResourceType {

	IRON(new ItemStack(ItemRegister.ironNugget), "nuggetIron"),
	GOLD(new ItemStack(Items.GOLD_NUGGET)),
	COPPER(EnumNuggetType.COPPER, "nuggetCopper"),
	TIN(EnumNuggetType.TIN, "nuggetTin"),
	SILVER("nuggetSilver", "dustTinySilver"),
	LEAD("nuggetLead", "dustTinyLead"),
	ALUMINIUM("nuggetAluminium", "nuggetAluminum"),
	ARDITE("nuggetArdite"),
	COBALT("nuggetCobalt"),
	MANYULLYN("nuggetManyullyn"),
	OSMIUM("nuggetOsmium"),
	DIAMOND(EnumNuggetType.DIAMOND, "nuggetDiamond"){

		@Override
		public String getType() {
			return "gem";
		}

	},
	EMERALD(EnumNuggetType.EMERALD, "nuggetEmerald"){

		@Override
		public String getType() {
			return "gem";
		}

	},
	APATITE(EnumNuggetType.APATITE, "nuggetApatite"){

		@Override
		public String getType() {
			return "gem";
		}

	},
	SILICON,
	CERTUS,
	FLUIX,
	PLATINUM("nuggetPlatinum"),
	NICKEL("nuggetNickel", "nuggetFerrous"),
	BRONZE(EnumNuggetType.BRONZE, "nuggetBronze"),
	INVAR("nuggetInvar"),
	ELECTRUM("nuggetElectrum"),
	;

	EnumOreResourceType(EnumNuggetType nugget, String... oreDictA){
		this(nugget.getStack(), oreDictA);
	}

	EnumOreResourceType(String... oreDictA){
		this((ItemStack)null, oreDictA);
	}

	@SuppressWarnings("all")
	EnumOreResourceType(ItemStack stack, String... oreDictA){
		if (oreDictA == null){
			oreDictA = new String[0];
		}
		setStack(stack);
		this.oreDictA = oreDictA;
	}

	private String[] oreDictA;
	private ItemStack finalStack;

	public String getType(){
		return "ingot";
	}

	public static void registerRecipes(IForgeRegistry<IRecipe> registry) {
		for (EnumOreResourceType type : EnumOreResourceType.values()) {
			if (type.oreDictA.length > 0) {
				if (ItemStackHelper.isStackValid(type.finalStack)) {
					if (!(type.finalStack.getItem() == ItemRegister.ironNugget && ItemRegister.ironNugget.getRegistryName().getResourceDomain().equals("minecraft"))) {
						for (String s : type.oreDictA) {
							if (s.startsWith("nugget")) {
								OreDictionary.registerOre(s, type.finalStack);
							}
						}
						if (type.oreDictA[0].startsWith("nugget")) {
							List<ItemStack> scks = OredictHelper.getOres(type.oreDictA[0].replace("nugget", type.getType()));
							if (scks.size() > 0 && ItemStackHelper.isStackValid(scks.get(0))) {
								ResourceLocation loc = MagicBeesResourceLocation.create(type.oreDictA[0] + "_to_block");
								IRecipe recipe = new ShapedOreRecipe(loc, scks.get(0), "XXX", "XXX", "XXX", 'X', type.oreDictA[0]);
								recipe.setRegistryName(loc);
								registry.register(recipe);
							}
						}
					}
				}
			}
		}
	}

	private void setStack(ItemStack stack){
		if (!ItemStackHelper.isStackValid(stack)){
			stack = null;
		}
		this.finalStack = stack;
	}

	public boolean enabled(){
		getStack();
		return finalStack != null;
	}

	public ItemStack getStack() {
		if (oreDictA != null){
			primLoop:
			for (String oreDict : oreDictA) {
				List<ItemStack> l = OredictHelper.getOres(oreDict);
				if (!l.isEmpty()) {
					for (ItemStack stack_ : l) {
						if (ItemStackHelper.isStackValid(stack_)) {
							setStack(ItemStackHelper.copyItemStack(stack_));
							break primLoop;
						}
					}
				}
			}
			oreDictA = null;
		}
		return finalStack == null ? ItemStackHelper.NULL_STACK : finalStack;
	}
}
