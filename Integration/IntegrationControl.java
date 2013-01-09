package LiquidMetals.Integration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IntegrationControl {

	
	
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
						Object[] ob = l.toArray(); //makes it an object array  0 is object[] of inputs and 1 is output
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
			System.out.println("Damn 1.0");
		} catch (IllegalArgumentException e) {
			System.out.println("Damn 2.0");
		} catch (IllegalAccessException e) {
			System.out.println("Damn 3.0");
		}
	}
	
}
