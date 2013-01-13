package LiquidMetals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Metal {

	private String prefix;
	private ItemStack item;
	private int amount;
	
	public Metal(String prefix, ItemStack item, int amount/*amount of liquid to equal one of the itemstack*/) {
		this.prefix = prefix;
		this.item = item;
		this.amount = amount;
	}
	
	public Metal(String prefix, String oreDictName, int amount/*amount of liquid to equal one of the itemstack*/) {
		this.prefix = prefix;
		this.amount = amount;
		ArrayList temp = OreDictionary.getOres(oreDictName);
		if(temp.size() > 0) {
			item = (ItemStack) temp.get(0);
		} else {
			item = null;
		}
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public int getAmount() {
		return amount;
	}
}
