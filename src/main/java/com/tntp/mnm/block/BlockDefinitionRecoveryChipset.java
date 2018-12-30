package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileDefinitionRecoveryChipset;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDefinitionRecoveryChipset extends SBlockContainer {

  public BlockDefinitionRecoveryChipset() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileDefinitionRecoveryChipset();
  }

}
