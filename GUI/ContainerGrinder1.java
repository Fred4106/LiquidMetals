package LM.GUI;

import LM.GrinderRecipeManager;
import LM.Blocks.TileGrinder1;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntityFurnace;

public class ContainerGrinder1 extends Container {

	InventoryPlayer playerInventory;
	TileGrinder1 tile;
	
	public ContainerGrinder1(InventoryPlayer inventoryPlayer, TileGrinder1 te) {
		playerInventory = inventoryPlayer;
		tile = te;
		
		this.addSlotToContainer(getSlot(inventoryPlayer.player, te, 0, 26, 48));
		this.addSlotToContainer(new SlotOutputGrinder(inventoryPlayer.player, te, 1, 134, 48));
		
		for (int var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(inventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (int var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(inventoryPlayer, var3, 8 + var3 * 18, 142));
		}
	}
	
	public Slot getSlot(EntityPlayer p, TileGrinder1 t, int num, int x, int y) {
		return new SlotInputGrinder1(p, t, num, x, y);
	}
	
	protected boolean pushItemStack(ItemStack item, int lowSlot, int highSlot, boolean reverseOrder) {
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
	
	public int getTier() {
		return 1;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
		ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(i);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (i == 1)
            {
                if (!this.mergeItemStack(var5, 2, 38, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (i != 0)
            {
            	if (GrinderRecipeManager.getRecipe(var5, getTier()) != null)
                {
                    if (!this.mergeItemStack(var5, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (i >= 2 && i < 29)
                {
                    if (!this.mergeItemStack(var5, 29, 38, false))
                    {
                        return null;
                    }
                }
                else if (i >= 29 && i < 38 && !this.mergeItemStack(var5, 2, 29, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 2, 38, false))
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

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(pl, var5);
        }

        return var3;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return true;
	}

}
