package com.tntp.mnm.api.neither;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.tileentity.STileNeithernet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class NeitherPipe extends TileEntityConnection {

  public NeitherPipe(int x, int y, int z) {
    super(x, y, z);
  }

  public STileNeithernet getEnd(World world) {
    TileEntity tile = super.getTileEntity(world);
    if (!(tile instanceof STileNeithernet))
      return null;
    return (STileNeithernet) tile;
  }

}
