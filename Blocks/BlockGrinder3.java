package LiquidMetals.Blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import LiquidMetals.GuiHandler;
import LiquidMetals.LM_Main;

public class BlockGrinder3 extends BlockGrinder1 {
	
	public BlockGrinder3(int par1) {
		super(par1);
		this.setBlockName("LargeGrinder");
		textureOffset = 4;
	}
	
	public void openGui(World world, int x, int y, int z, EntityPlayer entityplayer) {
		entityplayer.openGui(LM_Main.instance, GuiHandler.Grinder3, world, x, y, z);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileGrinder3();
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return 68;
	}
	
}