package com.tntp.mnm.item;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.init.MNMCreativeTabs;

import net.minecraft.item.Item;

public class SItem extends Item {
  public SItem(String regName) {
    this.setTextureName(MNMMod.MODID + ":" + regName);
    this.setCreativeTab(MNMCreativeTabs.instance);
    this.setUnlocalizedName(regName);
  }
}
