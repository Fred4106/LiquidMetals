package LM.GUI;

import org.lwjgl.opengl.GL11;

import LM.Blocks.LiquefierTile;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;

public class LiquefierGui extends GuiContainer {

	LiquefierTile liquefier;
	
	public LiquefierGui(Container par1Container, LiquefierTile te) {
		super(par1Container);
		liquefier = te;
		xSize = 175;
		ySize = 165;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int i = mc.renderEngine.getTexture("/LM/gfx/LiquidMetal/liquifier.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(i);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		//int heat = stove.getEffectiveHeatLevel();
		//drawTexturedModalRect(left+85, top+52, xSize, 5*heat, 6, 5);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		fontRenderer.drawString("Stove", 10, 10, 0x404040);
	}

}
