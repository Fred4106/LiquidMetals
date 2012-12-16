package LM.Blocks;

import buildcraft.BuildCraftCore;
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import LM.CommonProxy;

public class LiquefierTile extends TileBuildCraft implements ITankContainer, IInventory{

	public final LiquidTank output = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 32);
	public ItemStack[] input = new ItemStack[4];
	
	public int cookTime = 0;
	public boolean cooking = false;
	
	private static int finishLimit = 1000;
	private static int coolDownLimit = finishLimit/10;
	
	boolean hasUpdate = false;
	public SafeTimeTracker tracker = new SafeTimeTracker();
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && hasUpdate && tracker.markTimeIfDelay(worldObj, 2 * BuildCraftCore.updateFactor)) {
			sendNetworkUpdate();
			hasUpdate = false;
		}

		if(CommonProxy.proxy.isSimulating(worldObj)) {
			if(input[0] != null) {
				input[0].stackSize--;
				hasUpdate = true;
			}
		}
		
		if (CommonProxy.proxy.isRenderWorld(worldObj)) {
			return;
		}
	}
	
	/* NETWORK */
	@Override
	public PacketPayload getPacketPayload() {
		PacketPayload payload = new PacketPayload(17, 0, 0);
		if (output.getLiquid() != null) {
			payload.intPayload[0] = output.getLiquid().itemID;
			payload.intPayload[1] = output.getLiquid().itemMeta;
			payload.intPayload[2] = output.getLiquid().amount;
		} else {
			payload.intPayload[0] = 0;
			payload.intPayload[1] = 0;
			payload.intPayload[2] = 0;
		}
		if(input[0] != null) {
			payload.intPayload[3] = input[0].getItem().shiftedIndex;
			payload.intPayload[4] = input[0].getItemDamage();
			payload.intPayload[5] = input[0].stackSize;
		}
		else {
			payload.intPayload[3] = 0;
			payload.intPayload[4] = 0;
			payload.intPayload[5] = 0;
		}
		if(input[1] != null) {
			payload.intPayload[6] = input[1].getItem().shiftedIndex;
			payload.intPayload[7] = input[1].getItemDamage();
			payload.intPayload[8] = input[1].stackSize;
		}
		else {
			payload.intPayload[6] = 0;
			payload.intPayload[7] = 0;
			payload.intPayload[8] = 0;
		}
		if(input[2] != null) {
			payload.intPayload[9] = input[2].getItem().shiftedIndex;
			payload.intPayload[10] = input[2].getItemDamage();
			payload.intPayload[11] = input[2].stackSize;
		}
		else {
			payload.intPayload[9] = 0;
			payload.intPayload[10] = 0;
			payload.intPayload[11] = 0;
		}
		if(input[3] != null) {
			payload.intPayload[12] = input[3].getItem().shiftedIndex;
			payload.intPayload[13] = input[3].getItemDamage();
			payload.intPayload[14] = input[3].stackSize;
		}
		else {
			payload.intPayload[12] = 0;
			payload.intPayload[13] = 0;
			payload.intPayload[14] = 0;
		}
		payload.intPayload[15] = cookTime;
		if(cooking) {
			payload.intPayload[16] = 1;
		}
		else {
			payload.intPayload[16] = 0;
		}
		return payload;
	}
	
	@Override
	public void handleUpdatePacket(PacketUpdate packet) {
		if (packet.payload.intPayload[0] > 0) {
			LiquidStack liquid = new LiquidStack(packet.payload.intPayload[0], packet.payload.intPayload[2], packet.payload.intPayload[1]);
			output.setLiquid(liquid);
		} else {
			output.setLiquid(null);
		}
		input[0] = new ItemStack(packet.payload.intPayload[3], packet.payload.intPayload[5], packet.payload.intPayload[4]);
		input[1] = new ItemStack(packet.payload.intPayload[6], packet.payload.intPayload[8], packet.payload.intPayload[7]);
		input[2] = new ItemStack(packet.payload.intPayload[9], packet.payload.intPayload[11], packet.payload.intPayload[10]);
		input[3] = new ItemStack(packet.payload.intPayload[12], packet.payload.intPayload[14], packet.payload.intPayload[13]);
		cookTime = packet.payload.intPayload[15];
		cooking = false;
		if(packet.payload.intPayload[16] == 1) {
			cooking = true;
		}
	}
	
	/* SAVING & LOADING */
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		if (data.hasKey("stored") && data.hasKey("liquidId")) {
			LiquidStack liquid = new LiquidStack(data.getInteger("liquidId"), data.getInteger("stored"), 0);
			output.setLiquid(liquid);
		} else {
			LiquidStack liquid = new LiquidStack(0, 0, 0);
			liquid.readFromNBT(data.getCompoundTag("Liquefier"));
			output.setLiquid(liquid);
		}
		if(data.getInteger("id0") != 0) {
			input[0] = new ItemStack(data.getInteger("id0"), data.getInteger("amount0"), data.getInteger("damage0"));
		}
		else {
			input[0] = null;
		}
		if(data.getInteger("id1") != 0) {
			input[1] = new ItemStack(data.getInteger("id1"), data.getInteger("amount1"), data.getInteger("damage1"));
		}
		else {
			input[1] = null;
		}
		if(data.getInteger("id2") != 0) {
			input[2] = new ItemStack(data.getInteger("id2"), data.getInteger("amount2"), data.getInteger("damage2"));
		}
		else {
			input[2] = null;
		}
		if(data.getInteger("id3") != 0) {
			input[3] = new ItemStack(data.getInteger("id3"), data.getInteger("amount3"), data.getInteger("damage3"));
		}
		else {
			input[3] = null;
		}
		cookTime = data.getInteger("cookTime");
		cooking = data.getBoolean("cooking");
	}

	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		if (output.getLiquid() != null) {
			data.setTag("Liquefier", output.getLiquid().writeToNBT(new NBTTagCompound()));
		}
		if(input[0] != null) {
			data.setInteger("id0", input[0].getItem().shiftedIndex);
			data.setInteger("amount0", input[0].stackSize);
			data.setInteger("damage0", input[0].getItemDamage());
		} else {
			data.setInteger("id0", 0);
			data.setInteger("amount0", 0);
			data.setInteger("damage0", 0);
		}
		if(input[1] != null) {
			data.setInteger("id1", input[1].getItem().shiftedIndex);
			data.setInteger("amount1", input[1].stackSize);
			data.setInteger("damage1", input[1].getItemDamage());
		} else {
			data.setInteger("id1", 0);
			data.setInteger("amount1", 0);
			data.setInteger("damage1", 0);
		}
		if(input[2] != null) {
			data.setInteger("id2", input[2].getItem().shiftedIndex);
			data.setInteger("amount2", input[2].stackSize);
			data.setInteger("damage2", input[2].getItemDamage());
		} else {
			data.setInteger("id2", 0);
			data.setInteger("amount2", 0);
			data.setInteger("damage2", 0);
		}
		if(input[3] != null) {
			data.setInteger("id3", input[3].getItem().shiftedIndex);
			data.setInteger("amount3", input[3].stackSize);
			data.setInteger("damage3", input[3].getItemDamage());
		} else {
			data.setInteger("id3", 0);
			data.setInteger("amount3", 0);
			data.setInteger("damage3", 0);
		}
		data.setInteger("cookTime", cookTime);
		data.setBoolean("cooking", cooking);
	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return output.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return output.drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new ILiquidTank[] {output};
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return output;
	}

	@Override
	public int getSizeInventory() {
		return input.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return input[var1];
	}

	@Override
	public ItemStack decrStackSize(int slotId, int count) {
		if (input[slotId] == null)
			return null;
		if (input[slotId].stackSize > count)
			return input[slotId].splitStack(count);
		ItemStack stack = input[slotId];
		input[slotId] = null;
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.input[i] == null)
			return null;

		ItemStack stackToTake = this.input[i];
		this.input[i] = null;
		return stackToTake;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		input[var1] = var2;
		
	}

	@Override
	public String getInvName() {
		return "Liquefier";
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
		
	}

	@Override
	public void closeChest() {
		
	}
}
