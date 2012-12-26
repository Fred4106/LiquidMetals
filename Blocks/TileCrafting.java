package LiquidMetals.Blocks;

import LiquidMetals.ArcFurnaceRecipe;
import LiquidMetals.ArcFurnaceRecipeManager;
import LiquidMetals.CommonProxy;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class TileCrafting extends TileBuildCraft implements ITankContainer, IInventory{

	public ItemStack[] inventory = new ItemStack[10];
	public LiquidTank liquidInventory = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 8);
	
	public boolean hasUpdate = false;
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 100 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			hasUpdate = false;
		}
	}
	
	//start to edit
	private int[] getValuesArray() {
		int[] values = new int[33];
		for(int a =  0; a < 30; a+=3)
		{
			if(inventory[a/3] != null) {
				values[a] = inventory[a/3].getItem().shiftedIndex;
				values[a+1] = inventory[a/3].getItemDamage();
				values[a+2] = inventory[a/3].stackSize;
			} else {
				values[a] = 0;
				values[a+1] = 0;
				values[a+2] = 0;
			}
		}
		if(liquidInventory.getLiquid() != null) {
			values[30] = liquidInventory.getLiquid().itemID;
			values[31] = liquidInventory.getLiquid().itemMeta;
			values[32] = liquidInventory.getLiquid().amount;
		} else {
			values[30] = 0;
			values[31] = 0;
			values[32] = 0;
		}
		return values;
	}
	
	private void useValuesArray(int[] values) {
		for(int a = 0; a < 10; a++) {
			if(values[a*3] != 0) {
				inventory[0] = new ItemStack(values[(a*3)], values[(a*3)+2], values[(a*3)+1]);
			}
		}
		if(values[30] != 0) {
			liquidInventory.setLiquid(new LiquidStack(values[30], values[32], values[31]));
		} else {
			liquidInventory.setLiquid(null);
		}
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
	
	@Override
	public void onInventoryChanged() {
		hasUpdate = true;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int slotId, int count) {
		if (inventory[slotId] == null)
			return null;
		if(slotId == 9) {
			for(int a = 0; a < 9; a++) {
				decrStackSize(a, 1*count);
			}
			
		}
		
		if (inventory[slotId].stackSize > count)
			return inventory[slotId].splitStack(count);
		ItemStack stack = inventory[slotId];
		inventory[slotId] = null;
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.inventory[i] == null)
			return null;

		ItemStack stackToTake = this.inventory[i];
		this.inventory[i] = null;
		return stackToTake;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inventory[var1] = var2;
	}

	@Override
	public String getInvName() {
		return "PoweredCrafter";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		hasUpdate = true;
		return liquidInventory.fill(resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		hasUpdate = true;
		return liquidInventory.fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		return liquidInventory.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		return liquidInventory.drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new ILiquidTank[] {liquidInventory};
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return liquidInventory;
	}

}