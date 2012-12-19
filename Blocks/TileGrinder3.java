package LM.Blocks;

import net.minecraft.src.TileEntity;

public class TileGrinder3 extends TileGrinder1 {
	
	public TileGrinder3() {
		super();
		cookReq = 1500;
	}

	protected int getTier() {
		return 3;
	}
	
	@Override
	public String getInvName() {
		return "Grinder 3";
	}
	
}
