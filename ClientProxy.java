package LiquidMetals;

import net.minecraft.client.renderer.RenderEngine;
import net.minecraftforge.client.MinecraftForgeClient;
import LiquidMetals.render.TextureBrassFX;
import LiquidMetals.render.TextureBronzeFX;
import LiquidMetals.render.TextureCopperFX;
import LiquidMetals.render.TextureGlowstoneFX;
import LiquidMetals.render.TextureGoldFX;
import LiquidMetals.render.TextureIronFX;
import LiquidMetals.render.TextureLapisLazuliFX;
import LiquidMetals.render.TextureLeadFX;
import LiquidMetals.render.TextureNikoliteFX;
import LiquidMetals.render.TextureRedstoneFX;
import LiquidMetals.render.TextureSilverFX;
import LiquidMetals.render.TextureTinFX;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Overriden proxy to additionally handle things that need to happen on the client side.
 *
 * Mostly this means preloading textures.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	private static String[] toPreload = {"Icons.png", "Acrfurnace.png", "grinder1.png", "IngotFormer.png", "LiquidCrafting.png", "Liquids.png", "Liquifier.png"};
	private static String path = "/LM/gfx/LiquidMetal/";
	
	@Override
	public void registerRenderers() {
		super.registerRenderers();
		for(int a = 0; a < toPreload.length; a++) {
			MinecraftForgeClient.preloadTexture(path+toPreload[a]);
			System.out.println("[Liquid Metals] Preloaded \""+path+toPreload[a] + "\".");
		}
	}
	
	@Override
	public void registerTextureFX() {
		RenderEngine renderEngine = FMLClientHandler.instance().getClient().renderEngine;
		renderEngine.registerTextureFX(new TextureIronFX());
		renderEngine.registerTextureFX(new TextureGoldFX());
		renderEngine.registerTextureFX(new TextureCopperFX());
		renderEngine.registerTextureFX(new TextureTinFX());
		renderEngine.registerTextureFX(new TextureSilverFX());
		renderEngine.registerTextureFX(new TextureLeadFX());
		renderEngine.registerTextureFX(new TextureRedstoneFX());
		renderEngine.registerTextureFX(new TextureGlowstoneFX());
		renderEngine.registerTextureFX(new TextureLapisLazuliFX());
		renderEngine.registerTextureFX(new TextureNikoliteFX());
		renderEngine.registerTextureFX(new TextureBrassFX());
		renderEngine.registerTextureFX(new TextureBronzeFX());
		
	}
}