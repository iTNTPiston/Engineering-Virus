package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileNeithernetPort;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockNeithernetPort extends SBlock implements ITileEntityProvider {

  public BlockNeithernetPort() {
    super(Material.iron, "neithernetPort");
    // TODO Auto-generated constructor stub
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileNeithernetPort();
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
