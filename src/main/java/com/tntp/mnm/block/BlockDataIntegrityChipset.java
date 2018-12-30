package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileDataIntegrityChipset;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDataIntegrityChipset extends SBlock implements ITileEntityProvider {

  public BlockDataIntegrityChipset() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileDataIntegrityChipset();
  }

}
