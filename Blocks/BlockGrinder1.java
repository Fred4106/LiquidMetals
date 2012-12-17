package LM.Blocks;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockGrinder1 extends BlockContainer{

	protected BlockGrinder1(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("LargeGrinder");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileGrinder1();
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		return 65;
	}
	
}
