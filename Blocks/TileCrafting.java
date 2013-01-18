package LiquidMetals.Blocks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

import buildcraft.api.core.Position;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
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
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.tileentity.TileEntityChest;
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

public class TileCrafting extends TileBuildCraft implements IInventory, ITankContainer, ISidedInventory, IPeripheral, IPowerReceptor{

	public ItemStack[] inventory = new ItemStack[27];
	public LiquidTank[] liquids = new LiquidTank[3];
	public boolean hasUpdate = false;
	private float liquidMultiplier = .95f;
	
	public boolean redstonePowered = false;
	
	public boolean computerMode = false;
	public boolean enabled = false;
	public IComputerAccess computer = null;
	
	public InventoryCrafting craftMatrix;
	private IPowerProvider powerProvider;
	
//	ItemStack[] blackList = {new ItemStack(Item.goldNugget, 1), new ItemStack(Block.blockGold, 1), new ItemStack(Block.blockSteel, 1)};
	
	public TileCrafting() {
		for(int a = 0; a < 3; a++) {
			liquids[a] = new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME * 1);
		}
		craftMatrix = new InventoryCrafting(new ContainerCrafting.ContainerNull(), 3, 3);
		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		powerProvider.configure(5, 200, 200, 200, 200);
	}
	
	/* UPDATING */
	@Override
	public void updateEntity() {
		if (CommonProxy.proxy.isSimulating(worldObj) && (worldObj.getWorldTime() % 80 == 0 || hasUpdate)) {
			this.checkRedstonePower();
			sendNetworkUpdate();
			hasUpdate = false;
		}
		
		if(CommonProxy.proxy.isSimulating(worldObj)) {
			doCraft();
		}
	}
	
	private boolean doCraft() {
		if(canCraft()) {
			if(powerProvider.useEnergy(200, 200, true) == 200) {
				ItemStack toOutput = getCraftingResult();
				removeEnoughItems();
				removeEnoughLiquid();
				if(computer != null) {
					computer.queueEvent("Item Crafted", new Object[]{toOutput.getDisplayName(), toOutput.stackSize});
				}
				dropOutput(toOutput);
				return true;
			}
		}
		return false;
	}
	
	public void checkRedstonePower() {
		redstonePowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	private int getMaxStackToAdd(IInventory tile, int slotNum) {
		if(tile.getInventoryStackLimit()-tile.getStackInSlot(slotNum).stackSize < 1) {
			return 0;
		}
		if(tile.getStackInSlot(slotNum).getMaxStackSize()-tile.getStackInSlot(slotNum).stackSize < 1) {
			return 0;
		}
		if(tile.getInventoryStackLimit()-tile.getStackInSlot(slotNum).stackSize > tile.getStackInSlot(slotNum).getMaxStackSize()-tile.getStackInSlot(slotNum).stackSize) {
			return tile.getStackInSlot(slotNum).getMaxStackSize()-tile.getStackInSlot(slotNum).stackSize;
		} else {
			return tile.getInventoryStackLimit()-tile.getStackInSlot(slotNum).stackSize;
		}
	}
	
	public ItemStack addOutputToInventory(ItemStack toDrop, IInventory tile) {
		int start = 0;
		int end = tile.getSizeInventory();
		if(tile instanceof ISidedInventory) {
			start = ((ISidedInventory) tile).getStartInventorySide(BlockGrinder1.metaToForgeDir(this.getBlockMetadata()).getOpposite());
			end = start + ((ISidedInventory) tile).getSizeInventorySide(BlockGrinder1.metaToForgeDir(this.getBlockMetadata()).getOpposite());
		}
		for(int a = start; a < end; a++) {
			if(tile.getStackInSlot(a) == null){
				if(tile.getInventoryStackLimit() >= toDrop.copy().stackSize){
					tile.setInventorySlotContents(a, toDrop);
					toDrop = null;
					break;
				} else {
					tile.setInventorySlotContents(a, new ItemStack(toDrop.itemID, tile.getInventoryStackLimit(), toDrop.getItemDamage()));
					toDrop.stackSize -= tile.getInventoryStackLimit();
				}
			} else {
				if(tile.getStackInSlot(a).isItemEqual(toDrop)) {
					if(getMaxStackToAdd(tile, a) >= toDrop.stackSize) {
						tile.getStackInSlot(a).stackSize+= toDrop.stackSize;
						toDrop = null;
						break;
					} else {
						int maxStack = getMaxStackToAdd(tile, a);
						tile.getStackInSlot(a).stackSize += maxStack;
						toDrop.stackSize -= maxStack;
					}
				}
			}
		}
		return toDrop;
	}
	
	public void dropOutput(ItemStack toDrop) {
		int side = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int x = xCoord;
		int z = zCoord;
		
		if(side == 0) {
			z++;
		} else if(side == 1) {
			x--;
		} else if(side == 2) {
			z--;
		} else if(side == 3) {
			x++;
		}
		
		IInventory tile = (IInventory) (worldObj.getBlockTileEntity(x, yCoord, z));
		toDrop = addOutputToInventory(toDrop, tile);
		
		if(tile instanceof TileEntityChest && toDrop != null) {
			TileEntityChest tile2 = (TileEntityChest) tile;
			if(tile2.adjacentChestZNeg != null) {
				toDrop = this.addOutputToInventory(toDrop, tile2.adjacentChestZNeg);
			} else if(tile2.adjacentChestZPosition != null) {
				toDrop = this.addOutputToInventory(toDrop, tile2.adjacentChestZPosition);
			} else if(tile2.adjacentChestXNeg != null) {
				toDrop = this.addOutputToInventory(toDrop, tile2.adjacentChestXNeg);
			} else if(tile2.adjacentChestXPos != null) {
				toDrop = this.addOutputToInventory(toDrop, tile2.adjacentChestXPos);
			}
		}
		
		if(toDrop != null && toDrop.stackSize > 0) {
			EntityItem var14 = new EntityItem(worldObj, (double)(this.xCoord), (double)((float)yCoord), (double)((float)zCoord), toDrop);

	        if (toDrop.hasTagCompound())
	        {
	            var14.func_92014_d().setTagCompound((NBTTagCompound)toDrop.getTagCompound().copy());
	        }

	        float var15 = 0.05F;
	        
	        var14.motionX = (double)((float)new Random().nextGaussian() * var15);
	        var14.motionY = (double)((float)new Random().nextGaussian() * var15 + 0.2F);
	        var14.motionZ = (double)((float)new Random().nextGaussian() * var15);
	        
	        worldObj.spawnEntityInWorld(var14);
		}
	}
	
	private boolean hasInventoryOnSide(ItemStack toDrop) {
		int side = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int x = xCoord;
		int z = zCoord;
		
		if(side == 0) {
			z++;
		} else if(side == 1) {
			x--;
		} else if(side == 2) {
			z--;
		} else if(side == 3) {
			x++;
		}
		
		TileEntity tile1 = worldObj.getBlockTileEntity(x, yCoord, z);
		if(tile1 == null) {
			return false;
		}
		if(tile1 instanceof IInventory) {
			return true;
		}
		return false;
	}
	
	private boolean canCraft() {
		if(redstonePowered && computerMode == false) {
			return false;
		}
		if(computerMode && enabled == false) {
			return false;
		}
		ItemStack item = getCraftingResult();
		if(!containsMarker()) {
			return false;
		}
		if(hasEnoughLiquid() && item != null) {
			if(!hasInventoryOnSide(item)) {
				return false;
			}
			if(invContainsEnoughItems()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsMarker() {
		for(int a = 0; a < 9; a++) {
			if(inventory[a] == null) {
				continue;
			}
			if(inventory[a].itemID == LM_Main.marker.itemID) {
				return true;
			}
		}
		return false;
	}
	
	private void removeEnoughItems() {
		ItemStack[] crafting = getMasterList();
		for(int a = 0; a < crafting.length; a++) {
			for(int b = 9; b < 27; b++) {
				if(inventory[b] == null) {
					
				} else {
					if(crafting[a].isItemEqual(inventory[b])) {
						if(inventory[b].copy().stackSize > crafting[a].copy().stackSize) {
							inventory[b].stackSize-=crafting[a].stackSize;
							crafting[a] = null;
							break;
						} else if(inventory[b].copy().stackSize < crafting[a].copy().stackSize){
							crafting[a].stackSize-= inventory[b].stackSize;
							inventory[b] = null;
						} else {
							crafting[a] = null;
							inventory[b] = null;
							break;
						}
					}
				}
			}
		}
		
	}
	
	private boolean invContainsEnoughItems() {
		ItemStack[] crafting = getMasterList();
		ItemStack[] inventory = getMasterListLowerInv();
		for(int a = 0; a < crafting.length; a++) {
			for(int b = 0; b < inventory.length; b++) {
				if(inventory[b] == null) {
					
				} else {
					if(crafting[a].isItemEqual(inventory[b])) {
						if(inventory[b].copy().stackSize > crafting[a].copy().stackSize) {
							inventory[b].stackSize-=crafting[a].stackSize;
							crafting[a] = null;
							break;
						} else if(inventory[b].copy().stackSize < crafting[a].copy().stackSize){
							crafting[a].stackSize-= inventory[b].stackSize;
							inventory[b] = null;
						} else {
							crafting[a] = null;
							inventory[b] = null;
							break;
						}
					}
				}
			}
		}
		
		for(int a = 0; a < crafting.length; a++) {
			if(crafting[a] != null) {
				return false;
			}
		}
		
		return true;
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
				a--;
			}
		}
		
		ItemStack[] items = new ItemStack[masterList.size()];
		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		for(int a = 0; a < masterList.size(); a++) {
			items[a] = masterList.get(a).copy();
			//System.out.println(items[a]);
		}
		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		return items;
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
				a--;
			}
		}
		
		ItemStack[] items = new ItemStack[masterList.size()];
		//System.out.println("**********************************");
		for(int a = 0; a < masterList.size(); a++) {
			items[a] = masterList.get(a).copy();
			//System.out.println(items[a]);
		}
		//	System.out.println("**********************************");
		return items;
	}
	
	private void removeEnoughLiquid() {
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
					liquidAmounts[a] = (int)(DEFAULT_SETTINGS.liquidNames.get(liquids[a].getLiquid().itemMeta).getAmount()*markers[a]*liquidMultiplier);
				}
			}
		}
		for(int a = 0; a < 3; a++) {
			if(liquids[a].getLiquid() != null) {
				if(liquids[a].getLiquid().itemID == LM_Main.molten.itemID) {
					liquids[a].drain(liquidAmounts[a], true);
				}
			}
		}
		
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
					liquidAmounts[a] = (int)(DEFAULT_SETTINGS.liquidNames.get(liquids[a].getLiquid().itemMeta).getAmount()*markers[a]*liquidMultiplier);
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
		ItemStack output = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj);
		if(output == null) {
			return output;
		}
		for(int a = 0; a < DEFAULT_SETTINGS.craftingBlackList.length; a++) {
			if(output.getItem().itemID == DEFAULT_SETTINGS.craftingBlackList[a]) {
				return null;
			}
		}
		return output;
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
		for(int a = 0; a < 3; a++) {
			if(liquids[a].getLiquid() != null) {
				if(liquids[a].getLiquid().asItemStack().isItemEqual(resource.asItemStack())) {
					int temp = liquids[a].fill(resource, doFill);
					if(temp > 0) {
						hasUpdate = true;
					}
					return temp;
				}
			}
		}
		for(int a = 0; a < 3; a++) {
			int temp = liquids[a].fill(resource, doFill);
			if(temp > 0) {
				hasUpdate = true;
				return temp;
			}
		}
		return 0;
	}

	private int[] getValuesArray() {
		int[] values = new int[93];
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
		values[90] = 0;
		if(this.redstonePowered) {
			values[90] = 1;
		}
		values[91] = 0;
		if(this.computerMode) {
			values[91] = 1;
		}
		values[92] = 0;
		if(this.enabled) {
			values[92] = 1;
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
		this.redstonePowered = false;
		if(values[90] != 0) {
			this.redstonePowered = true;
		}
		this.computerMode = false;
		if(values[91] != 0) {
			this.computerMode = true;
		}
		this.enabled = false;
		if(values[92] != 0) {
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
	
	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		int temp = liquids[tankIndex].fill(resource, doFill);
		if(temp>0) {
			hasUpdate = true;
		}
		return temp;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		for (int a = 2; a >= 0; a--) {
			LiquidStack temp = liquids[a].drain(maxDrain, doDrain);
			if(temp != null) {
				hasUpdate = true;
				return temp;
			}
		}
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		LiquidStack temp = liquids[tankIndex].drain(maxDrain, doDrain);
		if(temp != null) {
			hasUpdate = true;
		}
		return temp;
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

	public void setComputerMode(Object bool, IComputerAccess computer) {
		this.computerMode = (Boolean) bool;
		this.computer = null;
		if(this.computerMode) {
			this.computer = computer;
		}
	}
	
	public void setEnabled(Object bool) {
		this.enabled = (Boolean) bool;
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[] {"canCraft", "getOutputName", "getOutputAmount", "getNameInSlot", "getAmountInSlot", "getNameInTank", "getAmountInTank", "setComputerMode", "setEnabled"};
	}
	
	public String getOutputName() {
		if(canCraft()) {
			return getCraftingResult().getDisplayName();
		}
		return "null";
	}
	
	public int getOutputAmount() {
		if(canCraft()) {
			return getCraftingResult().copy().stackSize;
		}
		return 0;
	}
	
	public String getNameInSlot(Object o) {
		int slot = ((Integer) o);
		if(slot < 1 || slot > 18) {
			return "Slot "+slot+" is out of range";
		}
		if(inventory[slot+8] != null) {
			return inventory[slot+8].getDisplayName();
		}
		return "null";
	}
	
	public int getAmountInSlot(Object o) {
		int slot = ((Integer) o);
		if(slot < 1 || slot > 18) {
			return -1;
		}
		if(inventory[slot+8] != null) {
			return inventory[slot+8].stackSize;
		}
		return 0;
	}
	
	public String getNameInTank(Object o) {
		int slot = ((Integer) o);
		if(slot < 1 || slot > 3) {
			return "Slot "+slot+" is out of range";
		}
		if(liquids[slot].getLiquid() != null) {
			return liquids[slot].getLiquid().asItemStack().getDisplayName();
		}
		return "null";
	}
	
	public int getAmountInTank(Object o) {
		int slot = ((Integer) o);
		if(slot < 1 || slot > 3) {
			return -1;
		}
		if(liquids[slot].getLiquid() != null) {
			return liquids[slot].getLiquid().amount;
		}
		return 0;
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, int method,
			Object[] arguments) throws Exception {
		if(method == 0) {
			return new Object[] {canCraft()};
		}
		if(method == 1) {
			return new Object[] {getOutputName()};
		}
		if(method == 2) {
			return new Object[] {getOutputAmount()};
		}
		if(method == 3) {
			return new Object[] {getNameInSlot(arguments[0])};
		}
		if(method == 4) {
			return new Object[] {getAmountInSlot(arguments[0])};
		}
		if(method == 5) {
			return new Object[] {getNameInTank(arguments[0])};
		}
		if(method == 6) {
			return new Object[] {getAmountInTank(arguments[0])};
		}
		if(method == 7) {
			setComputerMode(arguments[0], computer);
			return new Object[] {};
		}
		if(method == 7) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		return "Liquid Crafting Table";
	}
	
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
	
}