package com.tntp.mnm.block;

import java.util.List;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNeithernetCable extends SBlockModelSpecial implements IBlockBidirectionalPipe {

  public BlockNeithernetCable() {
    super(Material.iron);
  }

  @Override
  public int connectPipeToBlock(World world, TileEntityConnection tecon, int comingFrom, int extra) {
    TileEntity te = world.getTileEntity(tecon.x, tecon.y, tecon.z);
    if (te instanceof STileNeithernet) {
      return ((STileNeithernet) te).connectPipe(tecon, comingFrom);
    }
    return -1;
  }

  @Override
  public boolean canConnectToBlock(World world, int x, int y, int z, int side) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof STileNeithernet) {
      if (((STileNeithernet) tile).getPortSide() == (side ^ 1))
        return true;
    }
    return false;
  }

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
    float minX = 0.25f, minY = 0.25f, minZ = 0.25f, maxX = 0.75f, maxY = 0.75f, maxZ = 0.75f;
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
  public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
    this.setBlockBoundsBasedOnState(world, x, y, z);
    super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    this.setBlockBoundsForItemRender();
  }

}
