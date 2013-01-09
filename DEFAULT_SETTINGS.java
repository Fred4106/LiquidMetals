package LiquidMetals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import LiquidMetals.Blocks.BlockFurnace;
import LiquidMetals.Blocks.BlockGrinder1;
import LiquidMetals.Blocks.BlockGrinder2;
import LiquidMetals.Blocks.BlockGrinder3;
import LiquidMetals.Blocks.BlockIngotFormer;
import LiquidMetals.Blocks.TileFurnace;
import LiquidMetals.Blocks.TileGrinder1;
import LiquidMetals.Blocks.TileGrinder2;
import LiquidMetals.Blocks.TileGrinder3;
import LiquidMetals.Blocks.TileIngotFormer;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
	public static int blockFurnace = 503;
	public static int blockIngotFormer = 504;
	
	//settings about ores and outputs and such
	private static int copperIngotOutput = 0;
	private static int copperIngotOutputMeta = 0;
	private static int tinIngotOutput = 0;
	private static int tinIngotOutputMeta = 0;
	private static int silverIngotOutput = 0;
	private static int silverIngotOutputMeta = 0;
	private static int leadIngotOutput = 0;
	private static int leadIngotOutputMeta = 0;
	
	public static void setup() {
		//*
		names.add("Iron");
		names.add("Gold");
		names.add("Copper");
		names.add("Tin");
		names.add("Silver");
		names.add("Lead");
		//*/
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
			blockFurnace = config.get("Block Ids", "Arc Furnace", blockFurnace).getInt();
			blockIngotFormer = config.get("Block Ids", "Ingot Former", blockIngotFormer).getInt();
			
			config.addCustomCategoryComment("Ingot Former Output", "Dont mess with these values unless you know what your doing.");
			copperIngotOutput = config.get("Ingot Former Output", "Ingot output copper", copperIngotOutput).getInt();
			copperIngotOutputMeta = config.get("Ingot Former Output", "Ingot output copper Meta", copperIngotOutputMeta).getInt();
			tinIngotOutput = config.get("Ingot Former Output", "Ingot output tin", tinIngotOutput).getInt();
			tinIngotOutputMeta = config.get("Ingot Former Output", "Ingot output tin Meta", tinIngotOutputMeta).getInt();
			silverIngotOutput = config.get("Ingot Former Output", "Ingot output silver", silverIngotOutput).getInt();
			silverIngotOutputMeta = config.get("Ingot Former Output", "Ingot output silver Meta", silverIngotOutputMeta).getInt();
			leadIngotOutput = config.get("Ingot Former Output", "Ingot output lead", leadIngotOutput).getInt();
			leadIngotOutputMeta = config.get("Ingot Former Output", "Ingot output lead Meta", leadIngotOutputMeta).getInt();
		}
		finally {
			config.save();
		}
	}
	
	public static void initBlocks() {
		LM_Main.blockGrinder1 = new BlockGrinder1(blockGrinder1);
		LM_Main.blockGrinder2 = new BlockGrinder2(blockGrinder2);
		LM_Main.blockGrinder3 = new BlockGrinder3(blockGrinder3);
		LM_Main.blockFurnace = new BlockFurnace(blockFurnace);
		LM_Main.blockIngotFormer = new BlockIngotFormer(blockIngotFormer);
		
		GameRegistry.registerBlock(LM_Main.blockGrinder1, "LM.Grind1");
		GameRegistry.registerBlock(LM_Main.blockGrinder2, "LM.Grind2");
		GameRegistry.registerBlock(LM_Main.blockGrinder3, "LM.Grind3");
		GameRegistry.registerBlock(LM_Main.blockFurnace, "LM.Furnace");
		GameRegistry.registerBlock(LM_Main.blockIngotFormer, "LM.IngotFormer");
		
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
		LiquidDictionary.getOrCreateLiquid("Molten Iron", new LiquidStack(LM_Main.molten.shiftedIndex, 1, 0));
		LiquidDictionary.getOrCreateLiquid("Molten Gold", new LiquidStack(LM_Main.molten.shiftedIndex, 1, 1));
		LiquidDictionary.getOrCreateLiquid("Molten Copper", new LiquidStack(LM_Main.molten.shiftedIndex, 1, 2));
		LiquidDictionary.getOrCreateLiquid("Molten Tin", new LiquidStack(LM_Main.molten.shiftedIndex, 1, 3));
		LiquidDictionary.getOrCreateLiquid("Molten Silver", new LiquidStack(LM_Main.molten.shiftedIndex, 1, 4));
		LiquidDictionary.getOrCreateLiquid("Molten Lead", new LiquidStack(LM_Main.molten.shiftedIndex, 1, 5));
		
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 0), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 1), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 2), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 3), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 4), new ItemStack(Item.bucketEmpty)));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, 5), new ItemStack(Item.bucketEmpty)));
	}
	
	public static void addGrinderRecipes() {
		GrinderRecipeManager.addRecipe(new ItemStack(Block.oreIron, 2, 0), new ItemStack(LM_Main.gravel, 3, 0), 1);
		GrinderRecipeManager.addRecipe(new ItemStack(Block.oreGold, 2, 0), new ItemStack(LM_Main.gravel, 3, 1), 1);
		GrinderRecipeManager.addRecipe("oreCopper", 2, new ItemStack(LM_Main.gravel, 3, 2), 1);
		GrinderRecipeManager.addRecipe("oreTin", 2, new ItemStack(LM_Main.gravel, 3, 3), 1);
		GrinderRecipeManager.addRecipe("oreSilver", 2, new ItemStack(LM_Main.gravel, 3, 4), 1);
		GrinderRecipeManager.addRecipe("oreLead", 2, new ItemStack(LM_Main.gravel, 3, 5), 1);
		
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 0), new ItemStack(LM_Main.sand, 3, 0), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 1), new ItemStack(LM_Main.sand, 3, 1), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 2), new ItemStack(LM_Main.sand, 3, 2), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 3), new ItemStack(LM_Main.sand, 3, 3), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 4), new ItemStack(LM_Main.sand, 3, 4), 2);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, 5), new ItemStack(LM_Main.sand, 3, 5), 2);
		
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, 0), new ItemStack(LM_Main.dust, 5, 0), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, 1), new ItemStack(LM_Main.dust, 5, 1), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, 2), new ItemStack(LM_Main.dust, 5, 2), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, 3), new ItemStack(LM_Main.dust, 5, 3), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, 4), new ItemStack(LM_Main.dust, 5, 4), 3);
		GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, 5), new ItemStack(LM_Main.dust, 5, 5), 3);
	}
	
	public static void addIngotFormerRecipes() {
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(Item.ingotIron, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(Item.ingotGold, 1));
		if(copperIngotOutput == 0) {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotCopper");
		} else {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(copperIngotOutput, 1, copperIngotOutputMeta));
		}
		
		if(tinIngotOutput == 0) {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotTin");
		} else {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(tinIngotOutput, 1, tinIngotOutputMeta));
		}
		
		if(silverIngotOutput == 0) {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotSilver");
		} else {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(silverIngotOutput, 1, silverIngotOutputMeta));
		}
		
		if(leadIngotOutput == 0) {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotLead");
		} else {
			IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(leadIngotOutput, 1, leadIngotOutputMeta));
		}
	}
	
	public static void addArcFurnaceRecipes() {
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 0), LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 1), LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 2), LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 3), LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 4), LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, 5), LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8));
		
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, 0), LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, 1), LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, 2), LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, 3), LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, 4), LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, 5), LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8));
		
		ArcFurnaceRecipeManager.addRecipe("dustIron", LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("dustGold", LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("dustCopper", LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("dustTin", LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("dustSilver", LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("dustLead", LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8));
		
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.ingotIron, 1), LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.ingotGold, 1), LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotCopper", LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotTin", LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotSilver", LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotLead", LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8));
	}

	public static void setupOreDict() {
		OreDictionary.registerOre("dustIron", new ItemStack(LM_Main.dust, 1, 0));
		OreDictionary.registerOre("dustGold", new ItemStack(LM_Main.dust, 1, 1));
		OreDictionary.registerOre("dustCopper", new ItemStack(LM_Main.dust, 1, 2));
		OreDictionary.registerOre("dustTin", new ItemStack(LM_Main.dust, 1, 3));
		OreDictionary.registerOre("dustSilver", new ItemStack(LM_Main.dust, 1, 4));
		OreDictionary.registerOre("dustLead", new ItemStack(LM_Main.dust, 1, 5));
	}
	
	public static void versionCheck() {
		String temp = LM_Main.class.getAnnotations()[0].toString();
		temp = temp.substring(temp.indexOf("version=")+8, temp.indexOf("version=")+8+5);
		
		URL pList;
		try {
			pList = new URL("http://pastebin.com/299XgcUS");
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(pList.openStream()));
			String inputLine;

	        while ((inputLine = in.readLine()) != null) {
	            if(inputLine.contains("codeVersion")) {
	                String temp2 = inputLine.subSequence(inputLine.indexOf("codeVersion")+12, inputLine.indexOf("codeVersion")+17).toString();
	                temp2 = temp2.replaceAll("_", ".");
	                if(temp.equalsIgnoreCase(temp2)) {
	                	System.out.println("{*Liquid Metals*} Version up to date");
	                } else {
	                	System.out.println("{*Liquid Metals*} Version not up to date");
	                	System.out.println(temp);
	                	System.out.println(temp2);
	                }
	                break;
	            }
	        }
		} catch (MalformedURLException e1) {
			System.out.println("{*Liquid Metals*} Unable to grab latest version.");
			
		}catch (IOException e) {
				System.out.println("{*Liquid Metals*} Unable to grab latest version.");
		}
        

       
	}
}
