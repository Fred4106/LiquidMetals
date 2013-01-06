package LiquidMetals.Blocks;


public class TileGrinder3 extends TileGrinder1 {
	
	public TileGrinder3() {
		super();
		cookReq = 30;
	}
	
	protected boolean useEnergy() {
		if(this.redstonePowered == true) {
			return false;
		}
		if(powerProvider.useEnergy(160, 160, true) == 160)
		{
			return true;
		}
		return false;
	}
	
	protected void configPower() {
		powerProvider.configure(5, 160, 160, 160, 160);
	}

	protected int getTier() {
		return 3;
	}
	
	@Override
	public String getInvName() {
		return "Grinder 3";
	}
	
}
