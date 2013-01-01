package LiquidMetals.GUI;

import net.minecraft.inventory.Container;
import LiquidMetals.Blocks.TileGrinder3;

public class GuiGrinder3 extends GuiGrinder1 {

	public GuiGrinder3(Container par1Container, TileGrinder3 te) {
		super(par1Container, te);
	}

	protected String getName() {
		return "Fine Grinder";
	}
	
}
