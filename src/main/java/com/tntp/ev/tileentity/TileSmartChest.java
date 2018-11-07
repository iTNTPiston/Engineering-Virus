package com.tntp.ev.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileSmartChest extends TileEntity implements IInventory {

  private ItemStack[] slots;

  public TileSmartChest(int invSlots) {
    slots = new ItemStack[invSlots];
  }

  @Override
  public int getSizeInventory() {
    return slots.length;
  }

  @Override
  public ItemStack getStackInSlot(int i) {
    return slots[i];
  }

  @Override
  public ItemStack decrStackSize(int slot, int size) {
    ItemStack stack = getStackInSlot(slot);
    if (stack == null)
      return null;
    if (stack.stackSize < size) {
      markDirty();
      setInventorySlotContents(slot, null);
      return stack;
    } else {
      markDirty();
      ItemStack s = stack.splitStack(size);
      if (stack.stackSize == 0)
        setInventorySlotContents(slot, null);
      return s;
    }
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int i) {
    return getStackInSlot(i);
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    slots[slot] = stack;
  }

  @Override
  public String getInventoryName() {
    return "container.ev.smartchest";
  }

  @Override
  public boolean hasCustomInventoryName() {
    return false;
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
    return true;
  }

  @Override
  public void openInventory() {

  }

  @Override
  public void closeInventory() {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {
    return true;
  }

}
