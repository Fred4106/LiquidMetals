package LM.GUI;

import net.minecraft.src.Container;
import LM.Blocks.TileGrinder1;
import LM.Blocks.TileGrinder2;
import LM.Blocks.TileGrinder3;

public class GuiGrinder3 extends GuiGrinder1 {

	public GuiGrinder3(Container par1Container, TileGrinder3 te) {
		super(par1Container, te);
	}

	protected String getName() {
		return "Large Grinder";
	}
	
}
