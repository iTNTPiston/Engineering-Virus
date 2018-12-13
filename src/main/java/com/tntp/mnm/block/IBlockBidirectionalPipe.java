package com.tntp.mnm.block;

import com.tntp.mnm.api.TileEntityConnection;

import net.minecraft.world.World;

public interface IBlockBidirectionalPipe {
  /**
   * Used in pipe scan
   * 
   * @param world
   * @param tecon
   * @param comingFrom the side this pipe comes from
   * @param extra      extra information
   * @return The side that the pipe sticks into the final block
   */
  public int connectPipeToBlock(World world, TileEntityConnection tecon, int comingFrom, int extra);

  /**
   * Used in detect connection
   * 
   * @param world world
   * @param x     coord of the BLOCK to connect to
   * @param y
   * @param z
   * @param side  side of THIS PIPE that sticks out
   * @return true if the pipe can connect to that block
   */
  public boolean canConnectToBlock(World world, int x, int y, int z, int side);
}
