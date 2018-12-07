package com.tntp.mnm.tileentity;

import com.tntp.mnm.util.SecurityUtil;

import net.minecraft.item.ItemStack;

public class TileSecurityEncoder extends STileInventory {

  public TileSecurityEncoder() {
    super(10);
  }

  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (getStackInSlot(9) == null) {// if output not obstructed
        ItemStack encodeItem = getStackInSlot(8);
        if (SecurityUtil.canEncode(encodeItem)) {// if item is ISecurityItem
          // Make an array
          ItemStack[] stacks = new ItemStack[8];
          for (int i = 0; i < stacks.length; i++) {
            stacks[i] = getStackInSlot(i);
          }
          // Compute security code
          int code = SecurityUtil.computeCodeFromStacks(stacks);
          // Encode and set the item
          SecurityUtil.writeCode(encodeItem, code);
          this.setInventorySlotContents(9, encodeItem);
          this.setInventorySlotContents(8, null);
          // Mark dirty
          markDirty();
        }
      }
    }
  }

}
