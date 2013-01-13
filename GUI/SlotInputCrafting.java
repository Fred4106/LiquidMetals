package LiquidMetals.GUI;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotInputCrafting extends Slot {

	public SlotInputCrafting(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	/**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }

}
