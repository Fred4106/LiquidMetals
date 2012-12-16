package LM;

import LM.Blocks.LiquefierTile;
import LM.GUI.LiquefierContainer;
import LM.GUI.LiquefierGui;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int LIQUEFIER = 0;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(!world.blockExists(x,  y, z))
			return null;
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(ID == LIQUEFIER) {
			if(!(tile instanceof LiquefierTile)) {
				return null;
			}
			return new LiquefierGui(new LiquefierContainer(player.inventory, (LiquefierTile) tile), (LiquefierTile) tile);
		}
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(!world.blockExists(x,  y, z))
			return null;
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(ID == LIQUEFIER) {
			if(!(tile instanceof LiquefierTile)) {
				return null;
			}
			return new LiquefierContainer(player.inventory, (LiquefierTile) tile);
		}
		return null;
	}
}
