package LiquidMetals.GUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import LiquidMetals.Blocks.TileGrinder1;
import LiquidMetals.Blocks.TileGrinder2;

public class ContainerGrinder2 extends ContainerGrinder1 {

	public ContainerGrinder2(InventoryPlayer inventoryPlayer, TileGrinder2 te) {
		super(inventoryPlayer, te);
	}

	public int getTier() {
		return 2;
	}
	
	@Override
	public Slot getSlot(EntityPlayer p, TileGrinder1 t, int num, int x, int y) {
		return new SlotInputGrinder2(p, t, num, x, y);
	}
}
