package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureCopperFX extends TextureLiquidsFX {

	public TextureCopperFX() {
		super(110, 180, 45, 115, 0, 70, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Copper")), LM_Main.molten.getTextureFile());
	}
}
