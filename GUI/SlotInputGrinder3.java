package LM.GUI;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;

public class SlotInputGrinder3 extends SlotInputGrinder1 {

	public SlotInputGrinder3(EntityPlayer p, IInventory par1iInventory, int par2, int par3, int par4) {
		super(p, par1iInventory, par2, par3, par4);
	}

	protected int getTier() {
		return 3;
	}
	
}
