package com.tntp.mnm.tileentity;

import com.tntp.mnm.util.UniversalUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * Super class for GerThermalSmelter and HeatDistributor
 * No EK tiles support automation
 * 
 * @author iTNTPiston
 *
 */
public class STileHeatNodeInventory extends STileHeatNode implements ISidedInventory {
  private ItemStack[] inventory;

  public STileHeatNodeInventory(int size) {
    inventory = new ItemStack[size];
  }

  @Override
  public int getSizeInventory() {
    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return inventory[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int size) {
    if (inventory[slot] == null)
      return null;
    ItemStack stack;
    if (inventory[slot].stackSize <= size) {
      stack = inventory[slot];
      inventory[slot] = null;
    } else {
      stack = inventory[slot].splitStack(size);
      if (inventory[slot].stackSize == 0)
        inventory[slot] = null;
    }
    markDirty();
    return stack;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot) {
    ItemStack s = inventory[slot];
    inventory[slot] = null;
    return s;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    inventory[slot] = stack;
    markDirty();
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
  public boolean isUseableByPlayer(EntityPlayer player) {
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
    return false;// EK Tiles do not support automation
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side) {
    return UniversalUtil.EMPTY_INT_ARRAY;
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack stack, int side) {
    return false;// EK tiles do not support automation
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, int side) {
    return false;// EK tiles do not support automation
  }

}
