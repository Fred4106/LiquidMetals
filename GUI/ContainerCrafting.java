package LiquidMetals.GUI;

import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.core.proxy.CoreProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import LiquidMetals.CommonProxy;
import LiquidMetals.GrinderRecipeManager;
import LiquidMetals.Blocks.TileCrafting;
import LiquidMetals.Blocks.TileGrinder1;
public class ContainerCrafting extends Container {

	InventoryPlayer playerInventory;
	TileCrafting tile;
	
	public ContainerCrafting(InventoryPlayer inventoryPlayer, TileCrafting te) {
		playerInventory = inventoryPlayer;
		tile = te;
		
		for (int a = 0; a < 3; a++) {
			for(int b = 0; b < 3; b++) {
				this.addSlotToContainer(new SlotInputCrafting(tile, b+a*3, b*18+8, a*18+16));
			}
		}
		
		for (int var3 = 0; var3 < 2; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(te, var4 + var3 * 9 + 9, 8 + var4 * 18, 74 + var3 * 18));
			}
		}
		
		
		for (int var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(inventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 128 + var3 * 18));
			}
		}

		for (int var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(inventoryPlayer, var3, 8 + var3 * 18, 186));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}
	
	public boolean pushItemStack(ItemStack item, int lowSlot, int highSlot, boolean reverseOrder) {
		boolean pushed = false;
		int i = reverseOrder ? highSlot - 1 : lowSlot;
		Slot currentSlot;
		ItemStack targetStack;
		
		while (reverseOrder ? i >= lowSlot : i < highSlot) {
			currentSlot = (Slot)inventorySlots.get(i);
			targetStack = currentSlot.getStack();
			
			if (targetStack == null && currentSlot.isItemValid(item)) {
				targetStack = item.copy();
				targetStack.stackSize = Math.min(currentSlot.getSlotStackLimit(), item.stackSize);
				currentSlot.putStack(targetStack);
				currentSlot.onSlotChanged();
				item.stackSize -= targetStack.stackSize;
				pushed = true;
				break;
			}
			
			i = reverseOrder ? i - 1 : i + 1;
		}
		
		return pushed;
	}
	
    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer pl, int i)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(i);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (i < 27)
            {
                if (!this.mergeItemStack(var5, 27, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 9, 27, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }
    
    public static class ContainerNull extends Container
	{
		public boolean canInteractWith(EntityPlayer var1)
		{
			return false;
		}

		public void onCraftMatrixChanged(IInventory inv)
		{
		}
	}
	
}