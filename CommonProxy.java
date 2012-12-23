package LiquidMetals;

import net.minecraft.world.World;
import cpw.mods.fml.common.SidedProxy;

/**
 * Proxy for things that need to happen on both the client and server.
 */
public class CommonProxy {
	@SidedProxy(clientSide = "LiquidMetals.ClientProxy", serverSide="LiquidMetals.CommonProxy")
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
