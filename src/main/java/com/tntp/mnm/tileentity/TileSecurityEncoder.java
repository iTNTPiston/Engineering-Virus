package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.util.SecurityUtil;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TileSecurityEncoder extends STileInventory implements ITileCont {

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

  @Override
  public void addContainerSlots(List<Slot> slots) {
    for (int i = 0; i < 8; i++) {
      slots.add(new Slot(this, i, 17 + 18 * i, 41));
    }
    slots.add(new Slot(this, 8, 17, 79));
    slots.add(new Slot(this, 9, 53, 79));
  }

  @Override
  public String getContainerGui() {
    return "GuiContSecurityEncoder";
  }

}
