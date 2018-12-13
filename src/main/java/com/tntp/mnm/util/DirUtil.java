package com.tntp.mnm.util;

public class DirUtil {
  public static int DOWN_MY = 0;
  public static int UP_PY = 1;
  public static int NORTH_MZ = 2;
  public static int SOUTH_PZ = 3;
  public static int WEST_MX = 4;
  public static int EAST_PX = 5;
  public static int[] ALL_DIR = { 0, 1, 2, 3, 4, 5 };
  public static int[][] OFFSETS = { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 }, { -1, 0, 0 }, { 1, 0, 0 } };
}
