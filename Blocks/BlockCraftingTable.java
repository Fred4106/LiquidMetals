package LiquidMetals.Blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import LiquidMetals.CommonProxy;
import LiquidMetals.GuiHandler;
import LiquidMetals.LM_Main;
import buildcraft.api.tools.IToolWrench;

public class BlockCraftingTable extends BlockContainer{
	
	private Random craftingRandom = new Random();
	protected int textureOffset = 5;
	
	public BlockCraftingTable(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("smallCrafting");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getTextureFile() {
		return "/LiquidMetals/gfx/LiquidMetal/Icons.png";
	}
	
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		return BlockGrinder1.getTextureLoc(side, meta)+textureOffset;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		if (entityplayer.isSneaking())
			return false;
		
		if (!CommonProxy.proxy.isRenderWorld(world)) {
			entityplayer.openGui(LM_Main.instance, GuiHandler.Crafting, world, x, y, z);
			return true;
		}
		
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileCrafting();
	}
	//*
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileCrafting var7 = (TileCrafting)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.craftingRandom.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.craftingRandom.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.craftingRandom.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.craftingRandom.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        EntityItem var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                        if (var9.hasTagCompound())
                        {
                            var14.func_92014_d().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.craftingRandom.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.craftingRandom.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.craftingRandom.nextGaussian() * var15);
                        par1World.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	//*/
}
