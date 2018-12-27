package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.gui.structure.ITileStructure;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class TileHeatCollectorFirewall extends STileHeatNode implements IHeatSource, ITileStructure {
  private static int BASE = 4, BOOST = 2, MAX = 1000;
  private int rate;

  public TileHeatCollectorFirewall() {
    setEK(0);
    setMaxEK(MAX);
    rate = 0;
  }

  @Override
  public boolean isSourceSide(int side) {
    return side == DirUtil.UP_PY;
  }

  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (rate > 0) {
        setEK(Math.min(getEK() + rate, getMaxEK()));
        markDirty();
      }
    }
  }

  @Override
  public void rescanSubtypes() {

    if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != Blocks.fire) {
      rate = 0;
    } else {
      int structure = 0;
      for (int xx = xCoord - 1; xx <= xCoord + 1; xx++) {
        for (int zz = zCoord - 1; zz <= zCoord + 1; zz++) {
          if (worldObj.getBlock(xx, yCoord, zz) == MNMBlocks.firewall) {
            structure++;
          }
        }
      }
      rate = BASE + structure * BOOST;
    }
  }

  @Override
  public boolean transferToSink(IHeatSink sink, int sourceSide) {
    int transfer = Math.min(rate, sink.getMaxEK() - sink.getEK());
    if (transfer > 0) {
      setEK(getEK() - transfer);
      sink.setEK(sink.getEK() + transfer);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setInteger("ek_rate", rate);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    rate = tag.getInteger("ek_rate");
  }

  @Override
  public String getStructureGui() {
    return "GuiStructureHeatCollectorFirewall";
  }

}
