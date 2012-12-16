package LM;

import java.util.logging.Level;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

/**
 * Proxy for things that need to happen on both the client and server.
 */
public class CommonProxy {
	@SidedProxy(clientSide = "LM.ClientProxy", serverSide="LM.CommonProxy")
	public static CommonProxy proxy;
	
	public void registerEventHandlers() {
		
	}

	public void registerTextureFX() {
	}

	public void registerRenderers() {
		// TODO Auto-generated method stub
		
	}

	public boolean isSimulating(World worldObj) {
		return !worldObj.isRemote;
	}

	public boolean isRenderWorld(World worldObj) {
		return worldObj.isRemote;
	}
}
