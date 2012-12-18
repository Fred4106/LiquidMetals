package LM;

import java.io.File;
import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import LM.Blocks.BlockGrinder1;
import LM.Blocks.TileGrinder1;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class DEFAULT_SETTINGS {
	public static ArrayList<String> names = new ArrayList();
	
	public static int dust = 7000;
	public static int sand = 7001;
	public static int gravel = 7002;
	public static int buckets = 7003;
	public static int liquid = 7004;
	
	public static int blockGrinder1 = 500;
	
	public static void setup() {
		names.add("Iron");
		names.add("Gold");
		names.add("Copper");
		names.add("Tin");
		names.add("Silver");
	}
	
	public static void readConfig(File configurationFile) {
		Configuration config = new Configuration(configurationFile);
		try {
			config.load();
			dust = config.get("Item Ids", "Dust", dust).getInt();
			sand = config.get("Item Ids", "Sand", sand).getInt();
			gravel = config.get("Item Ids", "Gravel", gravel).getInt();
			buckets = config.get("Item Ids", "Buckets", buckets).getInt();
			liquid = config.get("Item Ids", "Liquid", liquid).getInt();
			blockGrinder1 = config.get("Block Ids", "Small Grinder", blockGrinder1).getInt();
		}
		finally {
			config.save();
		}
	}
	
	public static void initBlocks() {
		LM_Main.blockGrinder1 = new BlockGrinder1(500);
		GameRegistry.registerBlock(LM_Main.blockGrinder1);
		LanguageRegistry.addName(LM_Main.blockGrinder1, "Rough Ore Grinder");
		GameRegistry.registerTileEntity(TileGrinder1.class, "Rough Grinder");
	}
	
	public static void initItems() {
		LM_Main.gravel = new ItemGravel(gravel);
		LM_Main.sand = new ItemSand(sand);
		LM_Main.dust = new ItemDust(dust);
		LM_Main.bucketMolten = new ItemBucket(buckets);
		LM_Main.molten = new ItemMolten(liquid);
	}
	
	public static void setupLiquids() {
		LM_Main.liquidMoltenIron = LiquidDictionary.getOrCreateLiquid("Molten " + names.get(0), new LiquidStack(LM_Main.molten.shiftedIndex, 1, 0));
		LM_Main.liquidMoltenGold = LiquidDictionary.getOrCreateLiquid("Molten " + names.get(1), new LiquidStack(LM_Main.molten.shiftedIndex, 1, 1));
		LM_Main.liquidMoltenCopper = LiquidDictionary.getOrCreateLiquid("Molten " + names.get(2), new LiquidStack(LM_Main.molten.shiftedIndex, 1, 2));
		LM_Main.liquidMoltenTin = LiquidDictionary.getOrCreateLiquid("Molten " + names.get(3), new LiquidStack(LM_Main.molten.shiftedIndex, 1, 3));
		LM_Main.liquidMoltenSilver = LiquidDictionary.getOrCreateLiquid("Molten " + names.get(4), new LiquidStack(LM_Main.molten.shiftedIndex, 1, 4));
		
		//* Comment out below to remove molten buckets without breaking worlds or items.
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(0), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 0), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(1), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 1), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(2), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 2), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(3), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 3), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(4), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 4), new ItemStack(Item.bucketEmpty)));
		//*/
	}
}
