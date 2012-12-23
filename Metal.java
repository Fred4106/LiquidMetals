package LiquidMetals;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Metal {
	
	private String oreName = "";
	private ItemStack ore;
	private String ingotName = "";
	private ItemStack ingot;
	private int dustId;
	private LiquidStack liquidStack;
	
	public Metal(ItemStack ore, int dustId, LiquidStack liquidStack, ItemStack ingot) {
		this.ore = ore;
		this.ingot = ingot;
		this.dustId = dustId;
		this.liquidStack = liquidStack;
	}
	
	public Metal(String oreIdent, int dustId, LiquidStack liquidStack, String outputIdent) {
		oreName = oreIdent;
		this.dustId = dustId;
		ingotName = outputIdent;
		this.liquidStack = liquidStack;
	}
	
	public ItemStack getIngotOutput() {
		if(ingotName.equals("")) {
			return ingot;
		}
		if(OreDictionary.getOres(ingotName).size() == 0) {
			return null;
		}
		return OreDictionary.getOres(ingotName).get(0);
	}
	
	public ItemStack[] getOres() {
		if(oreName.equals("")) {
			return new ItemStack[] {ore};
		}
		ArrayList<ItemStack> oreIds = OreDictionary.getOres(oreName);
		ItemStack[] toReturn = new ItemStack[oreIds.size()];
		for(int a = 0; a <  oreIds.size(); a++) {
			toReturn[a] = oreIds.get(a);
		}
		return toReturn;
	}
}
