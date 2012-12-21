package LM;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

public class IngotFormerRecipeManager {

	ArrayList<IngotFormerRecipe> recipes = new ArrayList();
	public static IngotFormerRecipeManager instance = new IngotFormerRecipeManager();
	
	public static void addRecipe(LiquidStack input, ItemStack output) {
		instance.recipes.add(new IngotFormerRecipe(input, output));
	}
	
	public static void addRecipe(LiquidStack input, String output) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(output);
		if(ores.size() == 0){
			return;
		}
		instance.recipes.add(new IngotFormerRecipe(input, ores.get(0)));
	}
	
	public static IngotFormerRecipe getRecipe(LiquidStack item) {
		if(item == null) {
			return null;
		}
		for(int a = 0; a < instance.recipes.size(); a++) {
			if((instance.recipes.get(a).getInput().itemID == item.itemID) &&
					(instance.recipes.get(a).getInput().itemMeta == item.itemMeta)) {
				int stackSize = item.amount;
				if(stackSize >= instance.recipes.get(a).getInput().amount) {
					return instance.recipes.get(a);
				}
			}
		}
		return null;
	}
}
