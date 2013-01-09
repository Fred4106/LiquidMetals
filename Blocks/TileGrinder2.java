package LiquidMetals.Blocks;


public class TileGrinder2 extends TileGrinder1 {
	
	public TileGrinder2() {
		super();
		cookReq = 20;
		this.powerReq = 80;
		this.configPower();
	}

	protected int getTier() {
		return 2;
	}
	
	@Override
	public String getType() {
		return "Rough Grinder";
	}
	
	@Override
	public String getInvName() {
		return "Grinder 2";
	}
	
}
