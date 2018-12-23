package com.tntp.mnm.tileentity;

import net.minecraft.item.ItemStack;

public class TileDataDefinitionTerminal extends STileNeithernetInventory {
  private ItemStack[] cachedDefinition;
  private int rowTotal;
  private int rowCurrent;
  private boolean needsUpdate;

  public TileDataDefinitionTerminal() {
    super(32);
  }

  public void scrollTo(int row) {
    if (row + 4 > rowTotal) {
      row = rowTotal - 4;
    }
    rowCurrent = row;
    int startIndex = rowCurrent * 8;
    int end = Math.min(startIndex + 32, cachedDefinition.length);
    for (int i = 0, j = startIndex; j < end; i++, j++) {
      this.setInventorySlotContents(i, cachedDefinition[j]);
    }
  }

  public void updateCache() {

  }

  @Override
  public void openInventory() {
    needsUpdate = true;
  }

  @Override
  public void closeInventory() {
    needsUpdate = false;
  }

}
