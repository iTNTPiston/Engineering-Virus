package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.IHeatSink;

public class TileGeoThermalSmelter extends STileHeatNode implements IHeatSink {

  @Override
  public boolean isSinkSide(int side) {
    if (worldObj != null) {
      return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == side;
    }
    return false;
  }

}
