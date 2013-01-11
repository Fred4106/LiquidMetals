package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureRedstoneFX extends TextureLiquidsFX {

	public TextureRedstoneFX() {
		super(150, 255, 30, 60, 30, 60, LM_Main.molten.getIconFromDamage(TextureLiquidsFX.liquidIndexOf("Redstone")), LM_Main.molten.getTextureFile());
	}
}
