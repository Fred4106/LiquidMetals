package LM;

import LM.Blocks.*;
import LM.GUI.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int Grinder1 = 0;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(!world.blockExists(x,  y, z))
			return null;
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(ID == Grinder1) {
			if(!(tile instanceof TileGrinder1)) {
				return null;
			}
			return new GuiGrinder1(new ContainerGrinder1(player.inventory, (TileGrinder1) tile), (TileGrinder1) tile);
		}
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(!world.blockExists(x,  y, z))
			return null;
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(ID == Grinder1) {
			if(!(tile instanceof TileGrinder1)) {
				return null;
			}
			return new ContainerGrinder1(player.inventory, (TileGrinder1) tile);
		}
		return null;
	}
}
