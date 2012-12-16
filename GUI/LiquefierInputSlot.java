package LM.GUI;

import LM.DEFAULT_SETTINGS;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class LiquefierInputSlot extends Slot{

	public LiquefierInputSlot(EntityPlayer p, IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack item)
	{
		if (item == null)
		{
			return false;
		}
		else
		{
			for(int a = 0; a < DEFAULT_SETTINGS.metals.size(); a++) {
				for(int b = 0; b < DEFAULT_SETTINGS.metals.get(a).getOres().length; b++) {
					if(item.getItem() == DEFAULT_SETTINGS.metals.get(a).getOres()[b].getItem()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
