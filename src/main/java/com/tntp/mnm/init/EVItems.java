package com.tntp.mnm.init;

import com.tntp.mnm.item.ItemByteCoin;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("engineeringvirus")
public class EVItems {
  public static final ItemByteCoin itemByteCoin = null;

  public static void loadItems() {
    GameRegistry.registerItem(new ItemByteCoin(), "itemByteCoin");
  }
}
