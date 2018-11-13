package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatConnector;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.Constants.NBT;

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

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setInteger("rescan_total", rescanTotal);
    tag.setInteger("rescan_cd", rescanCD);
    tag.setBoolean("rescaned", rescaned);
    NBTTagList inList = new NBTTagList();
    for (int i = 0; i < 6; i++) {
      if (in[i] != null) {
        NBTTagCompound pipe = new NBTTagCompound();
        pipe.setByte("index", (byte) i);
        in[i].writeToNBT(pipe);
        inList.appendTag(pipe);
      }
    }
    NBTTagList outList = new NBTTagList();
    for (int i = 0; i < 6; i++) {
      if (out[i] != null) {
        NBTTagCompound pipe = new NBTTagCompound();
        pipe.setByte("index", (byte) i);
        out[i].writeToNBT(pipe);
        outList.appendTag(pipe);
      }
    }
    tag.setTag("heatpipe_in", inList);
    tag.setTag("heatpipe_out", outList);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    rescanTotal = tag.getInteger("rescan_total");
    rescanCD = tag.getInteger("rescan_cd");
    rescaned = tag.getBoolean("rescaned");
    NBTTagList inList = tag.getTagList("heatpipe_in", NBT.TAG_COMPOUND);
    in = new HeatPipe[6];
    for (int i = 0; i < inList.tagCount(); i++) {
      NBTTagCompound pipe = inList.getCompoundTagAt(i);
      HeatPipe p = new HeatPipe(0, 0, 0);
      p.readFromNBT(pipe);
      int index = pipe.getByte("index");
      in[index] = p;
    }
    NBTTagList outList = tag.getTagList("heatpipe_out", NBT.TAG_COMPOUND);
    out = new HeatPipe[6];
    for (int i = 0; i < outList.tagCount(); i++) {
      NBTTagCompound pipe = outList.getCompoundTagAt(i);
      HeatPipe p = new HeatPipe(0, 0, 0);
      p.readFromNBT(pipe);
      int index = pipe.getByte("index");
      out[index] = p;
    }
  }

}
