package LiquidMetals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSand extends ItemLiquidMetal{

	public ItemSand(int i) {
		super(i);
		setCreativeTab(CreativeTabs.tabMaterials);
		setMaxStackSize(64);
		setIconIndex(1);
		setItemName("Sand");
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damage) {
		return iconIndex + (3*damage);
	}
	
	@Override
	public String getItemDisplayName(ItemStack item) {
		String type = DEFAULT_SETTINGS.itemNames.get(item.getItemDamage());
		return String.format("%s Ore Sand", type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		Set s = DEFAULT_SETTINGS.itemNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			itemList.add(new ItemStack(shiftedIndex, 1, (Integer)m.getKey()));
		}
	}
	
}