package com.tntp.mnm.api.db;

import java.util.ArrayList;
import java.util.List;

import com.tntp.mnm.util.ItemUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A Query with take/put
 * 
 * @author iTNTPiston
 *
 */
public class MainframeTPQuery implements IQuery {
  private int priority;
  private List<Take> takeList;

  private static class Take {
    int id, qty;// put is id=-1

    Take(int i, int q) {
      id = i;
      qty = q;
    }
  }

  public MainframeTPQuery(int priority) {
    this.priority = priority;
    takeList = new ArrayList<Take>();
  }

  public MainframeTPQuery() {
    this(0);
  }

  public void addTP(int id, int qty) {
    takeList.add(new Take(id, qty));
  }

  @Override
  public void execute(Mainframe mf, IInventory inv, int startSlot, int endSlot) {
    for (Take t : takeList) {
      if (t.id >= 0) {
        ItemStack s = mf.takeItemStack(t.id, t.qty);
        if (s != null) {
          putStackInInventory(inv, s, startSlot, endSlot);
          if (s.stackSize > 0) {
            mf.insertItemStack(s);
          }
        }
      } else {
        // put
        ItemStack[] is = new ItemStack[inv.getSizeInventory()];
        for (int i = startSlot; i <= endSlot; i++) {
          is[i] = inv.getStackInSlot(i);
        }
        mf.insertItemStack(is);
        for (int i = startSlot; i <= endSlot; i++) {
          if (is[i].stackSize == 0)
            is[i] = null;
          inv.setInventorySlotContents(i, is[i]);
        }
      }
    }
  }

  private void putStackInInventory(IInventory inv, ItemStack s, int startSlot, int endSlot) {
    if (s == null)
      return;
    for (int i = startSlot; i <= endSlot; i++) {
      ItemStack slot = inv.getStackInSlot(i);
      if (ItemUtil.areItemAndTagEqual(slot, s)) {
        int max = Math.max(inv.getInventoryStackLimit(), slot.getMaxStackSize());
        int space = max - slot.stackSize;
        if (s.stackSize <= space) {
          slot.stackSize += s.stackSize;
          s.stackSize = 0;
        } else {
          slot.stackSize += space;
          s.stackSize -= space;
        }
      }
      if (s.stackSize == 0)
        return;
    }
  }

  @Override
  public int getPriority() {
    return priority;
  }

}
