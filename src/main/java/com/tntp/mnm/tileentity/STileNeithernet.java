package com.tntp.mnm.tileentity;

import java.util.List;

import javax.annotation.Nonnull;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.api.neither.NeitherPipe;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.BlockUtil;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Neithernet tiles are connected 1-to-1
 * 
 * @author iTNTPiston
 *
 */
public class STileNeithernet extends STile {
  private NeitherPipe pipe;
  private static int RESCAN = 200;
  private int rescanTotal;
  private int rescanCD;
  private boolean rescaned;

  public STileNeithernet() {
    rescanTotal = RESCAN;
  }

  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (rescaned) {
        rescanCD = rescanTotal;
        rescaned = false;
      }
      if (rescanCD <= 0) {
        rescanNeither();
        markDirty();
      } else {
        rescanCD--;
      }
    }
  }

  public int getPortSide() {
    return this.getBlockMetadata();
  }

  public NeitherPipe getPipe() {
    return pipe;
  }

  public void rescanNeither() {
    // only server side
    if (!rescaned) {
      rescaned = true;
      int portSide = getPortSide();
      pipe = null;
      NeitherPipe p = new NeitherPipe(xCoord, yCoord, zCoord);
      if (findNeither(p, portSide, worldObj) != -1) {
        pipe = p;
      }
      System.out.println(pipe);
    }
  }

  /**
   * Find a heat node following the sides and fill the pipe
   * 
   * @param findSink
   * @param pipe
   */
  public static int findNeither(@Nonnull NeitherPipe pipe, int outSide, World world) {
    int[] offset = DirUtil.OFFSETS[outSide];
    pipe.x = pipe.x + offset[0];
    pipe.y = pipe.y + offset[1];
    pipe.z = pipe.z + offset[2];

    int comingFrom = outSide ^ 1;
    int endOutSide = -1;
    if (world.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      Block b = world.getBlock(pipe.x, pipe.y, pipe.z);
      if (b == MNMBlocks.neithernet_cable) {
        endOutSide = BlockUtil.pipeScan(world, Integer.MAX_VALUE, 0, 0, pipe, comingFrom, 0,
            MNMBlocks.neithernet_cable);
      }
    }
    return endOutSide;
  }

  public int connectPipe(TileEntityConnection tecon, int comingFrom) {
    if (comingFrom == getPortSide())
      return comingFrom ^ 1;
    return -1;
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setInteger("rescan_total", rescanTotal);
    tag.setInteger("rescan_cd", rescanCD);
    tag.setBoolean("rescaned", rescaned);
    tag.setBoolean("has_pipe", pipe != null);
    if (pipe != null) {
      NBTTagCompound pipeTag = new NBTTagCompound();
      pipe.writeToNBT(pipeTag);
      tag.setTag("pipe", pipeTag);
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    rescanTotal = tag.getInteger("rescan_total");
    rescanCD = tag.getInteger("rescan_cd");
    rescaned = tag.getBoolean("rescaned");
    if (tag.getBoolean("has_pipe")) {
      NBTTagCompound pipeTag = tag.getCompoundTag("pipe");
      pipe = new NeitherPipe(0, 0, 0);
      pipe.readFromNBT(pipeTag);
    }
  }

  /**
   * Exclude intermediate connection such as routers, which should override
   * this<br>
   * Ports must also override this
   * 
   * @param list
   */
  public void addFinalTilesTo(List<STileNeithernet> list) {
    list.add(this);
  }

  /**
   * Return true if this tile is not part of a router or port
   * 
   * @return
   */
  public boolean isFinalTile() {
    return true;
  }

  public Mainframe connectToMainframe() {
    if (pipe != null) {
      STileNeithernet tile = pipe.getEnd(worldObj);
      if (tile != null && !tile.isFinalTile()) {// cannot connect 2 final tiles
        return tile.connectToMainframe();
      }
    }
    return null;
  }

}
