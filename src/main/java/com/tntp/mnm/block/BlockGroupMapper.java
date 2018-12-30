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

public class BlockGroupMapper extends SBlockContainer {

  private IIcon port;
  private IIcon top;
  private IIcon front;
  private IIcon front_off;
  private IIcon front_debug;

  public BlockGroupMapper() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileGroupMapper();
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
    front = reg.registerIcon(tex + "_front");
    front_off = reg.registerIcon(tex + "_front_off");
    front_debug = reg.registerIcon(tex + "_front_debug");
    top = reg.registerIcon(tex + "_top");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    int meta = world.getBlockMetadata(x, y, z);
    int frontSide = meta & 7;
    if ((side ^ 1) == frontSide || side == 1) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileGroupMapper) {
        switch (((TileGroupMapper) tile).getMainframeStatus()) {
        case 1:
          return side == 1 ? top : front;
        case 2:
        case 3:
          return side == 1 ? top : front_debug;

        }
      }
      return side == 1 ? blockIcon : front_off;
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
      return front;
    }
    if (side == 1)
      return top;
    return blockIcon;
  }

}
