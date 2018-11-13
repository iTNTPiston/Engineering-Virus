package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.init.Blocks;

public class TileHeatCollectorFirewall extends STileHeatNode implements IHeatSource {
  private static int BASE = 4, BOOST = 2, MAX = 1000;
  private int rate;

  public TileHeatCollectorFirewall() {
    setEK(0);
    setMaxEK(MAX);
    rate = 0;
  }

  @Override
  public boolean isSourceSide(int side) {
    return side == DirUtil.D_U_PY;
  }

  @Override
  public void rescan() {
    super.rescan();
    if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != Blocks.fire) {
      rate = 0;
    } else {
      int structure = 0;
      for (int xx = xCoord - 1; xx <= xCoord + 1; xx++) {
        for (int zz = zCoord - 1; zz <= zCoord + 1; zz++) {
          if (worldObj.getBlock(xx, yCoord, zz) == MNMBlocks.blockFirewall) {
            structure++;
          }
        }
      }
      rate = BASE + structure * BOOST;
    }
  }

  @Override
  public boolean transferToSink(IHeatSink sink) {
    int transfer = Math.min(rate, sink.getMaxEK() - sink.getEK());
    if (transfer > 0) {
      setEK(getEK() - transfer);
      sink.setEK(sink.getEK() + transfer);
      return true;
    } else {
      return false;
    }
  }

}
