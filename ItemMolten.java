package LiquidMetals;

import java.util.List;

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
		String type = DEFAULT_SETTINGS.names.get(item.getItemDamage());
		return String.format("Molten %s", type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		for (int i = 0; i < DEFAULT_SETTINGS.names.size(); i++) {
			itemList.add(new ItemStack(shiftedIndex, 1, i));
		}
	}
	
}