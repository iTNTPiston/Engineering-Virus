package com.tntp.mnm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class SContainer extends Container {
  private int machineSlots;

  public SContainer(IInventory playerInventory, int slots) {
    machineSlots = slots;
    for (int k = 0; k < 3; k++) {
      for (int j = 0; j < 9; j++) {
        this.addSlotToContainer(new Slot(playerInventory, j + 9 + k * 9, 8 + j * 18, 123 + k * 18));
      }
    }
    for (int j = 0; j < 9; j++) {
      this.addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 181));
    }
  }

  public abstract void setupMachineSlots();

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }

  public ItemStack transferStackInSlot(EntityPlayer player, int index) {
    ItemStack itemstack = null;
    Slot slot = (Slot) this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index < machineSlots) {
        if (!this.mergeItemStack(itemstack1, machineSlots, this.inventorySlots.size(), true)) {
          return null;
        }
      } else {
        int start = 0;
        int end = machineSlots;
        if (!this.mergeItemStack(itemstack1, start, end, false)) {
          return null;
        }
      }

      if (itemstack1.stackSize == 0) {
        slot.putStack((ItemStack) null);
      } else {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }

  /**
   * merges provided ItemStack with the first avaliable one in the
   * container/player inventory
   */
  // add slot validity check
  // add slot stack limit check
  protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean increasingOrder) {
    boolean putIn = false;
    int k = start;

    if (increasingOrder) {
      k = end - 1;
    }

    Slot slot;
    ItemStack itemstack1;

    if (stack.isStackable()) {
      while (stack.stackSize > 0 && (!increasingOrder && k < end || increasingOrder && k >= start)) {
        slot = (Slot) this.inventorySlots.get(k);
        itemstack1 = slot.getStack();
        if (itemstack1 != null && itemstack1.getItem() == stack.getItem()
            && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage())
            && ItemStack.areItemStackTagsEqual(stack, itemstack1)) {
          int l = itemstack1.stackSize + stack.stackSize;
          int maxSize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());// add: respect slot limit

          if (l <= maxSize) {
            stack.stackSize = 0;
            itemstack1.stackSize = l;
            slot.onSlotChanged();
            putIn = true;
          } else if (itemstack1.stackSize < maxSize) {
            stack.stackSize -= maxSize - itemstack1.stackSize;
            itemstack1.stackSize = maxSize;
            slot.onSlotChanged();
            putIn = true;
          }
        }

        if (increasingOrder) {
          --k;
        } else {
          ++k;
        }
      }
    }

    if (stack.stackSize > 0) {
      if (increasingOrder) {
        k = end - 1;
      } else {
        k = start;
      }

      while (stack.stackSize > 0 && (!increasingOrder && k < end || increasingOrder && k >= start)) {
        slot = (Slot) this.inventorySlots.get(k);
        itemstack1 = slot.getStack();

        if (itemstack1 == null && slot.isItemValid(stack))// add
        {
          ItemStack putStack = stack.copy();
          int putSize = Math.min(slot.getSlotStackLimit(), stack.stackSize);
          putStack.stackSize = putSize;
          slot.putStack(putStack);
          slot.onSlotChanged();
          stack.stackSize -= putSize;
          putIn = true;
          break;
        }

        if (increasingOrder) {
          --k;
        } else {
          ++k;
        }
      }
    }

    return putIn;
  }

}
