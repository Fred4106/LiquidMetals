package LM;

import LM.Blocks.*; 
import LM.GUI.*;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int Grinder1 = 0;
	public static final int Grinder2 = 1;
	public static final int Grinder3 = 2;
	
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
		} else if(ID == Grinder2) {
			if(!(tile instanceof TileGrinder2)) {
				return null;
			}
			return new GuiGrinder2(new ContainerGrinder2(player.inventory, (TileGrinder2) tile), (TileGrinder2) tile);
		} else if(ID == Grinder3) {
			if(!(tile instanceof TileGrinder3)) {
				return null;
			}
			return new GuiGrinder3(new ContainerGrinder3(player.inventory, (TileGrinder3) tile), (TileGrinder3) tile);
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
		} else if(ID == Grinder2) {
			if(!(tile instanceof TileGrinder2)) {
				return null;
			}
			return new ContainerGrinder2(player.inventory, (TileGrinder2) tile);
		}  else if(ID == Grinder3) {
			if(!(tile instanceof TileGrinder3)) {
				return null;
			}
			return new ContainerGrinder3(player.inventory, (TileGrinder3) tile);
		}
		return null;
	}
}
