package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureSilverFX extends TextureLiquidsFX {

	public TextureSilverFX() {
		super(160, 230, 160, 230, 160, 230, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Silver")), LM_Main.molten.getTextureFile());
	}
}
