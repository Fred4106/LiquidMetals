package LiquidMetals;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Metal {

	private String prefix;
	private ItemStack item = null;
	private int amount;
	private String oreDictName = "";
	
	public Metal(String prefix, ItemStack item, int amount/*amount of liquid to equal one of the itemstack*/) {
		this.prefix = prefix;
		this.item = item;
		this.amount = amount;
	}
	
	public Metal(String prefix, String oreDictName, int amount/*amount of liquid to equal one of the itemstack*/) {
		this.prefix = prefix;
		this.oreDictName = oreDictName;
		this.amount = amount;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public String getOreDictName() {
		return this.oreDictName;
	}
	
	public int getAmount() {
		return amount;
	}
}
