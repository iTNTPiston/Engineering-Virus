package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileDataGroupChipset;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDataGroupChipset extends SBlockContainerModelSpecial {

  public BlockDataGroupChipset() {
    super(Material.iron);
    this.setBlockBounds(0, 0, 0, 1, 5 / 16f, 1);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileDataGroupChipset();
  }

}
