package LM;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;
import net.minecraftforge.liquids.LiquidStack;

public class RecipeManager {
	public class ArcFurnaceRecipe {
		private ItemStack input;
		private LiquidStack output;
		public ArcFurnaceRecipe(ItemStack input, LiquidStack output) {
			this.input = input;
			this.output = output;
		}
		
		public ItemStack getInput() {
			return input;
		}
		
		public LiquidStack getOutput() {
			return output;
		}
	}
	public class GrinderRecipe {
		private ItemStack input;
		private ItemStack output;
		private int tier;
		public GrinderRecipe(int tier, ItemStack input, ItemStack output) {
			this.tier = tier;
			this.input = input;
			this.output = output;
		}
		
		public int getTier() {
			return tier;
		}
		
		public ItemStack getInput() {
			return input;
		}
		
		public ItemStack getOutput() {
			return output;
		}
	}
	
	private static ArrayList<GrinderRecipe> grinderRecipies = new ArrayList();
	private static ArrayList<ArcFurnaceRecipe> arcFurnaceRecipies = new ArrayList();
	
	private static RecipeManager instance;
	public RecipeManager() {
		instance = this;
	}
	
	public static void addRecipeGrinder(int tier, ItemStack input, ItemStack output) {
		grinderRecipies.add(instance.new GrinderRecipe(tier, input, output));
	}
	
	public static void addRecipeArcFurnace(ItemStack input, LiquidStack output) {
		arcFurnaceRecipies.add(instance.new ArcFurnaceRecipe(input, output));
	}
	
	public static GrinderRecipe getGrinderRecipe(ItemStack input, int tier) {
		for(int a = 0; a < grinderRecipies.size(); a++) {
			if(grinderRecipies.get(a).getTier() == tier) {
				if(grinderRecipies.get(a).getInput().getItem() == input.getItem()) {
					if(grinderRecipies.get(a).getInput().stackSize <= input.stackSize) {
						return grinderRecipies.get(a);
					}
				}
			}
		}
		return null;
	}
	
	public static boolean validGrinderInput(ItemStack input, int tier) {
		for(int a = 0; a < grinderRecipies.size(); a++) {
			if(grinderRecipies.get(a).getInput().getItem() == input.getItem()) {
				if(grinderRecipies.get(a).getTier() == tier) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean validArcInput(ItemStack input) {
		for(int a = 0; a < arcFurnaceRecipies.size(); a++) {
			if(arcFurnaceRecipies.get(a).getInput().getItem() == input.getItem()) {
				return true;
			}
		}
		return false;
	}
	
	public static ArcFurnaceRecipe getArcFurnaceRecipe(ItemStack input) {
		for(int a = 0; a < arcFurnaceRecipies.size(); a++) {
			if(arcFurnaceRecipies.get(a).getInput().getItem() == input.getItem()) {
				if(arcFurnaceRecipies.get(a).getInput().stackSize <= input.stackSize) {
					return arcFurnaceRecipies.get(a);
				}
			}
		}
		return null;
	}
}