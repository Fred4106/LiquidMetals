package LM.GUI;

import LM.Blocks.TileGrinder1;
import LM.Blocks.TileGrinder2;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerGrinder2 extends ContainerGrinder1 {

	public ContainerGrinder2(InventoryPlayer inventoryPlayer, TileGrinder2 te) {
		super(inventoryPlayer, te);
	}

	@Override
	public Slot getSlot(EntityPlayer p, TileGrinder1 t, int num, int x, int y) {
		System.out.println("Please print out :)");
		return new SlotInputGrinder2(p, t, num, x, y);
	}
}
