package LM.Blocks;

import net.minecraft.src.TileEntity;

public class TileGrinder3 extends TileGrinder1 {
	
	public TileGrinder3() {
		super();
		cookReq = 30;
	}
	
	protected boolean useEnergy() {
		if(powerProvider.useEnergy(80, 80, true) == 80)
		{
			return true;
		}
		return false;
	}
	
	protected void configPower() {
		powerProvider.configure(5, 80, 80, 80, 80);
	}

	protected int getTier() {
		return 3;
	}
	
	@Override
	public String getInvName() {
		return "Grinder 3";
	}
	
}
