package LM.GUI;

import LM.Blocks.LiquefierTile;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class LiquefierContainer extends Container{

	InventoryPlayer playerInventory;
	LiquefierTile tile;
	
	public LiquefierContainer(InventoryPlayer par1InventoryPlayer, LiquefierTile te)
	{
		playerInventory = par1InventoryPlayer;
		tile = te;
		
        this.addSlotToContainer(new LiquefierInputSlot(par1InventoryPlayer.player, te, 0, 26, 37));
		this.addSlotToContainer(new LiquefierInputSlot(par1InventoryPlayer.player, te, 1, 44, 37));
		this.addSlotToContainer(new LiquefierInputSlot(par1InventoryPlayer.player, te, 2, 26, 55));
		this.addSlotToContainer(new LiquefierInputSlot(par1InventoryPlayer.player, te, 3, 44, 55));
		
		for (int var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (int var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		}
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
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
		ItemStack itemstack = null;
		if(!(inventorySlots.get(i) instanceof LiquefierInputSlot)) {
			if(!((LiquefierInputSlot)inventorySlots.get(0)).isItemValid(((Slot)inventorySlots.get(i)).getStack())) {
				return null;
			}
		}
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < ((IInventory)tile).getSizeInventory()) {
				if (!mergeItemStack(itemstack1, ((IInventory)tile).getSizeInventory(), inventorySlots.size(), true))
					return null;
			} else if (!mergeItemStack(itemstack1, 0, ((IInventory)tile).getSizeInventory(), false))
				return null;
			if (itemstack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
		}
		return itemstack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
