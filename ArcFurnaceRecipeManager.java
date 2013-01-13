package LiquidMetals;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ArcFurnaceRecipeManager {
	
	ArrayList<ArcFurnaceRecipe> recipes = new ArrayList();
	public static ArcFurnaceRecipeManager instance = new ArcFurnaceRecipeManager();
	
	public static void addRecipe(String input, LiquidStack output) {
		ArrayList<ItemStack> ores = OreDictionary.getOres(input);
		for(int a = 0; a < ores.size(); a++) {
			addRecipe(new ItemStack(ores.get(a).itemID, 1, ores.get(a).getItemDamage()), output);
		}
	}
	
	public static void addRecipe(ItemStack input, LiquidStack output) {
		instance.recipes.add(new ArcFurnaceRecipe(input, output));
	}
	
	public static ArcFurnaceRecipe getRecipe(ItemStack item) {
		if(item == null) {
			return null;
		}
		for(int a = 0; a < instance.recipes.size(); a++) {
			if((instance.recipes.get(a).getInput().getItem().itemID == item.getItem().itemID) &&
					(instance.recipes.get(a).getInput().getItemDamage() == item.getItemDamage())) {
				int stackSize = item.stackSize;
				if(stackSize >= instance.recipes.get(a).getInput().stackSize) {
					return instance.recipes.get(a);
				}
			}
		}
		return null;
	}
}