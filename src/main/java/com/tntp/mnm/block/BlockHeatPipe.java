package com.tntp.mnm.block;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.BlockUtil;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHeatPipe extends SBlockModelSpecial implements IBlockBidirectionalPipe {

  public BlockHeatPipe() {
    super(Material.iron, "heatPipe");
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  /**
   * Lets the block know when one of its neighbor changes. Doesn't know which
   * neighbor changed (coordinates passed are
   * their own) Args: x, y, z, neighbor Block
   */
  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    if (!world.isRemote && !BlockUtil.pipeConnectionStable(world, x, y, z, this)) {
      BlockUtil.pipeDetectConnection(world, x, y, z, 2, this);
    }
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    if (!world.isRemote) {
      BlockUtil.pipeDetectConnection(world, x, y, z, 3, this);
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
    int sides = BlockUtil.pipeMetaToSide(world.getBlockMetadata(x, y, z));
    float minX = 5f / 16, minY = 5f / 16, minZ = 5f / 16, maxX = 11f / 16, maxY = 11f / 16, maxZ = 11f / 16;
    if (BlockUtil.pipeIsConnectedToSide(sides, 0)) {
      minY = 0;
    }
    if (BlockUtil.pipeIsConnectedToSide(sides, 1)) {
      maxY = 1;
    }
    if (BlockUtil.pipeIsConnectedToSide(sides, 2)) {
      minZ = 0;
    }
    if (BlockUtil.pipeIsConnectedToSide(sides, 3)) {
      maxZ = 1;
    }
    if (BlockUtil.pipeIsConnectedToSide(sides, 4)) {
      minX = 0;
    }
    if (BlockUtil.pipeIsConnectedToSide(sides, 5)) {
      maxX = 1;
    }
    this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
  }

  @Override
  public int connectPipeToBlock(World world, TileEntityConnection tecon, int comingFrom, int extra) {
    TileEntity te = world.getTileEntity(tecon.x, tecon.y, tecon.z);
    if (te instanceof IHeatNode) {
      return ((IHeatNode) te).connectPipe((HeatPipe) tecon, comingFrom, extra);
    }
    return -1;
  }

  @Override
  public boolean canConnectToBlock(World world, int x, int y, int z, int side) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof IHeatSink) {
      if (((IHeatSink) tile).isSinkSide(side ^ 1))
        return true;
    } else if (tile instanceof IHeatSource) {
      if (((IHeatSource) tile).isSourceSide(side ^ 1))
        return true;
    }
    return false;
  }

}
