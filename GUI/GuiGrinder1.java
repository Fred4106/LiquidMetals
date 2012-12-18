package LM.GUI;

import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Item;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import LM.Blocks.LiquefierTile;
import LM.Blocks.TileGrinder1;

public class GuiGrinder1 extends GuiContainer {

	TileGrinder1 grinder;
	
	public GuiGrinder1(Container par1Container, TileGrinder1 te) {
		super(par1Container);
		grinder = te;
		xSize = 175;
		ySize = 165;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int i = mc.renderEngine.getTexture("/LM/gfx/LiquidMetal/grinder1.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(i);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		int getScaledDist = (int) (90*grinder.cookTime/grinder.cookReq);
		drawTexturedModalRect(left+44, top+51, 0, 166, getScaledDist, 174);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		fontRenderer.drawString(""+grinder.cookTime+"/50", 10, 10, 0x404040);
	}

}