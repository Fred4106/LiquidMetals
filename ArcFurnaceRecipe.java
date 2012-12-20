package LM;

import net.minecraft.src.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

public class ArcFurnaceRecipe {
	
	private ItemStack input;
	private LiquidStack output;
	
	public ArcFurnaceRecipe(ItemStack input, LiquidStack output) {
		this.input = input;
		this.output = output;
	}
	
	public ItemStack getInput() {
		return input.copy();
	}
	
	public LiquidStack getOutput() {
		return output.copy();
	}
}
