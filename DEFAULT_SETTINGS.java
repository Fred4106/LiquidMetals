package LM;

import java.io.File;
import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import LM.Blocks.BlockFurnace;
import LM.Blocks.BlockGrinder1;
import LM.Blocks.BlockGrinder2;
import LM.Blocks.BlockGrinder3;
import LM.Blocks.BlockIngotFormer;
import LM.Blocks.TileFurnace;
import LM.Blocks.TileGrinder1;
import LM.Blocks.TileGrinder2;
import LM.Blocks.TileGrinder3;
import LM.Blocks.TileIngotFormer;

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
	public static int blockGrinder2 = 501;
	public static int blockGrinder3 = 502;
	
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
			blockGrinder2 = config.get("Block Ids", "Medium Grinder", blockGrinder2).getInt();
			blockGrinder3 = config.get("Block Ids", "Large Grinder", blockGrinder3).getInt();
		}
		finally {
			config.save();
		}
	}
	
	public static void initBlocks() {
		LM_Main.blockGrinder1 = new BlockGrinder1(blockGrinder1);
		LM_Main.blockGrinder2 = new BlockGrinder2(blockGrinder2);
		LM_Main.blockGrinder3 = new BlockGrinder3(blockGrinder3);
		LM_Main.blockFurnace = new BlockFurnace(503);
		LM_Main.blockIngotFormer = new BlockIngotFormer(504);
		
		GameRegistry.registerBlock(LM_Main.blockGrinder1);
		GameRegistry.registerBlock(LM_Main.blockGrinder2);
		GameRegistry.registerBlock(LM_Main.blockGrinder3);
		GameRegistry.registerBlock(LM_Main.blockFurnace);
		GameRegistry.registerBlock(LM_Main.blockIngotFormer);
		
		LanguageRegistry.addName(LM_Main.blockGrinder1, "Rock Pulverizer");
		LanguageRegistry.addName(LM_Main.blockGrinder2, "Rough Grinder");
		LanguageRegistry.addName(LM_Main.blockGrinder3, "Fine Grinder");
		LanguageRegistry.addName(LM_Main.blockFurnace, "Arc Furnace");
		LanguageRegistry.addName(LM_Main.blockIngotFormer, "Ingot Former");
		
		GameRegistry.registerTileEntity(TileGrinder1.class, "Rock Pulverizer");
		GameRegistry.registerTileEntity(TileGrinder2.class, "Rough Grinder");
		GameRegistry.registerTileEntity(TileGrinder3.class, "Fine Grinder");
		GameRegistry.registerTileEntity(TileFurnace.class, "Arc Furnace");
		GameRegistry.registerTileEntity(TileIngotFormer.class, "Ingot Former");
	}
	
	public static void initItems() {
		LM_Main.gravel = new ItemGravel(gravel);
		LM_Main.sand = new ItemSand(sand);
		LM_Main.dust = new ItemDust(dust);
		LM_Main.bucketMolten = new ItemBucket(buckets);
		LM_Main.molten = new ItemMolten(liquid);
	}
	
	public static void setupLiquids() {
		for(int a = 0; a < names.size(); a++) {
			LiquidDictionary.getOrCreateLiquid("Molten " + names.get(a), new LiquidStack(LM_Main.molten.shiftedIndex, 1, a));
		}
		
		//* Comment out below to remove molten buckets without breaking worlds or items.
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(0), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 0), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(1), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 1), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(2), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 2), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(3), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 3), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + names.get(4), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 4), new ItemStack(Item.bucketEmpty)));
		//*/
	}
	
	public static void addGrinderRecipes() {
		GrinderRecipeManager.addRecipe(new ItemStack(Block.oreIron, 2, 0), new ItemStack(LM_Main.gravel, 3, 0), 1);
		GrinderRecipeManager.addRecipe(new ItemStack(Block.oreGold, 2, 0), new ItemStack(LM_Main.gravel, 3, 1), 1);
		GrinderRecipeManager.addRecipe("oreCopper", 2, new ItemStack(LM_Main.gravel, 3, 2), 1);
		GrinderRecipeManager.addRecipe("oreTin", 2, new ItemStack(LM_Main.gravel, 3, 3), 1);
		GrinderRecipeManager.addRecipe("oreSilver", 2, new ItemStack(LM_Main.gravel, 3, 4), 1);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 0), new ItemStack(LM_Main.sand, 3, 0), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 1), new ItemStack(LM_Main.sand, 3, 1), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 2), new ItemStack(LM_Main.sand, 3, 2), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 3), new ItemStack(LM_Main.sand, 3, 3), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 4), new ItemStack(LM_Main.sand, 3, 4), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 2, 0), new ItemStack(LM_Main.dust, 3, 0), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 2, 1), new ItemStack(LM_Main.dust, 3, 1), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 2, 2), new ItemStack(LM_Main.dust, 3, 2), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 2, 3), new ItemStack(LM_Main.dust, 3, 3), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 2, 4), new ItemStack(LM_Main.dust, 3, 4), 3);
	}
	
	public static void addIngotFormerRecipes() {
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten " + names.get(0), LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(Item.ingotIron, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten " + names.get(1), LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(Item.ingotGold, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten " + names.get(2), LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotCopper");
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten " + names.get(3), LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotTin");
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten " + names.get(4), LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotSilver");
	}
	
	public static void addArcFurnaceRecipes() {
		for(int a = 0; a < names.size(); a++) {
			ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, a), LiquidDictionary.getLiquid("Molten " + names.get(a), LiquidContainerRegistry.BUCKET_VOLUME/8));
		}
		for(int a = 0; a < names.size(); a++) {
			ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, a), LiquidDictionary.getLiquid("Molten " + names.get(a), LiquidContainerRegistry.BUCKET_VOLUME/8));
		}
		for(int a = 0; a < names.size(); a++) {
			ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.dust, 1, a), LiquidDictionary.getLiquid("Molten " + names.get(a), LiquidContainerRegistry.BUCKET_VOLUME/8));
		}
	}
	
}
