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
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import LiquidMetals.CommonProxy;
import LiquidMetals.IngotFormerRecipe;
import LiquidMetals.IngotFormerRecipeManager;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;

public class TileIngotFormer extends TileBuildCraft implements ITankContainer, IInventory, IPeripheral{

	public ItemStack[] inventory = new ItemStack[1];
	public LiquidTank input = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 4);
	
	public static int timeReq = 40;
	public int time = 0;
	
	public boolean hasUpdate = false;
	
	public boolean redstonePowered = false;
	
	//for computercraft control
	public IComputerAccess computer;
	public boolean computerMode = false;
	public boolean enabled = false;
	
	public TileIngotFormer() {
	}
	
	public void checkRedstonePower() {
			redstonePowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 10 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			checkRedstonePower();
			hasUpdate = false;
		}
		if(canCook()) {
			time++;
			if(time >= timeReq) {
				doCook();
				if(CommonProxy.proxy.isSimulating(worldObj)) {
					if(computerMode == true) {
						computer.queueEvent("IngotFormed", new Object[] {this.getOutputName(), this.getOutputAmount()});
					}
				}
				time = 0;
			}
		} else {
			time = 0;
		}
	}
	
	private boolean canCook() {
		if(redstonePowered == true && computerMode == false) {
			return false;
		}
		if(computerMode == true && enabled == false) {
			return false;
		}
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
		if(redstonePowered) {
			values[7] = 1;
		} else {
			values[7] = 0;
		}
		
		if(this.computerMode) {
			values[8] = 1;
		} else {
			values[8] = 0;
		}
		if(this.enabled) {
			values[9] = 1;
		} else {
			values[9] = 0;
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
			input.setLiquid(new LiquidStack(values[3], values[5], values[4]));
		} else {
			input.setLiquid(null);
		}
		time = values[6];
		redstonePowered = false;
		if(values[7] == 1) {
			redstonePowered = true;
		}
		computerMode = false;
		if(values[8] == 1) {
			computerMode = true;
		}
		enabled = false;
		if(values[9] == 1) {
			enabled = true;
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

	@Override
	public String getType() {
		return "Ingot Former";
	}

	public String getLiquidName() {
		if(input.getLiquid() != null) {
			return input.getLiquid().asItemStack().getDisplayName();
		}
		return "null";
	}
	
	public int getLiquidAmount() {
		if(input.getLiquid() != null) {
			return input.getLiquid().amount;
		}
		return 0;
	}
	
	public int getTankCapactiy() {
		return input.getCapacity();
	}
	
	public String getOutputName() {
		if(inventory[0] != null) {
			return inventory[0].getDisplayName();
		}
		return "null";
	}
	
	public int getOutputAmount() {
		if(inventory[0] != null) {
			return inventory[0].copy().stackSize;
		}
		return 0;
	}
	
	public void setEnabled(Object input) {
		this.enabled = (Boolean) input;
	}
	
	public void setComputerMode(Object input, IComputerAccess computer) {
		boolean temp = (Boolean) input;
		if(temp) {
			this.computer = computer;
			this.computerMode = true;
		} else {
			computer = null;
			this.computerMode = false;
		}
	}
	
	@Override
	public String[] getMethodNames() {
		String[] methods = {"getLiquidName", "getLiquidAmount", "getLiquidCapacity", "getOutputName", "getOutputAmount", "setComputerMode", "setEnabled"};
		return methods;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arguments) throws Exception {
		if(method == 0) {
			return new Object[] {getLiquidName()};
		}
		if(method == 1) {
			return new Object[] {getLiquidAmount()};
		}
		if(method == 2) {
			return new Object[] {getTankCapactiy()};
		}
		if(method == 3) {
			return new Object[] {getOutputName()};
		}
		if(method == 4) {
			return new Object[] {getOutputAmount()};
		}
		if(method == 5) {
			setComputerMode(arguments[0], computer);
			return new Object[] {};
		}
		if(method == 6) {
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