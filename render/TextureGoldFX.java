package LM.render;

import LM.DEFAULT_SETTINGS;
import LM.LM_Main;

public class TextureGoldFX extends TextureLiquidsFX {

	public TextureGoldFX() {
		super(225, 255, 200, 245, 50, 90, LM_Main.molten.getIconFromDamage(DEFAULT_SETTINGS.names.indexOf("Gold")), LM_Main.molten.getTextureFile());
		System.out.println(DEFAULT_SETTINGS.names.indexOf("Gold") + " Gold");
	}
}
