package com.tntp.mnm.util;

import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderUtil {
  public static Vertex rotate(Vertex v, float rad, ForgeDirection axis, Vertex vObj) {
    if (axis == null) {
      return v;
    }
    if (axis == ForgeDirection.DOWN) {
      axis = ForgeDirection.UP;
      rad = -rad;
    } else if (axis == ForgeDirection.NORTH) {
      axis = ForgeDirection.SOUTH;
      rad = -rad;
    } else if (axis == ForgeDirection.WEST) {
      axis = ForgeDirection.EAST;
      rad = -rad;
    }
    if (vObj == null) {
      vObj = new Vertex(0, 0, 0);
    }
    float cos = (float) Math.cos(rad);
    float sin = (float) Math.sin(rad);
    if (axis == ForgeDirection.EAST) {
      vObj.x = v.x;
      vObj.y = cos * v.y - sin * v.z;
      vObj.z = sin * v.y + cos * v.z;
    } else if (axis == ForgeDirection.UP) {
      vObj.x = cos * v.x + sin * v.z;
      vObj.y = v.y;
      vObj.z = -sin * v.x + cos * v.z;
    } else if (axis == ForgeDirection.SOUTH) {
      vObj.x = cos * v.x - sin * v.y;
      vObj.y = sin * v.x + cos * v.y;
      vObj.z = v.z;
    }
    return vObj;
  }
}
