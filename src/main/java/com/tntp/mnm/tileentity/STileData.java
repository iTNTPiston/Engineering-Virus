package com.tntp.mnm.tileentity;

import com.tntp.mnm.item.disk.ItemDisk;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class STileData extends STileNeithernetInventory {
  private int diskNum;

  private int clientUsedSpaceCache;

  /**
   * The data is being transferred using a disk key
   */
  protected boolean isTransferringData;

  public STileData(int size) {
    super(size);
  }

  @Override
  public void rescanSubtypes() {
    super.rescanSubtypes();
    diskNum = 0;
    for (int i = 0; i < this.getSizeInventory(); i++) {
      if (this.getStackInSlot(i) != null)
        diskNum++;
    }
    worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), EVENT_DISK_NUM, diskNum);
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
    if (isTransferringData)
      return stack;
    for (int i = 0; i < this.getSizeInventory(); i++) {
      if (this.getStackInSlot(i) == null) {
        ItemStack s = stack.splitStack(1);
        this.setInventorySlotContents(i, s);
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

  @Override
  public boolean receiveClientEvent(int event, int param) {
    if (super.receiveClientEvent(event, param))
      return true;
    if (event == EVENT_DISK_NUM) {
      if (diskNum != param) {
        diskNum = param;
        if (worldObj != null)
          worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
      }
      return true;
    }
    return false;
  }

  public int getNumDisk() {
    return diskNum;
  }

  public abstract void writeDataToNBT(NBTTagCompound tag);

  public abstract void readDataFromNBT(NBTTagCompound tag);

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    readDataFromNBT(tag);
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    writeDataToNBT(tag);
  }

}
