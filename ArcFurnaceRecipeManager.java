package LM;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

public class ArcFurnaceRecipeManager {
	
	ArrayList<ArcFurnaceRecipe> recipes = new ArrayList();
	public static ArcFurnaceRecipeManager instance = new ArcFurnaceRecipeManager();
	
	public static void addRecipe(ItemStack input, LiquidStack output) {
		instance.recipes.add(new ArcFurnaceRecipe(input, output));
	}
	
	public static ArcFurnaceRecipe getRecipe(ItemStack item) {
		if(item == null) {
			return null;
		}
		for(int a = 0; a < instance.recipes.size(); a++) {
			if((instance.recipes.get(a).getInput().getItem().shiftedIndex == item.getItem().shiftedIndex) &&
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
