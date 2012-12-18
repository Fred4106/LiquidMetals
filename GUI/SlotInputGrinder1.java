package LM.GUI;

import LM.GrinderRecipeManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

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
			System.out.println(item);
			if(GrinderRecipeManager.getRecipe(item, getTier()) != null) {
				System.out.println(getTier() + " " + GrinderRecipeManager.getRecipe(item, getTier()).toString());
				return true;
			}
		}
		return false;
	}
	
	protected int getTier() {
		return 1;
	}
}
