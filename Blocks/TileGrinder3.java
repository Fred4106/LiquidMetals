package LM.Blocks;

import net.minecraft.src.TileEntity;

public class TileGrinder3 extends TileGrinder1 {
	
	public TileGrinder3() {
		super();
		cookReq = 80;
	}
	
	protected boolean useEnergy() {
		if(powerProvider.useEnergy(8, 8, true) == 8)
		{
			return true;
		}
		return false;
	}
	
	protected void configPower() {
		powerProvider.configure(5, 8, 8, 8, 8);
	}

	protected int getTier() {
		return 3;
	}
	
	@Override
	public String getInvName() {
		return "Grinder 3";
	}
	
}
