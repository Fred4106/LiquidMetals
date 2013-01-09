package LiquidMetals.Integration.ComputerCraft;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLiquidTransposer extends BlockContainer {

	protected BlockLiquidTransposer(int id) {
		super(id, Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return null;
	}

}
