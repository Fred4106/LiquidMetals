package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureTinFX extends TextureLiquidsFX {

	public TextureTinFX() {
		super(120, 190, 120, 190, 140, 210, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Tin")), LM_Main.molten.getTextureFile());
	}
}