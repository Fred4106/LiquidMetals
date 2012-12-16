package LM;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;

public class DEFAULT_SETTINGS {
	public static int ironDust = 700;
	public static int goldDust = 701;
	public static int copperDust = 702;
	public static int tinDust = 703;
	public static int silverDust = 704;
	
	public static ArrayList<Metal> metals = new ArrayList();
	
	public static void readConfig(File configurationFile) {
		Configuration config = new Configuration(configurationFile);
		try {
		config.load();
		ironDust = config.get("Ids", "Iron Dust ID", ironDust, "Id to use for iron dust").getInt();
		goldDust = config.get("Ids", "Gold Dust ID", goldDust, "Id to use for gold dust").getInt();
		copperDust = config.get("Ids", "Copper Dust ID", copperDust, "Id to use for copper dust").getInt();
		tinDust = config.get("Ids", "Tin Dust ID", tinDust, "Id to use for tin dust").getInt();
		silverDust = config.get("Ids", "Silver Dust ID", silverDust, "Id to use for silver dust").getInt();
		config.save();
		}
		finally {
			config.save();
			metals.add(new Metal(new ItemStack(Block.oreIron, 0, 1), ironDust, new ItemStack(Item.ingotIron, 0, 1)));
			metals.add(new Metal(new ItemStack(Block.oreGold, 0, 1), goldDust, new ItemStack(Item.ingotGold, 0, 1)));
			metals.add(new Metal("plankWood", silverDust, "logWood"));
		}
	}
}
