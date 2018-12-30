package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;
import com.tntp.mnm.tileentity.TileGroupMapper;
import com.tntp.mnm.tileentity.TileQueryBuilder;
import com.tntp.mnm.util.BlockUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQueryBuilder extends SBlockContainer {

  private IIcon port;
  private IIcon top;
  private IIcon[] front;

  public BlockQueryBuilder() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileQueryBuilder();
  }

  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    super.onBlockPlacedBy(world, x, y, z, entity, stack);
    byte meta = BlockUtil.getRotationalMetaFromEntityPlacing(world, x, y, z, entity, stack);
    world.setBlockMetadataWithNotify(x, y, z, meta, 2);
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    super.onBlockAdded(world, x, y, z);
    if (!world.isRemote) {
      byte meta = BlockUtil.getRotationalMetaFromBlocks(world, x, y, z);
      world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    this.blockIcon = reg.registerIcon(MNMMod.MODID + ":machine_0");
    port = reg.registerIcon(MNMMod.MODID + ":neithernet_port_block");
    front = new IIcon[4];
    for (int i = 0; i < front.length; i++) {
      front[i] = reg.registerIcon(tex + "/front_" + i);
    }
    top = reg.registerIcon(tex + "/top");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    int meta = world.getBlockMetadata(x, y, z);
    int frontSide = meta & 7;
    if ((side ^ 1) == frontSide || side == 1) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileQueryBuilder) {
        int status = ((TileQueryBuilder) tile).getMainframeStatus();
        if (status > 0) {
          return side == 1 ? top : front[status];
        }
      }
      return side == 1 ? blockIcon : front[0];
    } else {
      return getIcon(side, meta);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    if (meta == 0 || meta == 1)
      meta = 2;
    int fronts = meta & 7;
    if (side == fronts) {
      return port;
    }
    if ((side ^ 1) == fronts) {
      return front[1];
    }
    if (side == 1)
      return top;
    return blockIcon;
  }

}
