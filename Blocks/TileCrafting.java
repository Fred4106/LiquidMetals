package LiquidMetals.Blocks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import buildcraft.api.core.Position;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.core.TileBuildCraft;
import buildcraft.core.inventory.TransactorRoundRobin;
import buildcraft.core.network.PacketPayload;
import buildcraft.core.network.PacketUpdate;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.core.utils.Utils;

import LiquidMetals.ArcFurnaceRecipe;
import LiquidMetals.ArcFurnaceRecipeManager;
import LiquidMetals.CommonProxy;
import LiquidMetals.DEFAULT_SETTINGS;
import LiquidMetals.LM_Main;
import LiquidMetals.Metal;
import LiquidMetals.GUI.ContainerCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class TileCrafting extends TileBuildCraft implements IInventory, ITankContainer, ISidedInventory{

	public ItemStack[] inventory = new ItemStack[27];
	public LiquidTank[] liquids = new LiquidTank[3];
	public boolean hasUpdate = false;
	
	public InventoryCrafting craftMatrix;
	
	public TileCrafting() {
		for(int a = 0; a < 3; a++) {
			liquids[a] = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 2);
		}
		craftMatrix = new InventoryCrafting(new ContainerCrafting.ContainerNull(), 3, 3);
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 40 == 0 || hasUpdate)) {
			sendNetworkUpdate();
			hasUpdate = false;
			getMasterListLowerInv();
		}
	}
	
	private boolean canCraft() {
		if(hasEnoughLiquid() && getCraftingResult() != null) {
			//create masterlist of needed items. Scan inventory for required items.
		}
		return false;
	}
	
	private ItemStack[] getMasterListLowerInv() {
		ArrayList<ItemStack> masterList = new ArrayList<ItemStack>();
		for(int a = 9; a < 27; a++) {
			if(inventory[a] != null) {
				if(masterList.size() == 0) {
					masterList.add(inventory[a].copy());
				} else {
					for(int b = 0; b < masterList.size(); b++) {
						if(masterList.get(b).isItemEqual(inventory[a])) {
							masterList.get(b).stackSize+=inventory[a].copy().stackSize;
							break;
						}
						if(b == masterList.size()-1) {
							masterList.add(inventory[a].copy());
							break;
						}
					}
				}
			}
		}
		
		for(int a = 0; a < masterList.size(); a++) {
			if(masterList.get(a).itemID == LM_Main.marker.itemID) {
				masterList.remove(a);
			}
		}
		
		ItemStack[] items = new ItemStack[masterList.size()];
		for(int a = 0; a < masterList.size(); a++) {
			items[a] = masterList.get(a).copy();
			System.out.println(items[a]);
		}
		System.out.println(" ");
		return items;
	}
	
	private boolean invContainsEnoughItems(ItemStack[] masterList) {
		
		
		return false;
	}
	
	private ItemStack[] getMasterList() {
		ArrayList<ItemStack> masterList = new ArrayList<ItemStack>();
		for(int a = 0; a < 9; a++) {
			if(inventory[a] != null) {
				if(masterList.size() == 0) {
					masterList.add(inventory[a].copy());
				} else {
					for(int b = 0; b < masterList.size(); b++) {
						if(masterList.get(b).isItemEqual(inventory[a])) {
							masterList.get(b).stackSize++;
							break;
						}
						if(b == masterList.size()-1) {
							masterList.add(inventory[a].copy());
							break;
						}
					}
				}
			}
		}
		
		for(int a = 0; a < masterList.size(); a++) {
			if(masterList.get(a).itemID == LM_Main.marker.itemID) {
				masterList.remove(a);
			}
		}
		
		ItemStack[] items = new ItemStack[masterList.size()];
		for(int a = 0; a < masterList.size(); a++) {
			items[a] = masterList.get(a).copy();
		}
		return items;
	}
	
	private boolean hasEnoughLiquid() {
		int[] markers = {0, 0, 0};
		int[] liquidAmounts = {0, 0, 0};
		
		for(int a = 0; a < 9; a++) {
			if(inventory[a] != null) {
				if(inventory[a].isItemEqual(new ItemStack(LM_Main.marker, 1, 0))) {
					markers[0]++;
				}
				if(inventory[a].isItemEqual(new ItemStack(LM_Main.marker, 1, 1))) {
					markers[1]++;
				}
				if(inventory[a].isItemEqual(new ItemStack(LM_Main.marker, 1, 2))) {
					markers[2]++;
				}
			}
		}
		
		for(int a = 0; a < liquids.length; a++) {
			if(liquids[a].getLiquid() != null) {
				if(liquids[a].getLiquid().itemID == LM_Main.molten.itemID) {
					liquidAmounts[a] = DEFAULT_SETTINGS.liquidNames.get(liquids[a].getLiquid().itemMeta).getAmount()*markers[a];
				} else {
					if(markers[a] > 0) {
						return false;
					}
				}
			} else {
				if(markers[a] > 0) {
					return false;
				}
			}
		}
		
		for(int a = 0; a < 3; a++) {
			if(liquidAmounts[a] > 0) {
				if(liquids[a].getLiquid().amount < liquidAmounts [a]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private ItemStack getCraftingResult() {
		setCraftMatrix();
		return CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj);
	}
	
	private void setCraftMatrix() {
		ItemStack red = null;
		ItemStack green = null;
		ItemStack blue = null;
		if(liquids[0].getLiquid() != null) {
			red = DEFAULT_SETTINGS.liquidNames.get(liquids[0].getLiquid().itemMeta).getItem();
		}
		if(liquids[1].getLiquid() != null) {
			green = DEFAULT_SETTINGS.liquidNames.get(liquids[1].getLiquid().itemMeta).getItem();
		}
		if(liquids[2].getLiquid() != null) {
			blue = DEFAULT_SETTINGS.liquidNames.get(liquids[2].getLiquid().itemMeta).getItem();
		}
		for(int a = 0; a < 9; a++) {
			if(inventory[a] != null) {
				if(inventory[a].isItemEqual(new ItemStack(LM_Main.marker, 1, 0)) && red != null) {
					craftMatrix.setInventorySlotContents(a, red.copy());//red
				} else if(inventory[a].isItemEqual(new ItemStack(LM_Main.marker, 1, 1)) && green != null) {
					craftMatrix.setInventorySlotContents(a, green.copy());//green
				} else if(inventory[a].isItemEqual(new ItemStack(LM_Main.marker, 1, 2)) && blue != null) {
					craftMatrix.setInventorySlotContents(a, blue.copy());//blue
				} else {
					craftMatrix.setInventorySlotContents(a, inventory[a]);
				}
			}
			else {
				craftMatrix.setInventorySlotContents(a, null);
			}
		}
	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		hasUpdate = true;
		for(int a = 0; a < 3; a++) {
			if(liquids[a].getLiquid() != null) {
				if(liquids[a].getLiquid().asItemStack().isItemEqual(resource.asItemStack())) {
					return liquids[a].fill(resource, doFill);
				}
			}
		}
		for(int a = 0; a < 3; a++) {
			int temp = liquids[a].fill(resource, doFill);
			if(temp > 0) {
				return temp;
			}
		}
		return 0;
	}

	private int[] getValuesArray() {
		int[] values = new int[90];
		for(int a = 0; a < 81; a+=3) {
			if(inventory[a/3] != null) {
				values[a] = inventory[a/3].itemID;
				values[a+1] = inventory[a/3].getItemDamage();
				values[a+2] = inventory[a/3].copy().stackSize;
			} else {
				values[a] = 0;
				values[a+1] = 0;
				values[a+2] = 0;
			}
		}
		if(liquids[0].getLiquid() != null) {
			values[81] = liquids[0].getLiquid().itemID;
			values[82] = liquids[0].getLiquid().itemMeta;
			values[83] = liquids[0].getLiquid().amount;
		} else {
			values[81] = 0;
			values[82] = 0;
			values[83] = 0;
		}
		if(liquids[1].getLiquid() != null) {
			values[84] = liquids[1].getLiquid().itemID;
			values[85] = liquids[1].getLiquid().itemMeta;
			values[86] = liquids[1].getLiquid().amount;
		} else {
			values[84] = 0;
			values[85] = 0;
			values[86] = 0;
		}
		if(liquids[2].getLiquid() != null) {
			values[87] = liquids[2].getLiquid().itemID;
			values[88] = liquids[2].getLiquid().itemMeta;
			values[89] = liquids[2].getLiquid().amount;
		} else {
			values[87] = 0;
			values[88] = 0;
			values[89] = 0;
		}
		return values;
	}
	
	private void useValuesArray(int[] values) {
		for(int a = 0; a < 27; a++) {
			if(values[a*3] != 0) {
				inventory[a] = new ItemStack(values[a*3], values[a*3 + 2], values[a*3 + 1]);
			} else {
				inventory[a] = null;
			}
		}
		if(values[81] != 0) {
			liquids[0].setLiquid(new LiquidStack(values[81], values[83], values[82]));
		}
		if(values[84] != 0) {
			liquids[1].setLiquid(new LiquidStack(values[84], values[86], values[85]));
		}
		if(values[87] != 0) {
			liquids[2].setLiquid(new LiquidStack(values[87], values[89], values[88]));
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
	
	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		hasUpdate = true;
		return liquids[tankIndex].fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		for (int a = 2; a >= 0; a--) {
			LiquidStack temp = liquids[a].drain(maxDrain, doDrain);
			if(temp != null) {
				return temp;
			}
		}
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		hasUpdate = true;
		return liquids[tankIndex].drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return liquids;
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		for(int a = 0; a < 3; a++) {
			if(type.asItemStack().isItemEqual(liquids[a].getLiquid().asItemStack())) {
				return liquids[a];
			}
		}
		return null;
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
		return "Liquid Crafting Table";
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

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 9;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 18;
	}

}