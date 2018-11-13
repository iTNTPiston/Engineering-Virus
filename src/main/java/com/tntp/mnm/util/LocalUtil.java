package com.tntp.mnm.util;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class LocalUtil {
  public static List<String> localizeList(String key, Object... objects) {
    ArrayList<String> list = new ArrayList<String>();
    for (int i = 0;; i++) {
      String k = key + i;
      String loc = localize(k, objects);
      if (k.equals(loc))
        break;
      list.add(loc);
    }
    return list;
  }

  public static String localize(String key) {
    return localize(key, new Object[0]);
  }

  public static String localize(String key, Object... objects) {
    return I18n.format(key, objects);
  }
}
