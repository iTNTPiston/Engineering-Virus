package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class TileHeatEngineFirewall extends STileHeatNode implements IHeatSink {
  private static int MAX_RATE = 2, MAX_EK = 1000, MAX_BOOSTED = 3, EK_TO_MZ = 10;
  private boolean boosted;

  public TileHeatEngineFirewall() {
    setEK(0);
    setMaxEK(MAX_EK);
    boosted = false;
  }

  @Override
  public boolean isSinkSide(int side) {
    return side == DirUtil.DOWN_MY;
  }

  @Override
  public void rescanSubtypes() {

    int structure = 0;
    for (int xx = xCoord - 1; xx <= xCoord + 1; xx++) {
      for (int zz = zCoord - 1; zz <= zCoord + 1; zz++) {
        if (worldObj.getBlock(xx, yCoord, zz) == MNMBlocks.firewall) {
          structure++;
        }
      }
    }
    boosted = structure == 8;

  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setBoolean("boosted", boosted);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    boosted = tag.getBoolean("boosted");
  }

}
