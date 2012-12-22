package LM.Blocks;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import LM.CommonProxy;
import LM.DEFAULT_SETTINGS;
import LM.GrinderRecipe;
import LM.GrinderRecipeManager;
import LM.LM_Main;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.core.IMachine;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;

public class TileGrinder1 extends TileBuildCraft implements IInventory, IPowerReceptor{
	public ItemStack[] inventory = new ItemStack[2];
	public int cookTime = 0;
	public boolean hasUpdate = false;
	
	public static int cookReq = 10;
	
	protected IPowerProvider powerProvider;
	
	public TileGrinder1() {
		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		configPower();
	}
	
	protected void configPower() {
		powerProvider.configure(5, 20, 20, 20, 20);
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 10 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			hasUpdate = false;
		}
		if(canCook()) {
			if(useEnergy())
			{
				cookTime++;
			}
		} else {
			cookTime = 0;
		}
		if (CommonProxy.proxy.isRenderWorld(worldObj)) {
			return;
		}
		if(cookTime >= cookReq) {
			if(canCook()) {
				doCook();
			}
			cookTime = 0;
		}
	}
	
	protected boolean useEnergy() {
		if(powerProvider.useEnergy(20, 20, true) == 20)
		{
			return true;
		}
		return false;
	}
	
	private boolean canCook() {
		GrinderRecipe tempRecipe = GrinderRecipeManager.getRecipe(inventory[0], getTier());
		if(tempRecipe != null) {
			if(inventory[1] == null) {
				return true;
			}
			else {
				if((inventory[1].getItem().shiftedIndex == tempRecipe.getOutput().getItem().shiftedIndex) && (inventory[1].getItemDamage() == tempRecipe.getOutput().getItemDamage()) && (64-inventory[1].stackSize >= tempRecipe.getOutput().stackSize)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void ajustInput() {
		GrinderRecipe tempRecipe = GrinderRecipeManager.getRecipe(inventory[0], getTier());
		if(inventory[0].stackSize > tempRecipe.getInput().stackSize) {
			inventory[0].stackSize-=2;
			return;
		} else {
			inventory[0] = null;
			return;
		}
	}
	
	protected int getTier() {
		return 1;
	}
	
	private void doCook() {
		GrinderRecipe tempRecipe = GrinderRecipeManager.getRecipe(inventory[0], getTier());
		if(tempRecipe != null) {
			if(inventory[1] == null) {
				inventory[1] = tempRecipe.getOutput();
			}
			else {
				if((inventory[1].getItem().shiftedIndex == tempRecipe.getOutput().getItem().shiftedIndex) && (inventory[1].getItemDamage() == tempRecipe.getOutput().getItemDamage()) && (64-inventory[1].stackSize >= tempRecipe.getOutput().stackSize)) {
					inventory[1].stackSize+=tempRecipe.getOutput().stackSize;
				}
			}
			ajustInput();
			hasUpdate = true;
		}
	}
	
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
		if(inventory[1] != null) {
			values[3] = inventory[1].itemID;
			values[4] = inventory[1].getItemDamage();
			values[5] = inventory[1].stackSize;
		} else {
			values[3] = 0;
			values[4] = 0;
			values[5] = 0;
		}
		values[6] = cookTime;
		return values;
	}
	
	private void useValuesArray(int[] values) {
		if(values[0] != 0) {
			inventory[0] = new ItemStack(values[0], values[2], values[1]);
		} else {
			inventory[0] = null;
		}
		if(values[3] != 0) {
			inventory[1] = new ItemStack(values[3], values[5], values[4]);
		} else {
			inventory[1] = null;
		}
		cookTime = values[6];
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
	
	//required stuff
	@Override
	public void onInventoryChanged() {
		hasUpdate = true;
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inventory[var1];
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
		return "Grinder 1";
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
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() { }

	@Override
	public int powerRequest() {
		return (int) Math.ceil(Math.min(getPowerProvider().getMaxEnergyReceived(), getPowerProvider().getMaxEnergyStored() - getPowerProvider().getEnergyStored()));
	}
}
