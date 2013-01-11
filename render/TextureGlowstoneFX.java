package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureGlowstoneFX extends TextureLiquidsFX {

	public TextureGlowstoneFX() {
		super(240, 255, 200, 255, 0, 40, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Glowstone")), LM_Main.molten.getTextureFile());
	}
}
