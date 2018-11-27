package com.tntp.mnm.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MNMCreativeTabs extends CreativeTabs {
  public static final MNMCreativeTabs instance = new MNMCreativeTabs();

  public MNMCreativeTabs() {
    super("metalnetworkmainframe");
  }

  @Override
  public Item getTabIconItem() {
    return Item.getItemFromBlock(MNMBlocks.centralProcessor);
  }

}
