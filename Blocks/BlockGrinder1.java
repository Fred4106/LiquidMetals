package LiquidMetals.Blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
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
import buildcraft.api.core.Position;
import buildcraft.api.tools.IToolWrench;
import buildcraft.core.utils.Utils;
import buildcraft.factory.TileQuarry;

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
		return "/LiquidMetals/gfx/LiquidMetal/Icons.png";
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);

		ForgeDirection orientation = Utils.get2dOrientation(new Position(entityliving.posX, entityliving.posY, entityliving.posZ), new Position(i, j, k));
		if(orientation == ForgeDirection.NORTH) {
			world.setBlockMetadataWithNotify(i, j, k, 0);
		} else if(orientation == ForgeDirection.EAST){
			world.setBlockMetadataWithNotify(i, j, k, 1);
		} else if(orientation == ForgeDirection.SOUTH){
			world.setBlockMetadataWithNotify(i, j, k, 2);
		} else if(orientation == ForgeDirection.WEST){
			world.setBlockMetadataWithNotify(i, j, k, 3);
		} 
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
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		TileGrinder1 tile = (TileGrinder1) world.getBlockTileEntity(i, j, k);

		if (tile != null) {
			tile.checkRedstonePower();
		}
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
			return ForgeDirection.WEST;
		}
		if(meta == 2) {
			return ForgeDirection.NORTH;
		}
		if(meta == 3) {
			return ForgeDirection.EAST;
		}
		return null;
	}
	
	public static ForgeDirection getFront(int meta) {
		return metaToForgeDir(meta);
	}
	
	public static ForgeDirection getBack(int meta) {
		return getFront(meta).getOpposite();
	}
	
	public static ForgeDirection getLeft(int meta) {
		ForgeDirection front = getFront(meta);
		if(front == ForgeDirection.NORTH) {
			return ForgeDirection.WEST;
		}
		if(front == ForgeDirection.SOUTH) {
			return ForgeDirection.EAST;
		}
		if(front == ForgeDirection.EAST) {
			return ForgeDirection.NORTH;
		}
		if(front == ForgeDirection.WEST) {
			return ForgeDirection.SOUTH;
		}
		return ForgeDirection.UNKNOWN;
	}
	
	public static ForgeDirection getRight(int meta) {
		return getLeft(meta).getOpposite();
	}
	
	public static ForgeDirection getTop(int meta) {
		return ForgeDirection.UP;
	}
	
	public static ForgeDirection getBottom(int meta) {
		return ForgeDirection.DOWN;
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
	//*
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
                            var14.func_92014_d().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
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
	//*/
}
