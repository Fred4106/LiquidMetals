package LM;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrinderRecipeManager {
	private ArrayList<GrinderRecipe> recipes = new ArrayList();
	public static GrinderRecipeManager instance = new GrinderRecipeManager();
	
	public static void addRecipe(ItemStack input, ItemStack output, int tier) {
		instance.recipes.add(new GrinderRecipe(input, output, tier));
	}
	
	public static void addRecipe(String input, int inputAmount, ItemStack output, int tier) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(input);
		for(int a = 0; a < ores.size(); a++) {
			addRecipe(new ItemStack(ores.get(a).itemID, inputAmount, ores.get(a).getItemDamage()), output, tier);
		}
	}
	
	public static GrinderRecipe getRecipe(ItemStack item, int tier) {
		if(item == null) {
			return null;
		}
		for(int a = 0; a < instance.recipes.size(); a++) {
  			if((instance.recipes.get(a).getInput().getItem().shiftedIndex == item.getItem().shiftedIndex)  && (instance.recipes.get(a).getInput().getItemDamage() == item.getItemDamage())) {
				int stackSize = item.stackSize;
  				if(stackSize >= instance.recipes.get(a).getInput().stackSize)
  					return instance.recipes.get(a);
			}
		}
		return null;
	}
}
