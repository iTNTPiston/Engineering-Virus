package com.tntp.mnm.tileentity;

import javax.annotation.Nonnull;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

public class STileHeatNode extends STile implements IHeatNode {
  private static int RESCAN = 200;
  private int rescanTotal;
  private int rescanCD;
  private boolean rescaned;
  private HeatPipe[] in;
  private HeatPipe[] out;
  private int ek;
  private int maxEk;

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
      if (this instanceof IHeatSource)
        workAsSource();
    }
  }

  public void workAsSource() {
    boolean worked = false;
    for (int i = 0; i < 6; i++) {
      if (out[i] != null) {
        IHeatNode targetSink = out[i].getEnd(worldObj);
        if (targetSink instanceof IHeatSink) {
          if (transferToSink((IHeatSink) targetSink, i)) {
            worked = true;
            ((TileEntity) targetSink).markDirty();
          }
        } else {
          rescanCD = 0;
        }
      }
    }
    if (worked)
      markDirty();
  }

  public boolean transferToSink(IHeatSink sink, int sourceSide) {
    return false;
  }

  public void rescan() {
    // only server side
    if (!rescaned) {
      rescaned = true;
      if (this instanceof IHeatSource) {
        for (int i = 0; i < 6; i++) {
          if (!((IHeatSource) this).isSourceSide(i)) {
            out[i] = null;
          } else {
            if (out[i] == null) {
              HeatPipe p = new HeatPipe(xCoord, yCoord, zCoord);
              if (findHeatNode(p, i, 1, worldObj) != -1)
                out[i] = p;
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
              HeatPipe p = new HeatPipe(xCoord, yCoord, zCoord);
              if (findHeatNode(p, i, 2, worldObj) != -1)
                in[i] = p;
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
  public static int findHeatNode(@Nonnull HeatPipe pipe, int outSide, int toSink, World world) {
    ForgeDirection direction = ForgeDirection.getOrientation(outSide);
    pipe.x = pipe.x + direction.offsetX;
    pipe.y = pipe.y + direction.offsetY;
    pipe.z = pipe.z + direction.offsetZ;

    int comingFrom = outSide ^ 1;
    int endOutSide = -1;
    if (world.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      Block b = world.getBlock(pipe.x, pipe.y, pipe.z);
      if (b == MNMBlocks.heat_pipe) {
        endOutSide = BlockUtil.pipeScan(world, Integer.MAX_VALUE, 0, 0, pipe, comingFrom, toSink, MNMBlocks.heat_pipe);
      } else {
        TileEntity te = world.getTileEntity(pipe.x, pipe.y, pipe.z);
        if (te instanceof IHeatNode) {
          endOutSide = ((IHeatNode) te).connectPipe(pipe, comingFrom, toSink);
        }
      }
    }
    return endOutSide;
  }

  @Override
  public HeatPipe[] getIn() {
    return in;
  }

  @Override
  public HeatPipe[] getOut() {
    return out;
  }

  /**
   * 
   */
  @Override
  public int connectPipe(HeatPipe pipe, int comingFrom, int toSink) {
    boolean sink = (toSink & 1) == 1;
    boolean source = (toSink & 2) == 2;

    if (sink) {
      if (this instanceof IHeatSink) {
        if (((IHeatSink) this).isSinkSide(comingFrom))
          return comingFrom ^ 1;
      }
    }
    if (source) {
      if (this instanceof IHeatSource) {
        if (((IHeatSource) this).isSourceSide(comingFrom))
          return comingFrom ^ 1;
      }
    }
    return -1;
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

    tag.setInteger("ek", ek);
    tag.setInteger("ek_max", maxEk);
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

    ek = tag.getInteger("ek");
    maxEk = tag.getInteger("ek_max");
  }

  @Override
  public int getEK() {
    return ek;
  }

  @Override
  public int getMaxEK() {
    return maxEk;
  }

  @Override
  public void setEK(int ek) {
    this.ek = ek;
  }

  @Override
  public void setMaxEK(int ek) {
    maxEk = ek;
  }

  @Override
  public String getInventoryName() {
    return getBlockType().getUnlocalizedName() + ".name";
  }

}
