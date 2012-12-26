package LiquidMetals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import LiquidMetals.Blocks.TileFurnace;
import LiquidMetals.Blocks.TileGrinder1;
import LiquidMetals.Blocks.TileGrinder2;
import LiquidMetals.Blocks.TileGrinder3;
import LiquidMetals.Blocks.TileIngotFormer;
import LiquidMetals.GUI.ContainerFurnace;
import LiquidMetals.GUI.ContainerGrinder1;
import LiquidMetals.GUI.ContainerGrinder2;
import LiquidMetals.GUI.ContainerGrinder3;
import LiquidMetals.GUI.ContainerIngotFormer;
import LiquidMetals.GUI.GuiFurnace;
import LiquidMetals.GUI.GuiGrinder1;
import LiquidMetals.GUI.GuiGrinder2;
import LiquidMetals.GUI.GuiGrinder3;
import LiquidMetals.GUI.GuiIngotFormer;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int Grinder1 = 0;
	public static final int Grinder2 = 1;
	public static final int Grinder3 = 2;
	public static final int Furnace = 3;
	public static final int IngotFormer = 4;
	public static final int Crafting = 5;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("opens now.");
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
		} else if(ID == Furnace) {
			if(!(tile instanceof TileFurnace)) {
				return null;
			}
			return new GuiFurnace(new ContainerFurnace(player.inventory, (TileFurnace) tile), (TileFurnace) tile);
		} else if(ID == IngotFormer) {
			if(!(tile instanceof TileIngotFormer)) {
				return null;
			}
			return new GuiIngotFormer(new ContainerIngotFormer(player.inventory, (TileIngotFormer) tile), (TileIngotFormer) tile);
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
		}  else if(ID == Furnace) {
			if(!(tile instanceof TileFurnace)) {
				return null;
			}
			return new ContainerFurnace(player.inventory, (TileFurnace) tile);
		}  else if(ID == IngotFormer) {
			if(!(tile instanceof TileIngotFormer)) {
				return null;
			}
			return new ContainerIngotFormer(player.inventory, (TileIngotFormer) tile);
		}
		return null;
	}
}
