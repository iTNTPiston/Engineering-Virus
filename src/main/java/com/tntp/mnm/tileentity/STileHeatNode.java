package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatConnector;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class STileHeatNode extends STile implements IHeatNode {
  private static int RESCAN = 200;
  private int rescanTotal;
  private int rescanCD;
  private boolean rescaned;
  private HeatPipe[] in;
  private HeatPipe[] out;

  public STileHeatNode() {
    in = new HeatPipe[6];
    out = new HeatPipe[6];
    rescanTotal = RESCAN;
  }

  public void updateEntity() {
    if (worldObj != null && !worldObj.isRemote) {
      if (rescaned) {
        rescanCD = rescanTotal;
        rescaned = false;
      }
      if (rescanCD == 0) {
        rescan();
        markDirty();
      } else {
        rescanCD--;
      }
    }
  }

  public void rescan() {
    if (!rescaned) {
      rescaned = true;
      if (this instanceof IHeatSource) {
        for (int i = 0; i < 6; i++) {
          if (!((IHeatSource) this).isSourceSide(i)) {
            out[i] = null;
          } else {
            if (out[i] == null) {
              out[i] = findHeatNode(i, true);
            }
          }
        }
      }
      if (this instanceof IHeatSink) {
        for (int i = 0; i < 6; i++) {
          if (!((IHeatSink) this).isSinkSide(i)) {
            in[i] = null;
          } else {
            if (in[i] == null) {
              in[i] = findHeatNode(i, false);
            }
          }
        }
      }
    }

  }

  /**
   * Find a heat node following the sides and fill the pipe
   * 
   * @param findSink
   * @param pipe
   */
  private HeatPipe findHeatNode(int outSide, boolean toSink) {
    ForgeDirection direction = ForgeDirection.getOrientation(outSide);
    HeatPipe pipe = new HeatPipe(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
    int comingFrom = outSide ^ 1;
    boolean successful = false;
    if (worldObj.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      TileEntity te = worldObj.getTileEntity(pipe.x, pipe.y, pipe.z);
      if (te instanceof IHeatConnector) {
        successful = ((IHeatConnector) te).forwardPipe(pipe, comingFrom, toSink);
      } else if ((toSink && te instanceof IHeatSink) || (!toSink && te instanceof IHeatSource)) {
        successful = ((IHeatNode) te).connectPipe(pipe, comingFrom, toSink);
      }
    }
    if (successful)
      return pipe;
    else
      return null;
  }

  @Override
  public HeatPipe[] getIn() {
    return in;
  }

  @Override
  public HeatPipe[] getOut() {
    return out;
  }

  @Override
  public boolean connectPipe(HeatPipe pipe, int comingFrom, boolean toSink) {
    if (toSink) {
      if (((IHeatSink) this).isSinkSide(comingFrom)) {
        return true;
      }
    } else {
      if (((IHeatSource) this).isSourceSide(comingFrom)) {
        return true;
      }
    }
    return false;
  }

}
