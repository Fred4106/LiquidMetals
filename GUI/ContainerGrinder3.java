package LiquidMetals.GUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import LiquidMetals.Blocks.TileGrinder1;
import LiquidMetals.Blocks.TileGrinder3;

public class ContainerGrinder3 extends ContainerGrinder1 {

	public ContainerGrinder3(InventoryPlayer inventoryPlayer, TileGrinder3 te) {
		super(inventoryPlayer, te);
	}

	public int getTier() {
		return 3;
	}
	
	@Override
	public Slot getSlot(EntityPlayer p, TileGrinder1 t, int num, int x, int y) {
		return new SlotInputGrinder3(p, t, num, x, y);
	}
}
