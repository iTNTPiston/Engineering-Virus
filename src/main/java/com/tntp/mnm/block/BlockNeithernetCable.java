package com.tntp.mnm.block;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockNeithernetCable extends SBlockModelSpecial implements IBlockBidirectionalPipe {

  public BlockNeithernetCable() {
    super(Material.iron, "neithernetCable");
  }

  @Override
  public int connectPipeToBlock(World world, TileEntityConnection tecon, int comingFrom, int extra) {
    return -1;
  }

  @Override
  public boolean canConnectToBlock(World world, int x, int y, int z, int side) {
    return false;
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    if (!world.isRemote && !BlockUtil.pipeConnectionStable(world, x, y, z, this)) {
      BlockUtil.pipeDetectConnection(world, x, y, z, 2, this);
    }
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    if (!world.isRemote) {
      BlockUtil.pipeDetectConnection(world, x, y, z, 3, this);
    }
  }

}
