package LiquidMetals.render;

import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;

public class TextureLeadFX extends TextureLiquidsFX {

	public TextureLeadFX() {
		super(20, 90, 20, 90, 20, 90, LM_Main.molten.getIconFromDamage(DEFAULT_SETTINGS.names.indexOf("Lead")), LM_Main.molten.getTextureFile());
	}
}