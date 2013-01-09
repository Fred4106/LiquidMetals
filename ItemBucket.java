package LiquidMetals;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBucket extends ItemLiquidMetal{

	public ItemBucket(int i) {
		super(i);
		setTextureFile("/LiquidMetals/gfx/LiquidMetal/Liquids.png");
		setCreativeTab(CreativeTabs.tabMaterials);
		setMaxStackSize(1);
		setContainerItem(Item.bucketEmpty);
		setIconIndex(1);
		setItemName("BucketMolten");
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
		return String.format("Bucket of Molten %s", type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		for (int i = 0; i < DEFAULT_SETTINGS.names.size(); i++) {
			itemList.add(new ItemStack(shiftedIndex, 1, i));
		}
	}
	
}