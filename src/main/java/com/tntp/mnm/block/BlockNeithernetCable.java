package com.tntp.mnm.block;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
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

}
