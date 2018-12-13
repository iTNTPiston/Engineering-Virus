package com.tntp.mnm.util;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.block.IBlockBidirectionalPipe;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockUtil {
  public static byte getRotationalMetaFromBlocks(World world, int x, int y, int z) {
    Block block = world.getBlock(x, y, z - 1);
    Block block1 = world.getBlock(x, y, z + 1);
    Block block2 = world.getBlock(x - 1, y, z);
    Block block3 = world.getBlock(x + 1, y, z);
    byte b0 = 3;

    if (block.isAir(world, x, y, z) && !block1.isAir(world, x, y, z)) {
      b0 = 3;
    }

    if (block1.isAir(world, x, y, z) && !block.isAir(world, x, y, z)) {
      b0 = 2;
    }

    if (block2.isAir(world, x, y, z) && !block3.isAir(world, x, y, z)) {
      b0 = 5;
    }

    if (block3.isAir(world, x, y, z) && !block2.isAir(world, x, y, z)) {
      b0 = 4;
    }
    return b0;
  }

  public static byte getRotationalMetaFromEntityPlacing(World world, int x, int y, int z, EntityLivingBase entity,
      ItemStack stack) {
    int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

    if (l == 0) {
      return 2;
    }

    if (l == 1) {
      return 5;
    }

    if (l == 2) {
      return 3;
    }

    if (l == 3) {
      return 4;
    }
    return -1;
  }

  /**
   * BDP util: convert meta data into an int that represents two sides
   * 
   * @param meta
   * @return
   */
  public static int pipeMetaToSide(int meta) {
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
    return (side0 << 4) + side1;
  }

  /**
   * BDP util: convert an int that represents two sides into metadata
   * 
   * @param sideCode
   * @return
   */
  public static int pipeSideToMeta(int sideCode) {
    return pipeSidesToMeta(sideCode >> 4, sideCode & 15);
  }

  /**
   * BDP util: convert two sides into the metadata
   * 
   * @param side0
   * @param side1
   * @return
   */
  public static int pipeSidesToMeta(int side0, int side1) {
    if (side0 == side1)
      return 0;
    if (side0 > side1) {
      return pipeSidesToMeta(side1, side0);
    }
    int add = 5;
    int meta = 0;
    for (int i = 0; i < side0; i++) {
      meta += add;
      add--;
    }
    meta += side1 - side0;
    return meta;
  }

  /**
   * BDP util: Get the other side given the two sides and one side
   * 
   * @param sidesCode
   * @param oneSide
   * @return
   */
  public static int pipeOtherSide(int sidesCode, int oneSide) {
    int side0 = sidesCode >> 4;
    if (side0 == oneSide) {
      return sidesCode & 15;
    } else {
      return side0;
    }
  }

  /**
   * BDP util: Check if a side is contained in the two sides
   * 
   * @param sidesCode
   * @param side
   * @return
   */
  public static boolean pipeIsConnectedToSide(int sidesCode, int side) {
    if (sidesCode == 0)
      return false;
    return (sidesCode >> 4) == side || (sidesCode & 15) == side;
  }

  /**
   * Move pipe forward
   * 
   * @param pipe
   * @param toDirection
   */
  public static void pipeForward(TileEntityConnection pipe, int toDirection) {
    int[] offset = DirUtil.OFFSETS[toDirection];
    pipe.x += offset[0];
    pipe.y += offset[1];
    pipe.z += offset[2];
  }

  /**
   * Recursively scan the other end of this pipe.
   * 
   * @param world
   * @param startX
   * @param startY
   * @param startZ
   * @param pipe
   * @param comingFrom
   * @param toSink
   * @param pipeType
   * @return
   */
  public static int pipeScan(World world, int startX, int startY, int startZ, TileEntityConnection pipe, int comingFrom,
      int toSink, IBlockBidirectionalPipe pipeType) {
    // pre-condition is the pipe position must be on a block that is heat pipe
    if (pipe.x == startX && pipe.y == startY && pipe.z == startZ)
      return -1;// prevent loop
    if (startX == Integer.MAX_VALUE) {
      // Init
      startX = pipe.x;
      startY = pipe.y;
      startZ = pipe.z;
    }
    int meta = world.getBlockMetadata(pipe.x, pipe.y, pipe.z);
    int sideCode = BlockUtil.pipeMetaToSide(meta);
    if (!BlockUtil.pipeIsConnectedToSide(sideCode, comingFrom)) {
      return -1;// If this pipe is not connected to the previous one,
      // connection fail
    }
    // update coming from
    comingFrom = BlockUtil.pipeOtherSide(sideCode, comingFrom);
    BlockUtil.pipeForward(pipe, comingFrom);
    comingFrom = comingFrom ^ 1;

    // Attempt to connect or forward
    if (world.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      Block b = world.getBlock(pipe.x, pipe.y, pipe.z);
      if (b == pipeType) {
        return pipeScan(world, startX, startY, startZ, pipe, comingFrom, toSink, pipeType);
      } else {
        return pipeType.connectPipeToBlock(world, pipe, comingFrom, toSink);
      }
    }
    return -1;
  }

  /**
   * Stable if both ends are connected to something
   * 
   * @param world
   * @param x
   * @param y
   * @param z
   * @return
   */
  public static boolean pipeConnectionStable(World world, int x, int y, int z, IBlockBidirectionalPipe pipeType) {
    int meta = world.getBlockMetadata(x, y, z);
    if (meta == 0)
      return false;
    int sides = BlockUtil.pipeMetaToSide(meta);
    return pipeEndStable(world, x, y, z, sides & 15, pipeType) && pipeEndStable(world, x, y, z, sides >> 4, pipeType);
  }

  public static boolean pipeEndStable(World world, int x, int y, int z, int dir, IBlockBidirectionalPipe pipeType) {
    int[] offset = DirUtil.OFFSETS[dir];
    int xx = x + offset[0];
    int yy = y + offset[1];
    int zz = z + offset[2];
    int toSide = dir;
    if (world.getBlock(xx, yy, zz) == pipeType) {
      int meta = world.getBlockMetadata(xx, yy, zz);
      int aSideCode = BlockUtil.pipeMetaToSide(meta);
      if (BlockUtil.pipeIsConnectedToSide(aSideCode, toSide ^ 1)) {
        return true;
      }
    } else {
      if (pipeType.canConnectToBlock(world, xx, yy, zz, toSide))
        return true;
    }
    return false;
  }

  /**
   * Detect connection of the pipe
   * 
   * @param world
   * @param x
   * @param y
   * @param z
   * @param flag
   * @param pipeType
   */
  public static void pipeDetectConnection(World world, int x, int y, int z, int flag,
      IBlockBidirectionalPipe pipeType) {
    int sideCode = 0;
    int foundSide = 0;
    for (int d = 0; d < 6; d++) {
      int[] dirOffset = DirUtil.OFFSETS[d];
      int xx = x + dirOffset[0];
      int yy = y + dirOffset[1];
      int zz = z + dirOffset[2];
      int toSide = d;
      if (world.getBlock(xx, yy, zz) == pipeType) {
        if (!pipeConnectionStable(world, xx, yy, zz, pipeType)) {
          sideCode = sideCode << 4;
          sideCode += toSide;
          foundSide++;
          if (foundSide >= 2)
            break;
        }
      } else {
        boolean canConnect = pipeType.canConnectToBlock(world, xx, yy, zz, toSide);
        if (canConnect) {
          sideCode = sideCode << 4;
          sideCode += toSide;
          foundSide++;
          if (foundSide >= 2)
            break;
        }
      }
    }
    if (foundSide == 1) {
      int toSide = sideCode ^ 1;
      sideCode = sideCode << 4;
      sideCode += toSide;
      foundSide++;
    }
    if (foundSide == 2) {
      int meta = BlockUtil.pipeSideToMeta(sideCode);
      if (meta != world.getBlockMetadata(x, y, z))
        world.setBlockMetadataWithNotify(x, y, z, BlockUtil.pipeSideToMeta(sideCode), flag);
    }
  }
}
