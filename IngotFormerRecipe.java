package LiquidMetals;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

public class IngotFormerRecipe {
	
	private LiquidStack input;
	private ItemStack output;
	
	public IngotFormerRecipe(LiquidStack input, ItemStack output) {
		this.input = input;
		this.output = new ItemStack(output.itemID, 1, output.getItemDamage());
	}
	
	public LiquidStack getInput() {
		return input.copy();
	}
	
	public ItemStack getOutput() {
		return output.copy();
	}
	
}
