package LiquidMetals.Blocks;


public class TileGrinder3 extends TileGrinder1 {
	
	public TileGrinder3() {
		super();
		cookReq = 30;
		this.powerReq = 160;
		this.configPower();
	}

	protected int getTier() {
		return 3;
	}
	
	@Override
	public String getType() {
		return "Fine Grinder";
	}
	
	@Override
	public String getInvName() {
		return "Grinder 3";
	}
	
}
