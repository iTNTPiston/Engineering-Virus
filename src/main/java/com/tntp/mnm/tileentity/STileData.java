package com.tntp.mnm.tileentity;

import com.tntp.mnm.item.disk.ItemDisk;

import net.minecraft.item.ItemStack;

public abstract class STileData extends STileNeithernetInventory {

  private int clientUsedSpaceCache;

  public STileData(int size) {
    super(size);
  }

  public int getTotalSpaceFromDisks() {
    int space = 0;
    for (int i = 0; i < this.getSizeInventory(); i++) {
      ItemStack stack = this.getStackInSlot(i);
      if (stack != null && stack.getItem() instanceof ItemDisk) {
        int add = ((ItemDisk) stack.getItem()).dataSize;
        space += add;
      }
    }
    return space;
  }

  public ItemStack insertDisk(ItemStack stack) {
    for (int i = 0; i < this.getSizeInventory(); i++) {
      if (this.getStackInSlot(i) == null) {
        ItemStack s = stack.splitStack(1);
        this.setInventorySlotContents(i, s);
        stack.stackSize--;
        break;
      }
    }
    if (stack.stackSize == 0) {
      stack = null;
    }
    return stack;
  }

  public abstract int getUsedSpace();

  /**
   * For gui purposes.
   * 
   * @return
   */
  public int getUsedSpaceCache() {
    return clientUsedSpaceCache;
  }

  public void setUsedSpaceCache(int c) {
    clientUsedSpaceCache = c;
  }

  public int receiveDataPullRequest() {
    return getUsedSpace();
  }
}
