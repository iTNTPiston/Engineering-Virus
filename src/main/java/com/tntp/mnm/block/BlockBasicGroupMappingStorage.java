package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.tileentity.TileBasicDiskStorage;
import com.tntp.mnm.tileentity.TileBasicGroupMappingStorage;
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

public class BlockBasicGroupMappingStorage extends SBlockContainer {

  private IIcon port;
  private IIcon[] front;
  private IIcon[] front_off;

  public BlockBasicGroupMappingStorage() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileBasicGroupMappingStorage();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    this.blockIcon = reg.registerIcon(MNMMod.MODID + ":machine_0");
    port = reg.registerIcon(MNMMod.MODID + ":neithernet_port_block");
    front = new IIcon[4];
    front_off = new IIcon[front.length];
    for (int i = 0; i < front.length; i++) {
      front[i] = reg.registerIcon(tex + "_front_" + i);
      front_off[i] = reg.registerIcon(tex + "_front_" + i + "_off");
    }
  }

//  @Override
//  @SideOnly(Side.CLIENT)
//  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
//    int meta = world.getBlockMetadata(x, y, z);
//    int frontSide = meta & 7;
//    if ((side ^ 1) == frontSide) {
//      TileEntity tile = world.getTileEntity(x, y, z);
//      if (tile instanceof TileBasicGroupMappingStorage) {
//        int diskNum = ((STileData) tile).getNumDisk();
//        if (diskNum >= 0 && diskNum < front.length) {
//          boolean connected = ((STileNeithernet) tile).isConnectedToMainframe();
//          return connected ? front[diskNum] : front_off[diskNum];
//        }
//      }
//      return front_off[0];
//    } else {
//      return getIcon(side, meta);
//    }
//  }

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
      return front[front.length - 1];
    }
    return blockIcon;
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

}
