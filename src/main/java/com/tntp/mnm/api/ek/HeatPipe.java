package com.tntp.mnm.api.ek;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HeatPipe {
  public int x;
  public int y;
  public int z;

  public HeatPipe(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void writeToNBT(NBTTagCompound tag) {
    tag.setInteger("x", x);
    tag.setShort("y", (short) y);
    tag.setInteger("z", z);
  }

  public void readFromNBT(NBTTagCompound tag) {
    x = tag.getInteger("x");
    y = tag.getShort("y");
    z = tag.getInteger("z");
  }

  public IHeatNode getEnd(World world) {
    if (world == null)
      return null;
    if (!world.getChunkFromBlockCoords(x, z).isChunkLoaded)
      return null;
    TileEntity tile = world.getTileEntity(x, y, z);
    if (!(tile instanceof IHeatNode))
      return null;
    return (IHeatNode) tile;
  }
}
