package LiquidMetals.Blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import LiquidMetals.CommonProxy;
import LiquidMetals.GuiHandler;
import LiquidMetals.LM_Main;
import buildcraft.api.core.Position;
import buildcraft.api.tools.IToolWrench;
import buildcraft.core.utils.Utils;

public class DEBUGLiquidGen extends BlockContainer{

	private Random furnaceRandom = new Random();
	private int textureOffset = 1;
	
	public DEBUGLiquidGen(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("DEBUGLiquidGen");
		setCreativeTab(LM_Main.tabBlocks);
	}

	@Override
	public String getTextureFile() {
		return "/LiquidMetals/gfx/LiquidMetal/Icons.png";
	}
	
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		if(side == meta) {
			return 243;
		}
		return 244;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		if (entityplayer.isSneaking())
			return false;

		Item equipped = entityplayer.getCurrentEquippedItem() != null ? entityplayer.getCurrentEquippedItem().getItem() : null;
		if (equipped instanceof IToolWrench && ((IToolWrench) equipped).canWrench(entityplayer, x, y, z)) {

			world.setBlockMetadata(x, y, z, (world.getBlockMetadata(x, y, z)+1)%6);
			((IToolWrench) equipped).wrenchUsed(entityplayer, x, y, z);
			return true;
		}
		
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new DEBUGLiquidGenTile();
	}
	//*/
}
