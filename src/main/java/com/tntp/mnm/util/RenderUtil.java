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
    float x = v.x;
    float y = v.y;
    float z = v.z;
    if (axis == DirUtil.EAST_PX) {
      vObj.x = x;
      vObj.y = cos * y - sin * z;
      vObj.z = sin * y + cos * z;
    } else if (axis == DirUtil.UP_PY) {
      vObj.x = cos * x + sin * z;
      vObj.y = y;
      vObj.z = -sin * x + cos * z;
    } else if (axis == DirUtil.SOUTH_PZ) {
      vObj.x = cos * x - sin * y;
      vObj.y = sin * x + cos * y;
      vObj.z = z;
    }
    return vObj;
  }

  public static int argb(int a, int r, int g, int b) {
    return (a << 24) + (r << 16) + (g << 8) + b;
  }
}
