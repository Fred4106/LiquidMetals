package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureIronFX extends TextureLiquidsFX {

	public TextureIronFX() {
		super(80, 150, 80, 150, 80, 150, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Iron")), LM_Main.molten.getTextureFile());
	}
}
