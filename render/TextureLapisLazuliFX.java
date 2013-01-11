package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureLapisLazuliFX extends TextureLiquidsFX {

	public TextureLapisLazuliFX() {
		super(0, 0, 0, 0, 180, 255, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Lapis Lazuli")), LM_Main.molten.getTextureFile());
	}
}
