package LM.Blocks;

import net.minecraft.src.TileEntity;

public class TileGrinder2 extends TileGrinder1 {
	
	public TileGrinder2() {
		super();
		cookReq = 40;
	}

	protected int getTier() {
		return 2;
	}
	
	protected boolean useEnergy() {
		if(powerProvider.useEnergy(4, 4, true) == 4)
		{
			return true;
		}
		return false;
	}
	
	protected void configPower() {
		powerProvider.configure(5, 4, 4, 4, 4);
	}
	
	@Override
	public String getInvName() {
		return "Grinder 2";
	}
	
}
