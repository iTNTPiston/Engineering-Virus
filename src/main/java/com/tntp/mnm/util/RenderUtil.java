package com.tntp.mnm.util;

import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderUtil {
  public static Vertex rotate(Vertex v, float rad, int axis, Vertex vObj) {
    if (axis < 0) {
      return v;
    }
    if (axis == DirUtil.DOWN_MY) {
      axis = DirUtil.UP_PY;
      rad = -rad;
    } else if (axis == DirUtil.NORTH_MZ) {
      axis = DirUtil.SOUTH_PZ;
      rad = -rad;
    } else if (axis == DirUtil.WEST_MX) {
      axis = DirUtil.EAST_PX;
      rad = -rad;
    }
    if (vObj == null) {
      vObj = new Vertex(0, 0, 0);
    }
    float cos = (float) MathHelper.cos(rad);
    float sin = (float) MathHelper.sin(rad);
    if (axis == DirUtil.EAST_PX) {
      vObj.x = v.x;
      vObj.y = cos * v.y - sin * v.z;
      vObj.z = sin * v.y + cos * v.z;
    } else if (axis == DirUtil.UP_PY) {
      vObj.x = cos * v.x + sin * v.z;
      vObj.y = v.y;
      vObj.z = -sin * v.x + cos * v.z;
    } else if (axis == DirUtil.SOUTH_PZ) {
      vObj.x = cos * v.x - sin * v.y;
      vObj.y = sin * v.x + cos * v.y;
      vObj.z = v.z;
    }
    return vObj;
  }
}
