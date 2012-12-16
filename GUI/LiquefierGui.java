package LM.GUI;

import org.lwjgl.opengl.GL11;

import buildcraft.core.DefaultProps;

import LM.Blocks.LiquefierTile;
import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Item;
import net.minecraftforge.client.ForgeHooksClient;

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
		displayGauge(left, top, 13, 116, liquefier.getScaledOutput(), liquefier.getOutputId(), liquefier.getOutputMeta());
		displayGauge(left, top, 13, 134, liquefier.getScaledWater(), liquefier.getWaterId(), liquefier.getWaterMeta());
	}

	private void displayGauge(int j, int k, int line, int col, int squaled, int liquidId, int liquidMeta) {
		int liquidImgIndex = 0;

		if (liquidId <= 0) {
			return;
		} if (liquidId < Block.blocksList.length && Block.blocksList[liquidId] != null) {
			ForgeHooksClient.bindTexture(Block.blocksList[liquidId].getTextureFile(), 0);
			liquidImgIndex = Block.blocksList[liquidId].blockIndexInTexture;
		} else if (Item.itemsList[liquidId] != null) {
			ForgeHooksClient.bindTexture(Item.itemsList[liquidId].getTextureFile(), 0);
			liquidImgIndex = Item.itemsList[liquidId].getIconFromDamage(liquidMeta);
		} else {
			return;
		}
		
		int imgLine = liquidImgIndex / 16;
		int imgColumn = liquidImgIndex - imgLine * 16;

		int start = 0;

		while (true) {
			int x = 0;

			if (squaled > 16) {
				x = 16;
				squaled -= 16;
			} else {
				x = squaled;
				squaled = 0;
			}

			drawTexturedModalRect(j + col, k + line + 58 - x - start, imgColumn * 16, imgLine * 16 + (16 - x), 16, 16 - (16 - x));
			start = start + 16;

			if (x == 0 || squaled == 0)
				break;
		}
		int i = mc.renderEngine.getTexture("/LM/gfx/LiquidMetal/liquifier.png");

		mc.renderEngine.bindTexture(i);
		drawTexturedModalRect(j + col, k + line, 176, 0, 16, 60);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		fontRenderer.drawString("Stove", 10, 10, 0x404040);
	}

}
