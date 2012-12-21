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
import LM.GrinderRecipe;
import LM.GrinderRecipeManager;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;

public class TileFurnace extends TileBuildCraft implements ITankContainer, IInventory, IPowerReceptor{

	public ItemStack[] inventory = new ItemStack[1];
	public LiquidTank output = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 8);
	
	public static int timeReq = 20;
	public static int heatReq = 20;
	
	public int heat = 0;
	public int time = 0;
	
	public boolean hasUpdate = false;
	
	protected IPowerProvider powerProvider;
	
	public TileFurnace() {
		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		powerProvider.configure(5, 100, 100, 100, 100);
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 10 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			hasUpdate = false;
		}
		ajustHeat();
		if(canCook()) {
			time++;
		} else {
			time = 0;
		}
		if(time >= timeReq && CommonProxy.proxy.isSimulating(worldObj)) {
			if(canCook()) {
				doCook();
				time = 0;
				hasUpdate = true;
			}
		}
	}
	
	private void ajustHeat() {
		if(CommonProxy.proxy.isSimulating(worldObj)) {
			if(worldObj.getWorldTime() % 50 == 0) {
				if(useEnergy()) {
					heat++;
					if(heat > 30) {
						heat = 30;
					}
				} else {
					heat--;
					if(heat < 0) {
						heat = 0;
					}
				}
				hasUpdate = true;
			}
		}
	}
	
	protected boolean useEnergy() {
		if(powerProvider.useEnergy(100, 100, true) == 100)
		{
			return true;
		}
		return false;
	}
	
	private boolean canCook() {
		ArcFurnaceRecipe temp = ArcFurnaceRecipeManager.getRecipe(inventory[0]);
		if(heat >= heatReq) {
			if(temp != null) {
				if(output.getLiquid()!= null) {
					if(temp.getOutput().itemID == output.getLiquid().itemID && temp.getOutput().itemMeta == output.getLiquid().itemMeta) {
						if(output.getCapacity() >= temp.getOutput().amount + output.getLiquid().amount) {
							return true;
						}
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	private void ajustInput() {
		ArcFurnaceRecipe temp = ArcFurnaceRecipeManager.getRecipe(inventory[0]);
		if(temp != null) {
			inventory[0].stackSize-=temp.getInput().stackSize;
		}
		if(inventory[0].stackSize <= 0) {
			inventory[0] = null;
		}
	}
	
	private void doCook() {
		ArcFurnaceRecipe temp = ArcFurnaceRecipeManager.getRecipe(inventory[0]);
		if(temp != null) {
			ajustInput();
			output.fill(temp.getOutput(), true);
		}
		hasUpdate = true;
	}
	
	//start to edit
	private int[] getValuesArray() {
		int[] values = new int[8];
		if(inventory[0] != null) {
			values[0] = inventory[0].itemID;
			values[1] = inventory[0].getItemDamage();
			values[2] = inventory[0].stackSize;
		} else {
			values[0] = 0;
			values[1] = 0;
			values[2] = 0;
		}
		if(output.getLiquid() != null) {
			values[3] = output.getLiquid().itemID;
			values[4] = output.getLiquid().itemMeta;
			values[5] = output.getLiquid().amount;
		} else {
			values[3] = 0;
			values[4] = 0;
			values[5] = 0;
		}
		values[6] = time;
		values[7] = heat;
		return values;
	}
	
	private void useValuesArray(int[] values) {
		if(values[0] != 0) {
			inventory[0] = new ItemStack(values[0], values[2], values[1]);
		} else {
			inventory[0] = null;
		}
		if(values[3] != 0) {
			output.setLiquid(new LiquidStack(values[3], values[5], values[4]));
		} else {
			output.setLiquid(null);
		}
		time = values[6];
		heat = values[7];
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
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return this.powerProvider;
	}

	@Override
	public void doWork() { }

	@Override
	public int powerRequest() {
		return (int) Math.ceil(Math.min(getPowerProvider().getMaxEnergyReceived(), getPowerProvider().getMaxEnergyStored() - getPowerProvider().getEnergyStored()));
	}
	
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
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		return output.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		hasUpdate = true;
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

}
