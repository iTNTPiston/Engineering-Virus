package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileMainframeRecoveryChipset;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMainframeRecoveryChipset extends SBlockContainer {

  public BlockMainframeRecoveryChipset() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileMainframeRecoveryChipset();
  }

}
