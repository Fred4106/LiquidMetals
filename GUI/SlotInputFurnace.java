package LiquidMetals.GUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import LiquidMetals.ArcFurnaceRecipeManager;

public class SlotInputFurnace extends Slot{

	public SlotInputFurnace(EntityPlayer p, IInventory par1iInventory, int par2, int par3, int par4) {
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
			if(ArcFurnaceRecipeManager.getRecipe(item) != null) {
				return true;
			}
		}
		return false;
	}
	
	protected int getTier() {
		return 1;
	}
}
