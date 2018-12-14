package com.tntp.mnm.block;

import com.tntp.mnm.api.TileEntityConnection;

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

}
