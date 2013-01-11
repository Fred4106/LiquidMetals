package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureGoldFX extends TextureLiquidsFX {

	public TextureGoldFX() {
		super(225, 255, 170, 250, 50, 90, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Gold")), LM_Main.molten.getTextureFile());
	}
}
