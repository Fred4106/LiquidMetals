package LM.Blocks;

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
import LM.ArcFurnaceRecipe;
import LM.ArcFurnaceRecipeManager;
import LM.CommonProxy;
import LM.IngotFormerRecipe;
import LM.IngotFormerRecipeManager;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;

public class TileIngotFormer extends TileBuildCraft implements ITankContainer, IInventory{

	public ItemStack[] inventory = new ItemStack[1];
	public LiquidTank input = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 4);
	
	public static int timeReq = 40;
	public int time = 0;
	
	public boolean hasUpdate = false;
	
	public TileIngotFormer() {
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 10 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			hasUpdate = false;
		}
		if(canCook()) {
			time++;
			if(time >= timeReq) {
				doCook();
				time = 0;
			}
		} else {
			time = 0;
		}
	}
	
	private boolean canCook() {
		if(input.getLiquid() == null) {
			return false;
		}
		IngotFormerRecipe temp = IngotFormerRecipeManager.getRecipe(input.getLiquid());
		if(temp != null) {
			if(input.getLiquid().isLiquidEqual(temp.getInput())) {
				if(input.getLiquid().amount >= temp.getInput().amount) {
					if(inventory[0] == null) {
						return true;
					}
					if(inventory[0].isItemEqual(temp.getOutput()))
					{
						if(inventory[0].stackSize + temp.getOutput().stackSize <= 64) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private void doCook() {
		IngotFormerRecipe temp = IngotFormerRecipeManager.getRecipe(input.getLiquid());
		if(canCook()) {
			input.drain(temp.getInput().amount, true);
			if(input.getLiquid() != null) {
				if(input.getLiquid().amount <= 0) {
					input = null;
				}
			}
			if(inventory[0] == null) {
				inventory[0] = temp.getOutput();
			} else {
				inventory[0].stackSize += temp.getOutput().stackSize;
			}
			hasUpdate = true;
		}
	}
	
	//start to edit
	private int[] getValuesArray() {
		int[] values = new int[7];
		if(inventory[0] != null) {
			values[0] = inventory[0].itemID;
			values[1] = inventory[0].getItemDamage();
			values[2] = inventory[0].stackSize;
		} else {
			values[0] = 0;
			values[1] = 0;
			values[2] = 0;
		}
		if(input.getLiquid() != null) {
			values[3] = input.getLiquid().itemID;
			values[4] = input.getLiquid().itemMeta;
			values[5] = input.getLiquid().amount;
		} else {
			values[3] = 0;
			values[4] = 0;
			values[5] = 0;
		}
		values[6] = time;
		return values;
	}
	
	private void useValuesArray(int[] values) {
		if(values[0] != 0) {
			inventory[0] = new ItemStack(values[0], values[2], values[1]);
		} else {
			inventory[0] = null;
		}
		if(values[3] != 0) {
			input.setLiquid(new LiquidStack(values[3], values[5], values[4]));
		} else {
			input.setLiquid(null);
		}
		time = values[6];
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
		return "furnace";
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
		return input.fill(resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		hasUpdate = true;
		return input.fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		return input.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		return input.drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new ILiquidTank[] {input};
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return input;
	}

}