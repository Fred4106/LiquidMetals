package LM.Blocks;

import net.minecraft.src.TileEntity;

public class TileGrinder2 extends TileGrinder1 {
	
	public TileGrinder2() {
		super();
		cookReq = 1000;
	}

	protected int getTier() {
		return 2;
	}
	
	@Override
	public String getInvName() {
		return "Grinder 2";
	}
	
}
