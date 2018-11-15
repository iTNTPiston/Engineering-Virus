//package com.tntp.mnm.tileentity;
//
//import com.tntp.mnm.api.ek.HeatPipe;
//import com.tntp.mnm.api.ek.IHeatConnector;
//import com.tntp.mnm.api.ek.IHeatNode;
//import com.tntp.mnm.api.ek.IHeatSink;
//import com.tntp.mnm.api.ek.IHeatSource;
//
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraftforge.common.util.ForgeDirection;
//
//public class HeatConnectorImpl extends TileEntity {
//  int[] sides;
//
//  public HeatConnectorImpl() {
//    sides = new int[2];
//    sides[0] = -1;
//    sides[1] = -1;
//  }
//
//  public void updateEntity() {
//    if (worldObj != null && !worldObj.isRemote) {
//      if (sides[0] == -1 && sides[1] == -1) {
//        int i = 0;
//        for (int dir = 0; dir < 6 && i < 2; dir++) {
//          ForgeDirection direction = ForgeDirection.getOrientation(dir);
//          int xx = xCoord + direction.offsetX;
//          int yy = yCoord + direction.offsetY;
//          int zz = zCoord + direction.offsetZ;
//          int diro = dir ^ 1;
//          if (worldObj.getChunkFromBlockCoords(xx, zz).isChunkLoaded) {
//            TileEntity te = worldObj.getTileEntity(xx, yy, zz);
//            if (te instanceof IHeatConnector) {
//              IHeatConnector hc = (IHeatConnector) te;
//              if (hc.canConnectToSide(diro)) {
//                sides[i++] = dir;
//              }
//            } else if (te instanceof IHeatSink) {
//              if (((IHeatSink) te).isSinkSide(diro)) {
//                sides[i++] = dir;
//              }
//            } else if (te instanceof IHeatSource) {
//              if (((IHeatSource) te).isSourceSide(diro)) {
//                sides[i++] = dir;
//              }
//            }
//          }
//        }
//      }
//    }
//  }
//
//  public void setSides(int s1, int s2) {
//    sides[0] = s1;
//    sides[1] = s2;
//  }
//
//  public boolean forwardPipe(HeatPipe pipe, int comingFrom, boolean toSink) {
//    boolean forwarded = false;
//    int i = 0;
//    for (; i < 2; i++) {
//      if (comingFrom == sides[i]) {
//        forwardPipe(pipe, i, sides);
//        forwarded = true;
//        break;
//      }
//    }
//    if (!forwarded)
//      return false;
//    comingFrom = sides[i ^ 1] ^ 1;
//    if (worldObj.getChunkFromBlockCoords(pipe.x, pipe.z).isChunkLoaded) {
//      TileEntity te = worldObj.getTileEntity(pipe.x, pipe.y, pipe.z);
//      if (te instanceof IHeatConnector) {
//        return ((IHeatConnector) te).forwardPipe(pipe, comingFrom, toSink);
//      } else if ((toSink && te instanceof IHeatSink) || (!toSink && te instanceof IHeatSource)) {
//        return ((IHeatNode) te).connectPipe(pipe, comingFrom, toSink);
//      }
//    }
//    return false;
//
//  }
//
//  public static void forwardPipe(HeatPipe pipe, int fromSideIndex, int[] connectingSides) {
//    ForgeDirection direction = ForgeDirection.getOrientation(connectingSides[fromSideIndex ^ 1]);
//    pipe.x += direction.offsetX;
//    pipe.y += direction.offsetY;
//    pipe.z += direction.offsetZ;
//  }
//
//  public void writeToNBT(NBTTagCompound tag) {
//    super.writeToNBT(tag);
//    tag.setByte("side0", (byte) sides[0]);
//    tag.setByte("side1", (byte) sides[1]);
//  }
//
//  public void readFromNBT(NBTTagCompound tag) {
//    super.readFromNBT(tag);
//    sides = new int[2];
//    sides[0] = tag.getByte("side0");
//    sides[1] = tag.getByte("side1");
//  }
//
//  public void rotateSides() {
//
//  }
//
//}
