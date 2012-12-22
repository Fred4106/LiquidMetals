package LM.GUI;

import org.lwjgl.opengl.GL11;

import LM.Blocks.TileFurnace;
import LM.Blocks.TileIngotFormer;
import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Item;
import net.minecraftforge.client.ForgeHooksClient;

public class GuiIngotFormer extends GuiContainer {

	TileIngotFormer furnace;
	
	public GuiIngotFormer(Container par1Container, TileIngotFormer te) {
		super(par1Container);
		furnace = te;
		xSize = 175;
		ySize = 165;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int i = mc.renderEngine.getTexture("/LM/gfx/LiquidMetal/IngotFormer.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(i);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		drawTexturedModalRect(left+44, top+51, 0, 166, ((furnace.time*92)/furnace.timeReq), 10);
		//*
		if(furnace.input.getLiquid() != null) {
			displayGauge(left, top, 15, 26, (furnace.input.getLiquid().amount*58)/furnace.input.getCapacity(), furnace.input.getLiquid().itemID, furnace.input.getLiquid().itemMeta);
		}
		//*/
		mc.renderEngine.bindTexture(i);
		drawTexturedModalRect(left+26, top+20, 176, 5, 15, 53);
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
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		fontRenderer.drawString(getName(), xSize-80, 10, 0x404040);
	}
	
	protected String getName() {
		return "Ingot Former";
	}
	
}