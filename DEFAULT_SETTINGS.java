package LM;

import java.io.File;
import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import LM.Blocks.BlockGrinder1;
import LM.Blocks.BlockGrinder2;
import LM.Blocks.BlockGrinder3;
import LM.Blocks.TileGrinder1;
import LM.Blocks.TileGrinder2;
import LM.Blocks.TileGrinder3;

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
		LM_Main.blockGrinder2 = new BlockGrinder2(501);
		LM_Main.blockGrinder3 = new BlockGrinder3(502);
		
		GameRegistry.registerBlock(LM_Main.blockGrinder1);
		GameRegistry.registerBlock(LM_Main.blockGrinder2);
		GameRegistry.registerBlock(LM_Main.blockGrinder3);
		
		LanguageRegistry.addName(LM_Main.blockGrinder1, "Rock Pulverizer");
		LanguageRegistry.addName(LM_Main.blockGrinder2, "Rough Grinder");
		LanguageRegistry.addName(LM_Main.blockGrinder3, "Fine Grinder");
		
		GameRegistry.registerTileEntity(TileGrinder1.class, "Rock Pulverizer");
		GameRegistry.registerTileEntity(TileGrinder2.class, "Rough Grinder");
		GameRegistry.registerTileEntity(TileGrinder3.class, "Fine Grinder");
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
	
	public static void addArcFurnaceRecipes() {
		//ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 0), new LiquidStack());
	}
	
}
