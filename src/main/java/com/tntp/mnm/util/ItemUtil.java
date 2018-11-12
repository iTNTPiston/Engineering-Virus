package com.tntp.mnm.util;

import net.minecraft.item.ItemStack;

public class ItemUtil {
  public static int VERSION = 1;

  /**
   * See if the two ItemStack are the same except for the stacksize
   * 
   * @param stack1
   * @param stack2
   * @return
   */
  public static boolean areItemAndTagEqual(ItemStack stack1, ItemStack stack2) {
    if (stack1 == null && stack2 == null)
      return true;
    if (ItemStack.areItemStackTagsEqual(stack1, stack2)) {
      return stack1.isItemEqual(stack2);
    }
    return false;
  }
}
