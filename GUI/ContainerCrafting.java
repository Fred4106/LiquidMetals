package LiquidMetals.GUI;

import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.core.proxy.CoreProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import LiquidMetals.CommonProxy;
import LiquidMetals.Blocks.TileCrafting;
public class ContainerCrafting extends BuildCraftContainer {

	TileCrafting tile;

	// public InventoryCrafting craftMatrix;
	public IInventory craftResult;

	public class SlotAutoCrafting extends Slot {

		private final IInventory craftMatrix;
		private EntityPlayer thePlayer;

		public SlotAutoCrafting(EntityPlayer entityplayer, IInventory iinventory, IInventory iinventory1, int i, int j, int k) {
			super(iinventory1, i, j, k);
			thePlayer = entityplayer;
			craftMatrix = iinventory;
		}

		@Override
		public boolean isItemValid(ItemStack itemstack) {
			return false;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer pl, ItemStack itemstack) {
			CoreProxy.proxy.onCraftingPickup(thePlayer.worldObj, thePlayer, itemstack);
			if (itemstack.itemID == Block.workbench.blockID) {
				thePlayer.addStat(AchievementList.buildWorkBench, 1);
			} else if (itemstack.itemID == Item.pickaxeWood.shiftedIndex) {
				thePlayer.addStat(AchievementList.buildPickaxe, 1);
			} else if (itemstack.itemID == Block.stoneOvenIdle.blockID) {
				thePlayer.addStat(AchievementList.buildFurnace, 1);
			} else if (itemstack.itemID == Item.hoeWood.shiftedIndex) {
				thePlayer.addStat(AchievementList.buildHoe, 1);
			} else if (itemstack.itemID == Item.bread.shiftedIndex) {
				thePlayer.addStat(AchievementList.makeBread, 1);
			} else if (itemstack.itemID == Item.cake.shiftedIndex) {
				thePlayer.addStat(AchievementList.bakeCake, 1);
			} else if (itemstack.itemID == Item.pickaxeStone.shiftedIndex) {
				thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
			} else if (itemstack.itemID == Item.swordWood.shiftedIndex) {
				thePlayer.addStat(AchievementList.buildSword, 1);
			} else if (itemstack.itemID == Block.enchantmentTable.blockID) {
				thePlayer.addStat(AchievementList.enchantments, 1);
			} else if (itemstack.itemID == Block.bookShelf.blockID) {
				thePlayer.addStat(AchievementList.bookcase, 1);
			}
			CoreProxy.proxy.TakenFromCrafting(thePlayer, itemstack, craftMatrix);

			tile.extractItem(true, true);
		}

	}

	public ContainerCrafting(InventoryPlayer inventoryplayer, TileCrafting tile) {
		super(tile.getSizeInventory());

		craftResult = new InventoryCraftResult();
		this.tile = tile;
		addSlotToContainer(new SlotAutoCrafting(inventoryplayer.player, tile, craftResult, 0, 124, 35));
		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 3; k1++) {
				addSlotToContainer(new Slot(tile, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
			}

		}

		for (int i1 = 0; i1 < 3; i1++) {
			for (int l1 = 0; l1 < 9; l1++) {
				addSlotToContainer(new Slot(inventoryplayer, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18));
			}

		}

		for (int j1 = 0; j1 < 9; j1++) {
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 142));
		}

		onCraftMatrixChanged(tile);
	}

	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();
		craftResult.setInventorySlotContents(0, tile.findRecipe());
	}

	@Override
	public ItemStack slotClick(int i, int j, int flag, EntityPlayer entityplayer) {
		// This call ensures that the ouptut is correctly computed
		craftResult.setInventorySlotContents(0, tile.findRecipe());

		ItemStack ret = super.slotClick(i, j, flag, entityplayer);
		onCraftMatrixChanged(tile);

		return ret;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tile.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer pl, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i == 0) {
				if (!mergeItemStack(itemstack1, 10, 46, true))
					return null;
			} else if (i >= 10 && i < 37) {
				if (!mergeItemStack(itemstack1, 37, 46, false))
					return null;
			} else if (i >= 37 && i < 46) {
				if (!mergeItemStack(itemstack1, 10, 37, false))
					return null;
			} else if (!mergeItemStack(itemstack1, 10, 46, false))
				return null;
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(pl, itemstack1);
			} else
				return null;
		}
		return itemstack;
	}

}