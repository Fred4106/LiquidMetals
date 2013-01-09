package LiquidMetals.Blocks;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
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

public class TileFurnace extends TileBuildCraft implements ITankContainer, IInventory, IPowerReceptor, IPeripheral{

	public ItemStack[] inventory = new ItemStack[1];
	public LiquidTank output = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 8);
	
	public static int timeReq = 20;
	public static int heatReq = 20;
	
	public int heat = 0;
	public int time = 0;
	
	public int heatMax = 30;
	
	public boolean hasUpdate = false;
	
	protected IPowerProvider powerProvider;
	
	public boolean redstonePowered = false;

	//Computer stuff;
	IComputerAccess computer = null;
	public boolean computerMode = false;
	public boolean enabled = false;
	
	public TileFurnace() {
		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		powerProvider.configure(5, 100, 100, 100, 100);
		
	}
	
	public void checkRedstonePower() {
		redstonePowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 10 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			hasUpdate = false;
			checkRedstonePower();
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
					if(heat > heatMax) {
						heat = heatMax;
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
		if(computerMode)
		{
			if(!enabled)
			{
				return false;
			}
		} else if(this.redstonePowered == true) {
			return false;
		}
		
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
		if(computerMode && CommonProxy.proxy.isSimulating(worldObj)) {
			computer.queueEvent("Metal Melted", new Object[] {this.getLiquidName(), this.getLiquidAmount()});
		}
		hasUpdate = true;
	}
	
	//start to edit
	private int[] getValuesArray() {
		int[] values = new int[10];
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
		values[8] = 0;
		if(this.computerMode) {
			values[8] = 1;
		}
		values[9] = 0;
		if(this.enabled) {
			values[9] = 1;
		}
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
		this.computerMode = false;
		if(values[8] == 1) {
			this.computerMode = true;
		}
		this.enabled = false;
		if(values[9] == 1) {
			this.enabled = true;
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

	@Override
	public String getType() {
		return "Furnace";
	}

	public String getInputName() {
		if(inventory[0] != null) {
			return inventory[0].getItem().getItemDisplayName(inventory[0]);
		}
		return "null";
	}
	
	public int getInputAmount() {
		if(inventory[0] != null) {
			return inventory[0].copy().stackSize;
		}
		return 0;
	}
	
	public String getLiquidName() {
		if(output.getLiquid() != null) {
			return output.getLiquid().asItemStack().getItem().getItemDisplayName(output.getLiquid().asItemStack());
		}
		return "null";
	}
	
	public int getLiquidAmount() {
		if(output.getLiquid() != null) {
			return output.getLiquid().amount;
		}
		return 0;
	}
	
	public int getLiquidCapacity() {
		return output.getCapacity();
	}
	
	public int getHeat() {
		return heat;
	}
	
	public int getReqHeat() {
		return heatReq;
	}
	
	public int getMaxHeat() {
		return heatMax;
	}
	
	public void setComputerMode(Object bool, IComputerAccess computer) {
		boolean temp = (Boolean) bool;
		this.computerMode = temp;
		this.computer = null;
		if(this.computerMode) {
			this.computer = computer;
		}
	}
	
	public void setEnabled(Object bool) {
		boolean temp = (Boolean) bool;
		this.enabled = temp;
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[] {"getInputName", "getInputAmount", "getLiquidName", "getLiquidAmount", "getLiquidCapacity", "getHeat", "getReqHeat", "getMaxHeat", "setComputerMode", "setEnabled"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arguments) throws Exception {
		if(method == 0) {
			return new Object[] {getInputName()};
		}
		if(method == 1) {
			if(inventory[0] != null) {
				return new Object[] {getInputAmount()};
			}
			return new Object[] {0};
		}
		if(method == 2) {
			return new Object[] {getLiquidName()};
		}
		if(method == 3) {
			return new Object[] {getLiquidAmount()};
		}
		if(method == 4) {
			return new Object[] {getLiquidCapacity()};
		}
		if(method == 5) {
			return new Object[] {getHeat()};
		}
		if(method == 6) {
			return new Object[] {getReqHeat()};
		}
		if(method == 7) {
			return new Object[] {getMaxHeat()};
		}
		if(method == 8) {
			setComputerMode(arguments[0], computer);
			return new Object[] {};
		}
		if(method == 9) {
			setEnabled(arguments[0]);
			return new Object[] {};
		}
		return null;
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}

}
