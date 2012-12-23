package LiquidMetals;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import LiquidMetals.Blocks.BlockFurnace;
import LiquidMetals.Blocks.BlockGrinder1;
import LiquidMetals.Blocks.BlockGrinder2;
import LiquidMetals.Blocks.BlockGrinder3;
import LiquidMetals.Blocks.BlockIngotFormer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * This is the main mod class. This is basically just an event handler which passes
 * off events to different classes as needed to get things loaded correctly.
 * 
 * This class also holds references to all our blocks and items that get added to the game.
 */
@Mod(modid = "LiquidMetals", version = "0.1.1", name = "Liquid Metals", useMetadata = false, dependencies="required-after:BuildCraft|Silicon; required-after:BuildCraft|Core; required-after:BuildCraft|Transport; required-after:BuildCraft|Factory; required-after:BuildCraft|Energy; required-after:BuildCraft|Builders;")
@NetworkMod(serverSideRequired = true, clientSideRequired = true)
public class LM_Main {

	@Instance("LiquidMetals")
	public static LM_Main instance = new LM_Main();
	public static GuiHandler guiHandler = new GuiHandler();
	
	@SidedProxy(clientSide = "LiquidMetals.ClientProxy", serverSide = "LiquidMetals.CommonProxy")
	public static CommonProxy proxy;
	
	//start
	public static Item molten;
	public static Item bucketMolten;	
	public static Item gravel;
	public static Item sand;
	public static Item dust;
	public static LiquidStack liquid;
	//end
	
	//start blocks
	public static BlockGrinder1 blockGrinder1;
	public static BlockGrinder2 blockGrinder2;
	public static BlockGrinder3 blockGrinder3;
	public static BlockFurnace blockFurnace;
	public static BlockIngotFormer blockIngotFormer;
	
	/**
	 * The mod's pre-initialisation event hook. Deals with reading and/or initialising the configuration file
	 * and registering all our blocks and items (which has to be done here so that they're in the
	 * system before statistics and achievements are initialised - or it borks!).
	 * 
	 * @param event The event details.
	 */
	@PreInit
	public void preInitialise(FMLPreInitializationEvent event) {
		DEFAULT_SETTINGS.readConfig(event.getSuggestedConfigurationFile());
		DEFAULT_SETTINGS.setup();
		DEFAULT_SETTINGS.initBlocks();
		DEFAULT_SETTINGS.initItems();
		DEFAULT_SETTINGS.setupLiquids();
		OreDictionary.registerOre("oreCopper", new ItemStack(Block.sand, 1, 0));
		proxy.registerEventHandlers();
	}
	
	/**
	 * The mod's initialisation event hook (previously the 'load' function). Deals with registration of generic
	 * recipes, pre-loading of textures, initialisation of rendering functionality and GUI handler registration.
	 *   
	 * @param event The event details.
	 */
	@Init
	public void initialise(FMLInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(instance, guiHandler);
		/*
		//iron
		moltenIron = new ItemLiquidMetal(7000).setItemName("moltenIron").setIconIndex(0);
		bucketMoltenIron = new ItemLiquidMetal(7001).setItemName("bucketMoltenIron").setContainerItem(Item.bucketEmpty).setIconIndex(1).setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc);
		liquidMoltenIron = LiquidDictionary.getOrCreateLiquid("Molten Iron", new LiquidStack(moltenIron, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Iron", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketMoltenIron), new ItemStack(Item.bucketEmpty)));
		LanguageRegistry.addName(bucketMoltenIron, "Molten Iron Bucket");
		LanguageRegistry.addName(moltenIron, "Molten Iron");
		//gold
		moltenGold = new ItemLiquidMetal(7002).setItemName("moltenGold").setIconIndex(2);
		bucketMoltenGold = new ItemLiquidMetal(7003).setItemName("bucketMoltenGold").setContainerItem(Item.bucketEmpty).setIconIndex(3).setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMisc);
		liquidMoltenGold = LiquidDictionary.getOrCreateLiquid("Molten Gold", new LiquidStack(moltenGold, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Molten Gold", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketMoltenGold), new ItemStack(Item.bucketEmpty)));
		LanguageRegistry.addName(bucketMoltenGold, "Molten Gold Bucket");
		LanguageRegistry.addName(moltenGold, "Molten Gold");
		*/
		proxy.registerRenderers();
		proxy.registerTextureFX();
		
	}
	
	/**
	 * The mod's post-initialisation event hook (previously 'modsLoaded'). Deals with integration into other
	 * (now initialised) mods, including - but not limited to - registration of mod-reliant recipes.
	 *
	 * @param event The event details.
	 */
	@PostInit
	public void postInitialise(FMLPostInitializationEvent event) {
		DEFAULT_SETTINGS.addGrinderRecipes();
		DEFAULT_SETTINGS.addArcFurnaceRecipes();
		DEFAULT_SETTINGS.addIngotFormerRecipes();
		/*
		CraftingManager.getInstance().addRecipe(new ItemStack(blockGrinder1), "#@#", "$%$", "###", '#', Item.ingotIron, '$', Block.pistonBase, '%', Block.blockSteel, '@', BuildCraftTransport.pipePowerWood);
		CraftingManager.getInstance().addRecipe(new ItemStack(blockGrinder2), "#@#", "$%$", "###", '#', Item.ingotGold, '$', BuildCraftCore.ironGearItem, '%', blockGrinder1, '@', BuildCraftTransport.pipePowerStone);
		CraftingManager.getInstance().addRecipe(new ItemStack(blockGrinder3), "#@#", "$%$", "###", '#', Item.diamond, '$', BuildCraftCore.goldGearItem, '%', blockGrinder2, '@', BuildCraftTransport.pipePowerGold);
		CraftingManager.getInstance().addRecipe(new ItemStack(blockIngotFormer), "!@!", "!#!", "!!!", '!', Item.ingotIron, '@', BuildCraftFactory.tankBlock, '#', BuildCraftCore.goldGearItem);
		CraftingManager.getInstance().addRecipe(new ItemStack(this.blockFurnace), "#@#", "#%#", "#*#", '#', Item.ingotIron, '@', BuildCraftCore.diamondGearItem, '%', Block.blockSteel, '*', BuildCraftTransport.pipePowerGold);
		*/
	}
	
}