package LM;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Metal {
	
	private String oreName = "";
	private ItemStack ore;
	private String ingotName = "";
	private ItemStack ingot;
	private int dustId;
	
	public Metal(ItemStack ore, int dustId, ItemStack ingot) {
		this.ore = ore;
		this.ingot = ingot;
		this.dustId = dustId;
	}
	
	public Metal(String oreIdent, int dustId, String outputIdent) {
		oreName = oreIdent;
		this.dustId = dustId;
		ingotName = outputIdent;
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
