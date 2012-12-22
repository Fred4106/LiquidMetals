package LM.Blocks;

import net.minecraft.src.TileEntity;

public class TileGrinder2 extends TileGrinder1 {
	
	public TileGrinder2() {
		super();
		cookReq = 20;
	}

	protected int getTier() {
		return 2;
	}
	
	protected boolean useEnergy() {
		if(powerProvider.useEnergy(40, 40, true) == 40)
		{
			return true;
		}
		return false;
	}
	
	protected void configPower() {
		powerProvider.configure(5, 40, 40, 40, 40);
	}
	
	@Override
	public String getInvName() {
		return "Grinder 2";
	}
	
}
