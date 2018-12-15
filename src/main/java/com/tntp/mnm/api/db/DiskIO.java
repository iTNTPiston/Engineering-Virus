package com.tntp.mnm.api.db;

import com.tntp.mnm.item.disk.ItemDisk;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DiskIO {
  private IInventory disks;

  private int totalSpace;
  private int usedSpace;

  public boolean add(int space) {
    if (totalSpace - usedSpace > space) {
      usedSpace += space;
      return true;
    }
    return false;
  }

  private void addToItems(int space) {
    for (int i = 0; i < disks.getSizeInventory(); i++) {
      ItemStack item = disks.getStackInSlot(i);
      if (item != null && item.getItem() instanceof ItemDisk) {

      }
    }
  }
}
