package LiquidMetals.GUI;

import net.minecraft.inventory.Container;
import LiquidMetals.Blocks.TileGrinder2;

public class GuiGrinder2 extends GuiGrinder1 {

	public GuiGrinder2(Container par1Container, TileGrinder2 te) {
		super(par1Container, te);
	}

	protected String getName() {
		return "Rough Grinder";
	}
	
}
