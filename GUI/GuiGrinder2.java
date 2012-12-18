package LM.GUI;

import net.minecraft.src.Container;
import LM.Blocks.TileGrinder1;
import LM.Blocks.TileGrinder2;

public class GuiGrinder2 extends GuiGrinder1 {

	public GuiGrinder2(Container par1Container, TileGrinder2 te) {
		super(par1Container, te);
	}

	protected String getName() {
		return "Medium Grinder";
	}
	
}
