package LiquidMetals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMolten extends ItemLiquidMetal{

	public ItemMolten(int i) {
		super(i);
		setTextureFile("/LiquidMetals/gfx/LiquidMetal/Liquids.png");
		setMaxStackSize(64);
		setIconIndex(0);
		setItemName("Molten");
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damage) {
		return iconIndex + (2*damage);
	}
	
	@Override
	public String getItemDisplayName(ItemStack item) {
		String type = DEFAULT_SETTINGS.liquidNames.get(item.getItemDamage()).getPrefix();
		return String.format("Molten %s", type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		Set s = DEFAULT_SETTINGS.liquidNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			itemList.add(new ItemStack(shiftedIndex, 1, (Integer)m.getKey()));
		}
	}
	
}