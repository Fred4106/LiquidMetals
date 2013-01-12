package LiquidMetals.GUI;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import LiquidMetals.Blocks.TileCrafting;
import buildcraft.core.gui.GuiBuildCraft;
import buildcraft.core.utils.StringUtil;
import buildcraft.factory.TileAutoWorkbench;
import buildcraft.factory.gui.ContainerAutoWorkbench;

public class GuiCrafting extends GuiContainer {

	TileCrafting te;
	
	public GuiCrafting(Container par1Container, TileCrafting te) {
		super(par1Container);
		this.te = te;
		xSize = 175;
		ySize = 209;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int i = mc.renderEngine.getTexture("/LiquidMetals/gfx/LiquidMetal/liquidCrafting.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(i);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		
		for(int a = 0; a < 3; a++) {
			if(te.liquids[a].getLiquid() != null) {
				displayGauge(left, top, 16, 116+(a*18), (te.liquids[a].getLiquid().amount*52)/te.liquids[a].getCapacity(), te.liquids[a].getLiquid().itemID, te.liquids[a].getLiquid().itemMeta);
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		fontRenderer.drawString(getName(), 10, 5, 0x404040);
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

			drawTexturedModalRect(j + col, k + line + 52 - x - start, imgColumn * 16, imgLine * 16 + (16 - x), 16, 16 - (16 - x));
			start = start + 16;

			if (x == 0 || squaled == 0)
				break;
		}
	}
	
	private String getName() {
		return "Liquid Crafting Table";
	}

}