package LiquidMetals.Blocks;

import java.util.ArrayList;

import buildcraft.api.tools.IToolWrench;
import buildcraft.core.BlockBuildCraft;
import buildcraft.core.IItemPipe;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import LiquidMetals.CommonProxy;
import LiquidMetals.GuiHandler;
import LiquidMetals.LM_Main;

public class BlockCraftingTable extends BlockContainer{

	private int topTexture = 165;
	private int sideTexture = 165+16;
	private int bottomTexture = 165+16;
	
	public BlockCraftingTable(int id) {
		super(id, Material.iron);
		setHardness(5F);
		this.setBlockName("liquidCrafting");
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public String getTextureFile() {
		return "/LiquidMetals/gfx/LiquidMetal/Icons.png";
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		TileCrafting tile = (TileCrafting) world.getBlockTileEntity(i, j, k);

		if (tile != null) {
			//tile.checkRedstonePower();
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		if (entityplayer.isSneaking())
			return false;

		if (!CommonProxy.proxy.isRenderWorld(world)) {
			entityplayer.openGui(LM_Main.instance, GuiHandler.Crafting, world, x, y, z);
			return true;
		}
		
		return true;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		if(side == 0) {
			return bottomTexture;
		}
		if(side == 1) {
			return topTexture;
		}
		return sideTexture;
    }
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileCrafting();
	}
	
}