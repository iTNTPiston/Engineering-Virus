package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileDataGroupDefiner;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDataGroupDefiner extends SBlockContainer {

  public BlockDataGroupDefiner() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileDataGroupDefiner();
  }

}
