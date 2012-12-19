package LM.Blocks;

import LM.CommonProxy;
import LM.GuiHandler;
import LM.LM_Main;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockGrinder1 extends BlockContainer{
	
	public BlockGrinder1(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("SmallGrinder");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getTextureFile() {
		return "/LM/gfx/LiquidMetal/Icons.png";
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		if (entityplayer.isSneaking())
			return false;

		if (!CommonProxy.proxy.isRenderWorld(world)) {
			openGui(world, x, y, z, entityplayer);
			return true;
		}
		
		return true;
	}
	
	public void openGui(World world, int x, int y, int z, EntityPlayer entityplayer) {
		entityplayer.openGui(LM_Main.instance, GuiHandler.Grinder1, world, x, y, z);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileGrinder1();
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return 66;
	}
	
}