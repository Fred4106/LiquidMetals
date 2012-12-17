package LM.render;

import LM.DEFAULT_SETTINGS;
import LM.LM_Main;

public class TextureTinFX extends TextureLiquidsFX {

	public TextureTinFX() {
		super(120, 190, 120, 190, 140, 210, LM_Main.molten.getIconFromDamage(DEFAULT_SETTINGS.names.indexOf("Tin")), LM_Main.molten.getTextureFile());
		System.out.println(DEFAULT_SETTINGS.names.indexOf("Tin") + " Tin");
	}
}