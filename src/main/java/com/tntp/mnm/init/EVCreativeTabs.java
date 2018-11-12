package com.tntp.mnm.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class EVCreativeTabs extends CreativeTabs {
  public static final EVCreativeTabs instance = new EVCreativeTabs();

  public EVCreativeTabs() {
    super("ev");
  }

  @Override
  public Item getTabIconItem() {
    return EVItems.itemByteCoin;
  }

}
