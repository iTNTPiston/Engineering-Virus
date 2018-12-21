package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileCentralProcessor;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCentralProcessor extends SBlockModelSpecial implements ITileEntityProvider {

  public BlockCentralProcessor() {
    super(Material.iron);
    this.setBlockBounds(0, 0, 0, 1, 1 / 4f, 1);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileCentralProcessor();
  }

}
