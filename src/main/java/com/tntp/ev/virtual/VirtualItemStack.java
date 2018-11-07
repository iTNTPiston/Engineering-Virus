package com.tntp.ev.virtual;

import com.tntp.ev.item.ItemData;

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
