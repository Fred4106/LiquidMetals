package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureBrassFX extends TextureLiquidsFX {

	public TextureBrassFX() {
		super(225, 255, 140, 220, 20, 50, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Brass")), LM_Main.molten.getTextureFile());
	}
}
