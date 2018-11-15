package com.tntp.mnm.init;

import com.tntp.mnm.item.ItemToolBag;
import com.tntp.mnm.item.ItemWrench;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("metalnetworkmainframe")
public class MNMItems {
  public static final ItemWrench itemWrench = null;
  public static final ItemToolBag itemToolBag = null;

  public static void loadItems() {
    GameRegistry.registerItem(new ItemWrench(), "itemWrench");
    GameRegistry.registerItem(new ItemToolBag("itemToolBag", 5), "itemToolBag");
  }
}
