package LM.Blocks;

import java.util.Random;

import buildcraft.api.tools.IToolWrench;

import LM.CommonProxy;
import LM.GuiHandler;
import LM.LM_Main;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockGrinder1 extends BlockContainer{
	
	private Random grinderRandom = new Random();
	protected int textureOffset = 2;
	
	public BlockGrinder1(int par1) {
		super(par1, Material.iron);
		setHardness(5F);
		this.setBlockName("SmallGrinder");
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getTextureFile() {
		return "/LM/gfx/LiquidMetal/Icons.png";
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
		
		// Switch orientation if whacked with a wrench.
		Item equipped = entityplayer.getCurrentEquippedItem() != null ? entityplayer.getCurrentEquippedItem().getItem() : null;
		if (equipped instanceof IToolWrench && ((IToolWrench) equipped).canWrench(entityplayer, x, y, z)) {

			world.setBlockMetadata(x, y, z, (world.getBlockMetadata(x, y, z)+1)%4);
			((IToolWrench) equipped).wrenchUsed(entityplayer, x, y, z);
			return true;
		}
		
		if (entityplayer.isSneaking())
			return false;

		if (!CommonProxy.proxy.isRenderWorld(world)) {
			openGui(world, x, y, z, entityplayer);
			return true;
		}
		
		return true;
	}
	
	public void openGui(World world, int x, int y, int z, EntityPlayer entityplayer) {
		entityplayer.openGui(LM_Main.instance, GuiHandler.Grinder1, world, x, y, z);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileGrinder1();
	}

	public static ForgeDirection metaToForgeDir(int meta) {
		if(meta == 0) {
			return ForgeDirection.SOUTH;
		}
		if(meta == 1) {
			return ForgeDirection.NORTH;
		}
		if(meta == 2) {
			return ForgeDirection.WEST;
		}
		if(meta == 3) {
			return ForgeDirection.EAST;
		}
		return null;
	}
	
	public static int getTextureLoc(int side, int meta) {
		if(side == 0 || side == 1) {
			return 208;
		}
		ForgeDirection dir = metaToForgeDir(meta);
		if(dir == ForgeDirection.NORTH) {
			//north
			if(side == 2) {
				//back
				return 160;
			}
			if(side == 3) {
				//front
				return 192;
			}
			if(side == 4) {
				//left
				return 176;
			}
			if(side == 5) {
				//right
				return 176;
			}
		}
		if(dir == ForgeDirection.SOUTH) {
			//south
			if(side == 3) {
				//front
				return 160;
			}
			if(side == 2) {
				//back
				return 192;
			}
			if(side == 4) {
				//left
				return 176;
			}
			if(side == 5) {
				//right
				return 176;
			}
		}
		if(dir == ForgeDirection.EAST) {
			//east
			if(side == 5) {
				//back
				return 160;
			}
			if(side == 4) {
				//front
				return 192;
			}
			if(side == 3) {
				//left
				return 176;
			}
			if(side == 2) {
				//right
				return 176;
			}
		}
		if(dir == ForgeDirection.WEST) {
			//west
			if(side == 4) {
				//back
				return 160;
			}
			if(side == 5) {
				//front
				return 192;
			}
			if(side == 2) {
				//left
				return 176;
			}
			if(side == 3) {
				//right
				return 176;
			}
		}
		return 0;
	}
	
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
		return getTextureLoc(side, meta)+textureOffset;
    }
	
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
            TileGrinder1 var7 = (TileGrinder1)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.grinderRandom.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.grinderRandom.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.grinderRandom.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.grinderRandom.nextInt(21) + 10;

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
                        var14.motionX = (double)((float)this.grinderRandom.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.grinderRandom.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.grinderRandom.nextGaussian() * var15);
                        par1World.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
}
