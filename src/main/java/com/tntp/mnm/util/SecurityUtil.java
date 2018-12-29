package com.tntp.mnm.util;

import com.tntp.mnm.item.tools.ISecurityItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SecurityUtil {
  public static int computeCodeFromStacks(ItemStack[] stacks) {
    int code = 0;
    for (int i = 0; i < 8; i++) {
      int addOn = 0;
      int index = i % stacks.length;
      ItemStack stack = stacks[index];
      if (stack != null) {
        addOn = stack.getItemDamage() % 16;
      }
      code = code << 4;
      code += addOn;
    }
    return code;
  }

  public static void writeCode(ItemStack stack, int code) {
    if (!canEncode(stack))
      return;
    NBTTagCompound tag;
    if (!stack.hasTagCompound()) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    } else {
      tag = stack.getTagCompound();
    }
    tag.setInteger("MNM|Security", code);
    stack.setTagCompound(tag);
  }

  public static boolean matches(ItemStack stack, int codeToMatch) {
    if (!canEncode(stack))
      return false;
    if (!stack.hasTagCompound()) {
      return false;
    }
    NBTTagCompound tag = stack.getTagCompound();
    int i = tag.getInteger("MNM|Security");
    return i == codeToMatch;
  }

  public static boolean isEncoded(ItemStack stack) {
    if (!canEncode(stack))
      return false;
    if (!stack.hasTagCompound()) {
      return false;
    }
    return stack.getTagCompound().hasKey("MNM|Security");
  }

  public static int getCode(ItemStack stack) {
    if (!stack.hasTagCompound())
      return 0;
    NBTTagCompound tag = stack.getTagCompound();
    int i = tag.getInteger("MNM|Security");
    return i;
  }

  public static boolean canEncode(ItemStack stack) {
    if (stack == null)
      return false;
    Item item = stack.getItem();
    if (!(item instanceof ISecurityItem))
      return false;
    return true;
  }
}
