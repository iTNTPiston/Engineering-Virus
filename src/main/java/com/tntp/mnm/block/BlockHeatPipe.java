package com.tntp.mnm.block;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.init.MNMBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHeatPipe extends SBlockModelSpecial {

  public BlockHeatPipe() {
    super(Material.iron, "blockHeatPipe");
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
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

  public static int forwardPipe(World world, int startX, int startY, int startZ, HeatPipe pipe, int comingFrom,
      int toSink) {
    if (pipe.x == startX && pipe.y == startY && pipe.z == startZ)
      return -1;
    if (startX == Integer.MAX_VALUE) {
      startX = pipe.x;
      startY = pipe.y;
      startZ = pipe.z;
    }
    int meta = world.getBlockMetadata(pipe.x, pipe.y, pipe.z);
    int sideCode = metaToSide(meta);
    if (!isConnected(sideCode, comingFrom)) {
      return -1;
    }
    comingFrom = otherSide(sideCode, comingFrom);
    forwardPipe(pipe, comingFrom);
    comingFrom = comingFrom ^ 1;
    if (world.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      Block b = world.getBlock(pipe.x, pipe.y, pipe.z);
      if (b == MNMBlocks.blockHeatPipe) {
        return forwardPipe(world, startX, startY, startZ, pipe, comingFrom, toSink);
      } else {
        TileEntity te = world.getTileEntity(pipe.x, pipe.y, pipe.z);
        if (te instanceof IHeatNode) {
          return ((IHeatNode) te).connectPipe(pipe, comingFrom, toSink);
        }
      }
    }
    return -1;

  }

  public static void forwardPipe(HeatPipe pipe, int toDirection) {
    ForgeDirection direction = ForgeDirection.getOrientation(toDirection);
    pipe.x += direction.offsetX;
    pipe.y += direction.offsetY;
    pipe.z += direction.offsetZ;
  }

  /**
   * Stable if both ends are connected to something (sink,source or pipe)
   * 
   * @param world
   * @param x
   * @param y
   * @param z
   * @return
   */
  public boolean isConnectionStable(World world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    if (meta == 0)
      return false;
    int sides = metaToSide(meta);
    return isEndStable(world, x, y, z, ForgeDirection.VALID_DIRECTIONS[sides & 15])
        && isEndStable(world, x, y, z, ForgeDirection.VALID_DIRECTIONS[sides >> 4]);
  }

  public boolean isEndStable(World world, int x, int y, int z, ForgeDirection dir) {
    int xx = x + dir.offsetX;
    int yy = y + dir.offsetY;
    int zz = z + dir.offsetZ;
    int toSide = dir.ordinal();
    if (world.getBlock(xx, yy, zz) == this) {
      int meta = world.getBlockMetadata(xx, yy, zz);
      int aSideCode = metaToSide(meta);
      if (isConnected(aSideCode, toSide ^ 1)) {
        return true;
      }
    } else {
      TileEntity tile = world.getTileEntity(xx, yy, zz);
      if (tile == null)
        return false;
      if (tile instanceof IHeatSink) {
        if (((IHeatSink) tile).isSinkSide(toSide ^ 1))
          return true;
      } else if (tile instanceof IHeatSource) {
        if (((IHeatSource) tile).isSourceSide(toSide ^ 1))
          return true;
      }
    }
    return false;
  }

  public void detectConnection(World world, int x, int y, int z, int flag) {
    int sideCode = 0;
    int foundSide = 0;
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      int xx = x + dir.offsetX;
      int yy = y + dir.offsetY;
      int zz = z + dir.offsetZ;
      int toSide = dir.ordinal();
      if (world.getBlock(xx, yy, zz) == this) {
        if (!isConnectionStable(world, xx, yy, zz)) {
          sideCode = sideCode << 4;
          sideCode += toSide;
          foundSide++;
          if (foundSide >= 2)
            break;
        }
      } else {
        boolean canConnect = false;
        TileEntity tile = world.getTileEntity(xx, yy, zz);
        if (tile instanceof IHeatSink) {
          if (((IHeatSink) tile).isSinkSide(toSide ^ 1))
            canConnect = true;
        } else if (tile instanceof IHeatSource) {
          if (((IHeatSource) tile).isSourceSide(toSide ^ 1))
            canConnect = true;
        }
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
      int meta = sideToMeta(sideCode);
      if (meta != world.getBlockMetadata(x, y, z))
        world.setBlockMetadataWithNotify(x, y, z, sideToMeta(sideCode), flag);
    }
  }

  /**
   * Lets the block know when one of its neighbor changes. Doesn't know which
   * neighbor changed (coordinates passed are
   * their own) Args: x, y, z, neighbor Block
   */
  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    if (!world.isRemote && !isConnectionStable(world, x, y, z)) {
      detectConnection(world, x, y, z, 2);
    }
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    if (!world.isRemote) {
      detectConnection(world, x, y, z, 3);
    }
  }

  /**
   * Sets the block's bounds for rendering it as an item
   */
  @Override
  public void setBlockBoundsForItemRender() {
    this.setBlockBounds(0, 0, 0, 1, 1, 1);
  }

  /**
   * Updates the blocks bounds based on its current state. Args: world, x, y, z
   */
  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
    int sides = metaToSide(world.getBlockMetadata(x, y, z));
    float minX = 5f / 16, minY = 5f / 16, minZ = 5f / 16, maxX = 11f / 16, maxY = 11f / 16, maxZ = 11f / 16;
    if (isConnected(sides, 0)) {
      minY = 0;
    }
    if (isConnected(sides, 1)) {
      maxY = 1;
    }
    if (isConnected(sides, 2)) {
      minZ = 0;
    }
    if (isConnected(sides, 3)) {
      maxZ = 1;
    }
    if (isConnected(sides, 4)) {
      minX = 0;
    }
    if (isConnected(sides, 5)) {
      maxX = 1;
    }
    this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
  }

}
