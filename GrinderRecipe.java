package LM;

import net.minecraft.src.ItemStack;

public class GrinderRecipe {
	private ItemStack input;
	private ItemStack output;
	private int tier;
	
	public GrinderRecipe(ItemStack input, ItemStack output, int tier) {
		this.input = input;
		this.output = output;
		this.tier = tier;
	}

	public ItemStack getOutput() {
		return output.copy();
	}
	
	public ItemStack getInput() {
		return input.copy();
	}
	
	public String toString() {
		return input.itemID + " " + input.getItemDamage() + " " + input.stackSize + " " +output.itemID + " " + output.getItemDamage() + " " + output.stackSize + " " + tier;
	}
}
