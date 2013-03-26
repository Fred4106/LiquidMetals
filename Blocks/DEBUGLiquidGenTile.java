package LiquidMetals.Blocks;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import LiquidMetals.ArcFurnaceRecipe;
import LiquidMetals.ArcFurnaceRecipeManager;
import LiquidMetals.CommonProxy;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;

public class DEBUGLiquidGenTile extends TileBuildCraft{
	
	LiquidStack toUse = LiquidDictionary.getLiquid("Molten Iron", 500);
	
	public DEBUGLiquidGenTile() {
		
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		int side = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int x = xCoord;
		int z = zCoord;
		int y = yCoord;
		
		if(side == 0) {
			y--;
		} else if(side == 1) {
			y++;
		} else if(side == 2) {
			z--;
		} else if(side == 3) {
			z++;
		} else if(side == 4) {
			x--;
		} else if(side == 5) {
			x++;
		} 
		
		TileEntity tile1 = worldObj.getBlockTileEntity(x, y, z);
		if(tile1 != null) {
			if(tile1 instanceof ITankContainer) {
				((ITankContainer) tile1).fill(0, toUse, true);
			}
		}
	}
	
	//start to edit
	private int[] getValuesArray() {
		return new int[] { };
	}
	
	private void useValuesArray(int[] values) {
		
	}
	
	@Override
	public PacketPayload getPacketPayload() {
		int[] intpayload = getValuesArray();
		PacketPayload payload = new PacketPayload(intpayload.length, 0, 0);
		for(int a = 0; a < intpayload.length; a++) {
			payload.intPayload[a] = intpayload[a];
		}
		return payload;
	}
	
	@Override
	public void handleUpdatePacket(PacketUpdate packet) {
		useValuesArray(packet.payload.intPayload);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		useValuesArray(data.getIntArray("Values"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		data.setIntArray("Values", getValuesArray());
	}
	//end to edit

}
