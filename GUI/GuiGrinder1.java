package LiquidMetals.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

import LiquidMetals.Blocks.TileGrinder1;

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
		int i = mc.renderEngine.getTexture("/LiquidMetals/gfx/LiquidMetal/grinder1.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(i);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		int getScaledDist = (int) (90*grinder.cookTime/grinder.cookReq);
		drawTexturedModalRect(left+44, top+51, 0, 166, getScaledDist, 10);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		fontRenderer.drawString(getName(), 10, 10, 0x404040);
	}
	
	protected String getName() {
		return "Rock Pulverizer";
	}

}