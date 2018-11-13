package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileCentralProcessor;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCentralProcessor extends SBlockModelSpecial implements ITileEntityProvider {

  public BlockCentralProcessor() {
    super(Material.iron, "blockCentralProcessor");
    // TODO Auto-generated constructor stub
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileCentralProcessor();
  }

}
