package com.tntp.mnm.virtual;

import com.tntp.mnm.item.ItemData;

import net.minecraft.item.ItemStack;

public class VirtualItemStack {
  private ItemStack stack;

  public ItemStack getItemStack() {
    return stack;
  }

  public boolean isData() {
    return stack != null && stack.getItem() instanceof ItemData;
  }
}
