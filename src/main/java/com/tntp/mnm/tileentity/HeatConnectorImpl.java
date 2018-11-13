package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatConnector;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.ek.IHeatSink;
import com.tntp.mnm.api.ek.IHeatSource;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class HeatConnectorImpl extends TileEntity implements IHeatConnector {
  int[] sides;

  @Override
  public boolean forwardPipe(HeatPipe pipe, int comingFrom, boolean toSink) {
    boolean forwarded = false;
    int i = 0;
    for (; i < 2; i++) {
      if (comingFrom == sides[i]) {
        forwardPipe(pipe, i, sides);
        forwarded = true;
        break;
      }
    }
    if (!forwarded)
      return false;
    comingFrom = sides[i ^ 1] ^ 1;
    if (worldObj.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
      TileEntity te = worldObj.getTileEntity(pipe.x, pipe.y, pipe.z);
      if (te instanceof IHeatConnector) {
        return ((IHeatConnector) te).forwardPipe(pipe, comingFrom, toSink);
      } else if ((toSink && te instanceof IHeatSink) || (!toSink && te instanceof IHeatSource)) {
        return ((IHeatNode) te).connectPipe(pipe, comingFrom, toSink);
      }
    }
    return false;

  }

  public static void forwardPipe(HeatPipe pipe, int fromSideIndex, int[] connectingSides) {
    ForgeDirection direction = ForgeDirection.getOrientation(connectingSides[fromSideIndex ^ 1]);
    pipe.x += direction.offsetX;
    pipe.y += direction.offsetY;
    pipe.z += direction.offsetZ;
  }

  public static void notifyConnectionChange(World world, int x, int y, int z) {

  }

}
