package LiquidMetals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemMarker extends ItemLiquidMetal{

	public ItemMarker(int i) {
		super(i);
		setCreativeTab(CreativeTabs.tabMaterials);
		setMaxStackSize(8);
		setIconIndex(240);
		setItemName("Marker");
		setHasSubtypes(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damage) {
		return iconIndex + damage;
	}
	
	@Override
	public String getItemDisplayName(ItemStack item) {
		int damage = item.getItemDamage();
		if(damage == 0) {
			return "Red Liquid Marker";
		}
		if(damage == 1) {
			return "Green Liquid Marker";
		}
		if(damage == 2) {
			return "Blue Liquid Marker";
		}
		return "Report to Fred4106";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		itemList.add(new ItemStack(shiftedIndex, 1, 0));
		itemList.add(new ItemStack(shiftedIndex, 1, 1));
		itemList.add(new ItemStack(shiftedIndex, 1, 2));
	}

}
