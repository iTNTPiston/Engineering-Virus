package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileSecurityEncoder;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurityEncoder extends SBlockContainer {

  public BlockSecurityEncoder() {
    super(Material.iron, "securityEncoder");
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileSecurityEncoder();
  }

}
