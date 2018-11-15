package com.tntp.mnm.block;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.init.MNMBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHeatPipe extends SBlock {

  public BlockHeatPipe(Material mat, String regName) {
    super(mat, regName);
    // TODO Auto-generated constructor stub
  }

  public static int metaToSide(int meta) {
    int side0 = 0;
    int side1 = 0;
    int add = 5;
    int added = 0;
    while (add > 0) {
      added += add;
      if (meta <= added) {
        side1 = meta - added + add + side0;
        break;
      } else {
        add--;
        side0++;
      }
    }
    System.out.println(meta + " " + side0 + " " + side1);
    return (side0 << 4) + side1;
  }

  public static int sideToMeta(int sideCode) {
    return sidesToMeta(sideCode >> 4, sideCode & 15);
  }

  public static int sidesToMeta(int side0, int side1) {
    if (side0 == side1)
      return 0;
    if (side0 > side1) {
      return sidesToMeta(side1, side0);
    }
    int add = 5;
    int meta = 0;
    for (int i = 0; i < side0; i++) {
      meta += add;
      add--;
    }
    meta += side1 - side0;
    System.out.println(meta + " " + side0 + " " + side1);
    return meta;
  }

  public static boolean isConnected(int sidesCode, int side) {
    if (sidesCode == 0)
      return false;
    return (sidesCode >> 4) == side || (sidesCode & 15) == side;
  }

  public static int otherSide(int sidesCode, int oneSide) {
    int side0 = sidesCode >> 4;
    if (side0 == oneSide) {
      return sidesCode & 15;
    } else {
      return side0;
    }
  }

  public static boolean forwardPipe(World world, HeatPipe pipe, int comingFrom, boolean toSink) {

    int meta = world.getBlockMetadata(pipe.x, pipe.y, pipe.z);
    int sideCode = metaToSide(meta);
    if (!isConnected(sideCode, comingFrom)) {
      return false;
    }
    comingFrom = otherSide(sideCode, comingFrom);
    forwardPipe(pipe, comingFrom);
    comingFrom = comingFrom ^ 1;
    if (world.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      Block b = world.getBlock(pipe.x, pipe.y, pipe.z);
      if (b == MNMBlocks.blockHeatPipe) {
        return forwardPipe(world, pipe, comingFrom, toSink);
      } else {
        TileEntity te = world.getTileEntity(pipe.x, pipe.y, pipe.z);
        if ((toSink && te instanceof IHeatSink) || (!toSink && te instanceof IHeatSource)) {
          return ((IHeatNode) te).connectPipe(pipe, comingFrom, toSink);
        }
      }
    }
    return false;

  }

  public static void forwardPipe(HeatPipe pipe, int toDirection) {
    ForgeDirection direction = ForgeDirection.getOrientation(toDirection);
    pipe.x += direction.offsetX;
    pipe.y += direction.offsetY;
    pipe.z += direction.offsetZ;
  }

}
