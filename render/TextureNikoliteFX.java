package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureNikoliteFX extends TextureLiquidsFX {

	public TextureNikoliteFX() {
		super(50, 100, 100, 150, 220, 255, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Nikolite")), LM_Main.molten.getTextureFile());
	}
}
