package LM.Blocks;

import java.util.Random;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import LM.CommonProxy;
import LM.GuiHandler;
import LM.LM_Main;

public class BlockIngotFormer extends BlockContainer{

	private Random ingotFormerRandom = new Random();

	public BlockIngotFormer(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("ingotFormer");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getTextureFile() {
		return "/LM/gfx/LiquidMetal/Icons.png";
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		return 69;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		if (entityplayer.isSneaking())
			return false;

		if (!CommonProxy.proxy.isRenderWorld(world)) {
			entityplayer.openGui(LM_Main.instance, GuiHandler.IngotFormer, world, x, y, z);
			return true;	
		}
		
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileIngotFormer();
	}

	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileIngotFormer var7 = (TileIngotFormer)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.ingotFormerRandom.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.ingotFormerRandom.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.ingotFormerRandom.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.ingotFormerRandom.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        EntityItem var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                        if (var9.hasTagCompound())
                        {
                            var14.item.setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.ingotFormerRandom.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.ingotFormerRandom.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.ingotFormerRandom.nextGaussian() * var15);
                        par1World.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
}