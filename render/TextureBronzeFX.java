package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureBronzeFX extends TextureLiquidsFX {

	public TextureBronzeFX() {
		super(50, 100, 0, 50, 0, 20, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Bronze")), LM_Main.molten.getTextureFile());
	}
}
