package LiquidMetals.Blocks;

import java.util.ArrayList;

import buildcraft.core.BlockBuildCraft;
import buildcraft.core.IItemPipe;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import LiquidMetals.CommonProxy;
import LiquidMetals.GuiHandler;
import LiquidMetals.LM_Main;

public class BlockCraftingTable extends BlockBuildCraft {

	int topTexture;
	int sideTexture;

	public BlockCraftingTable(int i) {
		super(i, Material.wood);
		topTexture = 2 * 16 + 11;
		sideTexture = 2 * 16 + 12;
		setHardness(1.0F);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public int getBlockTextureFromSide(int i) {
		if (i == 1 || i == 0)
			return topTexture;
		else
			return sideTexture;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, i, j, k, entityplayer, par6, par7, par8, par9);

		// Drop through if the player is sneaking
		if (entityplayer.isSneaking())
			return false;

		if (entityplayer.getCurrentEquippedItem() != null) {
			if (entityplayer.getCurrentEquippedItem().getItem() instanceof IItemPipe)
				return false;
		}

		if (!CommonProxy.proxy.isRenderWorld(world)) {
			entityplayer.openGui(LM_Main.instance, GuiHandler.Crafting, world, i, j, k);
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileCrafting();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}
}