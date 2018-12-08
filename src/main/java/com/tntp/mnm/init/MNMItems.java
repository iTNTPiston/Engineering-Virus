package com.tntp.mnm.init;

import com.tntp.mnm.item.ItemToolBag;
import com.tntp.mnm.item.ItemWrench;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.item.tools.ItemCommonWrench;
import com.tntp.mnm.item.tools.ItemDisplayInterface;
import com.tntp.mnm.item.tools.ItemEKToolUniversal;
import com.tntp.mnm.item.tools.ItemMeterStick;
import com.tntp.mnm.item.tools.ItemScrewDriver;
import com.tntp.mnm.item.tools.ItemToolUniversal;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("metalnetworkmainframe")
public class MNMItems {
  public static final ItemWrench wrench = null;
  public static final ItemToolBag toolBag = null;
  public static final SItemTool eKToolUniversal = null;
  public static final ItemMeterStick meterStick = null;
  public static final ItemToolUniversal toolUniversal = null;
  public static final ItemCommonWrench commonWrench = null;
  public static final ItemScrewDriver screwDriver = null;
  public static final ItemDisplayInterface displayInterface = null;

  public static void loadItems() {
    GameRegistry.registerItem(new ItemWrench(), "wrench");
    GameRegistry.registerItem(new ItemToolBag("toolBag", 5), "toolBag");
    GameRegistry.registerItem(new ItemEKToolUniversal(), "eKToolUniversal");
    GameRegistry.registerItem(new ItemMeterStick(), "meterStick");
    GameRegistry.registerItem(new ItemToolUniversal(), "toolUniversal");
    GameRegistry.registerItem(new ItemCommonWrench(), "commonWrench");
    GameRegistry.registerItem(new ItemScrewDriver(), "screwDriver");
    GameRegistry.registerItem(new ItemDisplayInterface(), "displayInterface");
  }
}
