package com.tntp.mnm.init;

import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.item.ItemToolBag;
import com.tntp.mnm.item.ItemWrench;
import com.tntp.mnm.item.tools.ItemEKToolUniversal;
import com.tntp.mnm.item.tools.ItemMeterStick;
import com.tntp.mnm.item.tools.ItemToolUniversal;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("metalnetworkmainframe")
public class MNMItems {
  public static final ItemWrench itemWrench = null;
  public static final ItemToolBag itemToolBag = null;
  public static final SItemTool itemEKToolUniversal = null;
  public static final ItemMeterStick itemMeterStick = null;
  public static final ItemToolUniversal itemToolUniversal = null;

  public static void loadItems() {
    GameRegistry.registerItem(new ItemWrench(), "itemWrench");
    GameRegistry.registerItem(new ItemToolBag("itemToolBag", 5), "itemToolBag");
    GameRegistry.registerItem(new ItemEKToolUniversal(), "itemEKToolUniversal");
    GameRegistry.registerItem(new ItemMeterStick(), "itemMeterStick");
    GameRegistry.registerItem(new ItemToolUniversal(), "itemToolUniversal");
  }
}
