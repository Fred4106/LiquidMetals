package LM.Blocks;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import LM.CommonProxy;
import LM.GuiHandler;
import LM.LM_Main;

public class BlockIngotFormer extends BlockContainer{

	public BlockIngotFormer(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("ingotFormer");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getTextureFile() {
		return "/LM/gfx/LiquidMetal/icons.png";
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		return 69;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		if (entityplayer.isSneaking())
			return false;

		if (!CommonProxy.proxy.isRenderWorld(world)) {
			entityplayer.openGui(LM_Main.instance, GuiHandler.IngotFormer, world, x, y, z);
			return true;
		}
		
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileFurnace();
	}

}