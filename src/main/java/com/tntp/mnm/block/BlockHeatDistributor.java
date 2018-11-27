package com.tntp.mnm.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHeatDistributor extends SBlock implements ITileEntityProvider {

  public BlockHeatDistributor(Material mat, String regName) {
    super(Material.iron, "heatDistributor");
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    // TODO Auto-generated method stub
    return null;
  }

}
