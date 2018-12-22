package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileDiskStorage;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDiskStorage extends SBlockContainer {

  public BlockDiskStorage() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileDiskStorage();
  }

}
