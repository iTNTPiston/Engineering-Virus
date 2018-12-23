package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.db.Mainframe;

import net.minecraft.item.ItemStack;

public class TileQueryBuilder extends STileNeithernetInventory {

  private String currentGroupName;
  private int currentFirstGroupIndex;
  private int currentScrollIndex;

  private ItemStack[] cachedGroupList;
  private ItemStack[] cachedItemGroup;

  public TileQueryBuilder() {
    super(12);// 0-8 inventory, 9,10,11 chip,12-26 display
  }

  public void scroll(int direction) {

  }

  public void selectGroup(String str) {
    Mainframe mf = getPort().getMainframe();
  }

}
