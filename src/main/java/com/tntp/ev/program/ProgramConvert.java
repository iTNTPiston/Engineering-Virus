package com.tntp.ev.program;

import com.tntp.ev.api.digital.IDisk;
import com.tntp.ev.api.digital.IMemory;
import com.tntp.ev.api.digital.IProgram;
import com.tntp.ev.util.ItemUtil;
import com.tntp.ev.virtual.VirtualItemStack;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Converts VirtualItemStack to ItemStack
 * 
 * @author iTNTPiston
 *
 */
public class ProgramConvert implements IProgram {

  @Override
  public int execCycle(IMemory mem, IDisk disk, IInventory inv) {
    int error = 0;
    for (int i = 0; i < disk.diskSize(); i++) {
      VirtualItemStack vis = disk.getStackAt(i);
      if (vis != null && !vis.isData()) {
        ItemStack stack = vis.getItemStack();
        for (int j = 0; j < inv.getSizeInventory() && stack.stackSize > 0; j++) {
          ItemStack s = inv.getStackInSlot(j);
          if (s == null) {
            ItemStack put = stack.copy();
            put.stackSize = Math.min(stack.getMaxStackSize(), stack.stackSize);
            stack.stackSize -= put.stackSize;
            inv.setInventorySlotContents(j, put);
          } else if (ItemUtil.areItemAndTagEqual(stack, s)) {
            int putStack = Math.min(stack.stackSize, stack.getMaxStackSize() - s.stackSize);
            s.stackSize += putStack;
            stack.stackSize -= putStack;
          }
        }
        if (stack.stackSize > 0) {
          error += stack.stackSize;
        } else {
          disk.setStackAt(i, null);
        }
        break;
      }
    }
    return error;
  }

  @Override
  public int authority() {
    return 6;
  }

}
