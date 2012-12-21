package LM;

import net.minecraft.src.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

public class IngotFormerRecipe {
	
	private LiquidStack input;
	private ItemStack output;
	
	public IngotFormerRecipe(LiquidStack input, ItemStack output) {
		this.input = input;
		this.output = output;
	}
	
	public LiquidStack getInput() {
		return input.copy();
	}
	
	public ItemStack getOutput() {
		return output.copy();
	}
	
}
