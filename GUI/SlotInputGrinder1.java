package LiquidMetals.GUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import LiquidMetals.GrinderRecipeManager;

public class SlotInputGrinder1 extends Slot{

	public SlotInputGrinder1(EntityPlayer p, IInventory par1iInventory, int par2, int par3, int par4) {
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
			if(GrinderRecipeManager.getRecipe(item, getTier()) != null) {
				return true;
			}
		}
		return false;
	}
	
	protected int getTier() {
		return 1;
	}
}
