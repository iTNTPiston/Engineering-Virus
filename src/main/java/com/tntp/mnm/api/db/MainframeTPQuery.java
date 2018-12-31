package com.tntp.mnm.api.db;

import java.util.ArrayList;
import java.util.List;

import com.tntp.mnm.util.ItemUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
    System.out.println("Analyzing Query");
    for (Take t : takeList) {
      if (t.id >= 0) {
        System.out.println("Executing TAKE");
        ItemStack s = mf.takeItemStack(t.id, t.qty);
        if (s != null) {
          putStackInInventory(inv, s, startSlot, endSlot);
          if (s.stackSize > 0) {
            mf.insertItemStack(s);
          }
        }
      } else {
        System.out.println("Executing PUT");
        // put
        ItemStack[] is = new ItemStack[endSlot - startSlot + 1];
        for (int i = 0; i < is.length; i++) {
          is[i] = inv.getStackInSlot(i + startSlot);
        }
        mf.insertItemStack(is);
        System.out.println("Applying changes");
        for (int i = 0; i < is.length; i++) {
          if (is[i] != null && is[i].stackSize == 0)
            is[i] = null;
          inv.setInventorySlotContents(i + startSlot, is[i]);
        }
      }
    }
  }

  private void putStackInInventory(IInventory inv, ItemStack s, int startSlot, int endSlot) {
    if (s == null || s.stackSize == 0)
      return;
    System.out.println("Putting stacks in inventory");
    for (int i = startSlot; i <= endSlot; i++) {
      ItemStack slot = inv.getStackInSlot(i);
      int max = Math.min(inv.getInventoryStackLimit(), s.getMaxStackSize());

      if (slot == null) {
        int size = Math.min(max, s.stackSize);
        inv.setInventorySlotContents(i, s.splitStack(size));
      } else if (ItemUtil.areItemAndTagEqual(slot, s)) {
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
