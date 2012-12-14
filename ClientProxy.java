package LM;

import LM.render.TextureGoldFX;
import LM.render.TextureIronFX;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Overriden proxy to additionally handle things that need to happen on the client side.
 *
 * Mostly this means preloading textures.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		super.registerRenderers();
		MinecraftForgeClient.preloadTexture("/LM/gfx/LiquidMetal/Icons.png");
	}
	
	@Override
	public void registerTextureFX() {
		net.minecraft.src.RenderEngine renderEngine = FMLClientHandler.instance().getClient().renderEngine;
		renderEngine.registerTextureFX(new TextureIronFX());
		renderEngine.registerTextureFX(new TextureGoldFX());
	}
}