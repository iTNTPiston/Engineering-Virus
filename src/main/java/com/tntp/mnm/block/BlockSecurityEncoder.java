package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileSecurityEncoder;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurityEncoder extends SBlockContainerModelSpecial {

  public BlockSecurityEncoder() {
    super(Material.iron);
    this.setBlockBounds(0, 0, 0, 1, 0.5f, 1);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileSecurityEncoder();
  }

}
