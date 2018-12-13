package com.tntp.mnm.api.ek;

import com.tntp.mnm.api.TileEntityConnection;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HeatPipe extends TileEntityConnection {

  public HeatPipe(int x, int y, int z) {
    super(x, y, z);
  }

  public IHeatNode getEnd(World world) {
    TileEntity tile = super.getTileEntity(world);
    if (!(tile instanceof IHeatNode))
      return null;
    return (IHeatNode) tile;
  }
}
