package com.tntp.mnm.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

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

  public static String toTwoDigitStackSizeDisplay(int stacksize) {
    if (stacksize == 0)
      return EnumChatFormatting.GRAY + "" + stacksize;
    if (stacksize < 100)
      return String.valueOf(stacksize);
    if (stacksize < 1000)
      return stacksize / 100 + "!";
    if (stacksize < 10000)
      return EnumChatFormatting.YELLOW + "" + stacksize / 1000 + "K";
    if (stacksize < 100000)
      return EnumChatFormatting.GREEN + "" + stacksize / 10000 + "W";
    if (stacksize < 1000000)
      return EnumChatFormatting.AQUA + "" + stacksize / 100000 + "L";
    if (stacksize < 10000000)
      return EnumChatFormatting.LIGHT_PURPLE + "" + stacksize / 1000000 + "M";
    if (stacksize < 100000000)
      return EnumChatFormatting.LIGHT_PURPLE + "" + stacksize / 10000000 + "U";
    if (stacksize < 1000000000)
      return EnumChatFormatting.LIGHT_PURPLE + "" + stacksize / 100000000 + "V";
    return EnumChatFormatting.LIGHT_PURPLE + "?G";
  }
}
