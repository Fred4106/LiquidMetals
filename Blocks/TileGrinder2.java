package LiquidMetals.Blocks;


public class TileGrinder2 extends TileGrinder1 {
	
	public TileGrinder2() {
		super();
		cookReq = 20;
	}

	protected int getTier() {
		return 2;
	}
	
	protected boolean useEnergy() {
		if(this.redstonePowered == true) {
			return false;
		}
		if(powerProvider.useEnergy(80, 80, true) == 80)
		{
			return true;
		}
		return false;
	}
	
	protected void configPower() {
		powerProvider.configure(5, 80, 80, 80, 80);
	}
	
	@Override
	public String getInvName() {
		return "Grinder 2";
	}
	
}
