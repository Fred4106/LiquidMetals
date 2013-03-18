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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import LiquidMetals.Blocks.BlockCraftingTable;
import LiquidMetals.Blocks.BlockFurnace;
import LiquidMetals.Blocks.BlockGrinder1;
import LiquidMetals.Blocks.BlockGrinder2;
import LiquidMetals.Blocks.BlockGrinder3;
import LiquidMetals.Blocks.BlockIngotFormer;
import LiquidMetals.Blocks.TileCrafting;
import LiquidMetals.Blocks.TileFurnace;
import LiquidMetals.Blocks.TileGrinder1;
import LiquidMetals.Blocks.TileGrinder2;
import LiquidMetals.Blocks.TileGrinder3;
import LiquidMetals.Blocks.TileIngotFormer;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class DEFAULT_SETTINGS {
	public static Map<Integer, String> itemNames = new HashMap<Integer, String>();
	public static Map<Integer, Metal> liquidNames = new HashMap<Integer, Metal>();
	
	public static int dust = 7000;
	public static int sand = 7001;
	public static int gravel = 7002;
	public static int buckets = 7003;
	public static int liquid = 7004;
	public static int marker = 7005;
	
	public static int blockGrinder1 = 500;
	public static int blockGrinder2 = 501;
	public static int blockGrinder3 = 502;
	public static int blockFurnace = 503;
	public static int blockIngotFormer = 504;
	public static int blockCrafting = 505;
	
	public static int[] craftingBlackList = {Item.goldNugget.itemID, Block.blockGold.blockID, Block.blockSteel.blockID};
	public static boolean tweakRp2Alloy = true;
	public static boolean grindingVersion = true;
	
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
		itemNames.put(0, "Iron");
		itemNames.put(1, "Gold");
		itemNames.put(2, "Copper");
		itemNames.put(3, "Tin");
		itemNames.put(4, "Silver");
		itemNames.put(5, "Lead");
		
		liquidNames.put(0, new Metal("Iron", new ItemStack(Item.ingotIron), LiquidContainerRegistry.BUCKET_VOLUME/8));
		liquidNames.put(1, new Metal("Gold", new ItemStack(Item.ingotGold), LiquidContainerRegistry.BUCKET_VOLUME/8));
		liquidNames.put(2, new Metal("Copper", "ingotCopper", LiquidContainerRegistry.BUCKET_VOLUME/8));
		liquidNames.put(3, new Metal("Tin", "ingotTin", LiquidContainerRegistry.BUCKET_VOLUME/8));
		liquidNames.put(4, new Metal("Silver", "ingotSilver", LiquidContainerRegistry.BUCKET_VOLUME/8));
		liquidNames.put(5, new Metal("Lead", "ingotLead", LiquidContainerRegistry.BUCKET_VOLUME/8));
		
		liquidNames.put(32, new Metal("Glowstone", new ItemStack(Item.lightStoneDust), LiquidContainerRegistry.BUCKET_VOLUME/40));
		liquidNames.put(33, new Metal("Lapis Lazuli", new ItemStack(Item.dyePowder, 1, 4), LiquidContainerRegistry.BUCKET_VOLUME/40));
		liquidNames.put(34, new Metal("Redstone", new ItemStack(Item.redstone), LiquidContainerRegistry.BUCKET_VOLUME/40));
		liquidNames.put(35, new Metal("Nikolite", "dustNikolite", LiquidContainerRegistry.BUCKET_VOLUME/40));
		liquidNames.put(36, new Metal("Bronze", "ingotBronze", LiquidContainerRegistry.BUCKET_VOLUME/8));
		liquidNames.put(37, new Metal("Brass", "ingotBrass", LiquidContainerRegistry.BUCKET_VOLUME/8));
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
			marker = config.get("Item Ids", "Marker", marker).getInt();
			blockGrinder1 = config.get("Block Ids", "Small Grinder", blockGrinder1).getInt();
			blockGrinder2 = config.get("Block Ids", "Medium Grinder", blockGrinder2).getInt();
			blockGrinder3 = config.get("Block Ids", "Large Grinder", blockGrinder3).getInt();
			blockFurnace = config.get("Block Ids", "Arc Furnace", blockFurnace).getInt();
			blockIngotFormer = config.get("Block Ids", "Ingot Former", blockIngotFormer).getInt();
			blockCrafting = config.get("Block Ids", "Liquid Crafting", blockCrafting).getInt();
			
			config.addCustomCategoryComment("Tweaks", "Used to fine tune the behavior of Liquid Metals");
			craftingBlackList = config.get("Tweaks", "Liquid Crafting blacklist", craftingBlackList, "Enter the ids of items you dont want my crafing table to make.").getIntList();
			tweakRp2Alloy = config.get("Tweaks", "Edit rp2 alloy furnace recipes?", tweakRp2Alloy, "Removes the reclaimation recipies to avoid exploits.").getBoolean(tweakRp2Alloy);
			grindingVersion = config.get("Tweaks", "Add grinding machines?", grindingVersion).getBoolean(grindingVersion);
			
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
		if(grindingVersion) {
			LM_Main.blockGrinder1 = new BlockGrinder1(blockGrinder1);
			LM_Main.blockGrinder2 = new BlockGrinder2(blockGrinder2);
			LM_Main.blockGrinder3 = new BlockGrinder3(blockGrinder3);
			
			GameRegistry.registerBlock(LM_Main.blockGrinder1, "LM.Grind1");
			GameRegistry.registerBlock(LM_Main.blockGrinder2, "LM.Grind2");
			GameRegistry.registerBlock(LM_Main.blockGrinder3, "LM.Grind3");
			
			LanguageRegistry.addName(LM_Main.blockGrinder1, "Rock Pulverizer");
			LanguageRegistry.addName(LM_Main.blockGrinder2, "Rough Grinder");
			LanguageRegistry.addName(LM_Main.blockGrinder3, "Fine Grinder");
			
			GameRegistry.registerTileEntity(TileGrinder1.class, "Rock Pulverizer");
			GameRegistry.registerTileEntity(TileGrinder2.class, "Rough Grinder");
			GameRegistry.registerTileEntity(TileGrinder3.class, "Fine Grinder");
		}
		
		LM_Main.blockFurnace = new BlockFurnace(blockFurnace);
		LM_Main.blockIngotFormer = new BlockIngotFormer(blockIngotFormer);
		LM_Main.blockCrafting = new BlockCraftingTable(blockCrafting);
		
		GameRegistry.registerBlock(LM_Main.blockFurnace, "LM.Furnace");
		GameRegistry.registerBlock(LM_Main.blockIngotFormer, "LM.IngotFormer");
		GameRegistry.registerBlock(LM_Main.blockCrafting, "LM,LiquidCrafting");
		
		LanguageRegistry.addName(LM_Main.blockFurnace, "Arc Furnace");
		LanguageRegistry.addName(LM_Main.blockIngotFormer, "Ingot Former");
		LanguageRegistry.addName(LM_Main.blockCrafting, "Liquid Crafting Table");
		
		GameRegistry.registerTileEntity(TileFurnace.class, "Arc Furnace");
		GameRegistry.registerTileEntity(TileIngotFormer.class, "Ingot Former");
		GameRegistry.registerTileEntity(TileCrafting.class, "Liquid Crafting Table");
	}
	
	public static void initItems() {
		LM_Main.gravel = new ItemGravel(gravel);
		LM_Main.sand = new ItemSand(sand);
		LM_Main.dust = new ItemDust(dust);
		LM_Main.bucketMolten = new ItemBucket(buckets);
		LM_Main.molten = new ItemMolten(liquid);
		LM_Main.marker = new ItemMarker(marker);
		
	}
	
	public static void setupLiquids() {
		Set s = liquidNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			Metal temp = (Metal) m.getValue();
			LiquidDictionary.getOrCreateLiquid("Molten " + temp.getPrefix(), new LiquidStack(LM_Main.molten.itemID, 1, (Integer)m.getKey()));
			LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten " + temp.getPrefix(), LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(LM_Main.bucketMolten, 1, (Integer)m.getKey()), new ItemStack(Item.bucketEmpty)));
		}
	}
	
	public static void addGrinderRecipes() {
		GrinderRecipeManager.addRecipe(new ItemStack(Block.oreIron, 2, 0), new ItemStack(LM_Main.gravel, 3, 0), 1);
		GrinderRecipeManager.addRecipe(new ItemStack(Block.oreGold, 2, 0), new ItemStack(LM_Main.gravel, 3, 1), 1);
		GrinderRecipeManager.addRecipe("oreCopper", 2, new ItemStack(LM_Main.gravel, 3, 2), 1);
		GrinderRecipeManager.addRecipe("oreTin", 2, new ItemStack(LM_Main.gravel, 3, 3), 1);
		GrinderRecipeManager.addRecipe("oreSilver", 2, new ItemStack(LM_Main.gravel, 3, 4), 1);
		GrinderRecipeManager.addRecipe("oreLead", 2, new ItemStack(LM_Main.gravel, 3, 5), 1);
		
		Set s = itemNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 2, (Integer) m.getKey()), new ItemStack(LM_Main.sand, 3, (Integer) m.getKey()), 2);
			GrinderRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 4, (Integer) m.getKey()), new ItemStack(LM_Main.dust, 5, (Integer) m.getKey()), 3);
		}
	}
	
	public static void addIngotFormerRecipes() {
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(Item.ingotIron, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8), new ItemStack(Item.ingotGold, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Brass", LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotBrass");
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Bronze", LiquidContainerRegistry.BUCKET_VOLUME/8), "ingotBronze");
		
		//Dust
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Glowstone", LiquidContainerRegistry.BUCKET_VOLUME/40), new ItemStack(Item.lightStoneDust, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Lapis Lazuli", LiquidContainerRegistry.BUCKET_VOLUME/40), new ItemStack(Item.dyePowder, 1, 4));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Redstone", LiquidContainerRegistry.BUCKET_VOLUME/40), new ItemStack(Item.redstone, 1));
		IngotFormerRecipeManager.addRecipe(LiquidDictionary.getLiquid("Molten Nikolite", LiquidContainerRegistry.BUCKET_VOLUME/40), "dustNikolite");
		
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
		Set s = itemNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.gravel, 1, (Integer)m.getKey()), LiquidDictionary.getLiquid("Molten "+m.getValue(), LiquidContainerRegistry.BUCKET_VOLUME/8));
			ArcFurnaceRecipeManager.addRecipe(new ItemStack(LM_Main.sand, 1, (Integer)m.getKey()), LiquidDictionary.getLiquid("Molten "+m.getValue(), LiquidContainerRegistry.BUCKET_VOLUME/8));
			ArcFurnaceRecipeManager.addRecipe("dust"+m.getValue(), LiquidDictionary.getLiquid("Molten "+m.getValue(), LiquidContainerRegistry.BUCKET_VOLUME/8));
		}
		
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.ingotIron, 1), LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.ingotGold, 1), LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotCopper", LiquidDictionary.getLiquid("Molten Copper", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotTin", LiquidDictionary.getLiquid("Molten Tin", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotSilver", LiquidDictionary.getLiquid("Molten Silver", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotLead", LiquidDictionary.getLiquid("Molten Lead", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotBrass", LiquidDictionary.getLiquid("Molten Brass", LiquidContainerRegistry.BUCKET_VOLUME/8));
		ArcFurnaceRecipeManager.addRecipe("ingotBronze", LiquidDictionary.getLiquid("Molten Bronze", LiquidContainerRegistry.BUCKET_VOLUME/8));
		
		//For liquids that are made from non standard items;
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.lightStoneDust, 1), LiquidDictionary.getLiquid("Molten Glowstone", LiquidContainerRegistry.BUCKET_VOLUME/40));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.dyePowder, 1, 4), LiquidDictionary.getLiquid("Molten Lapis Lazuli", LiquidContainerRegistry.BUCKET_VOLUME/40));
		ArcFurnaceRecipeManager.addRecipe(new ItemStack(Item.redstone, 1), LiquidDictionary.getLiquid("Molten Redstone", LiquidContainerRegistry.BUCKET_VOLUME/40));
		ArcFurnaceRecipeManager.addRecipe("dustNikolite", LiquidDictionary.getLiquid("Molten Nikolite", LiquidContainerRegistry.BUCKET_VOLUME/40));
	}

	public static void setupOreDict() {
		//register all the dusts.
		Set s = itemNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			OreDictionary.registerOre("dust"+m.getValue(), new ItemStack(LM_Main.dust, 1, (Integer)m.getKey()));
		}
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

	public static void editRp2Recipes() {
		try {
			Field[] fields = Class.forName("com.eloraam.redpower.core.CraftLib").getDeclaredFields();
			List tempAlloyRecipes = new ArrayList();
			
			for(Field f : fields) {
				if(f.getName().equals("alloyRecipes")) {
					f.setAccessible(true);
					List d = (List) f.get(null); //this is the list of alloyRecipes
					for(int a = 0; a < d.size(); a++) {
						List l = (List)d.get(a); //gets the parts of the recipes
						Object[] ob = l.toArray(); //makes it an object array 0 is object[] of inputs and 1 is output
						if(!((ItemStack)ob[1]).isItemEqual(new ItemStack(Item.ingotGold, 1))) {
							if(!((ItemStack)ob[1]).isItemEqual(new ItemStack(Item.ingotIron, 1))) {
								tempAlloyRecipes.add(l);
							}
						}
					}
					f.set(null, tempAlloyRecipes);
					f.setAccessible(false);
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("No Redpower2 detected.");
		} catch (IllegalArgumentException e) {
			System.out.println("Damn 2.0");
		} catch (IllegalAccessException e) {
			System.out.println("Damn 3.0");
		}
	}
	
	public static void fixLiquids() {
		Set s = liquidNames.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()) {
			Map.Entry m = (Map.Entry)it.next();
			((Metal)m.getValue()).updateLiquid();
		}
	}
}
