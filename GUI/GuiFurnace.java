package LiquidMetals.GUI;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import LiquidMetals.Blocks.TileFurnace;

public class GuiFurnace extends GuiContainer {
	
	TileFurnace furnace;
	
	public GuiFurnace(Container par1Container, TileFurnace te) {
		super(par1Container);
		furnace = te;
		xSize = 175;
		ySize = 165;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int i = mc.renderEngine.getTexture("/LiquidMetals/gfx/LiquidMetal/Arcfurnace.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(i);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		drawTexturedModalRect(left+44, top+57, 0, 176, ((furnace.heat*88)/30), 10);
		drawTexturedModalRect(left+44, top+46, 0, 166, ((furnace.time*88)/furnace.timeReq), 10);
		if(furnace.output.getLiquid() != null) {
			displayGauge(left, top, 15, 134, (furnace.output.getLiquid().amount*58)/furnace.output.getCapacity(), furnace.output.getLiquid().itemID, furnace.output.getLiquid().itemMeta);
		}
		mc.renderEngine.bindTexture(i);
		drawTexturedModalRect(left+134, top+20, 176, 5, 15, 53);
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
		fontRenderer.drawString(getName(), 10, 10, 0x404040);
	}
	
	protected String getName() {
		return "Arc Furnace";
	}

}
