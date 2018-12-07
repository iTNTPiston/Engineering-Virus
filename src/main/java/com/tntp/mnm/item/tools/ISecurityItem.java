package com.tntp.mnm.item.tools;

import net.minecraft.item.ItemStack;

public interface ISecurityItem {
  public int getSecurityCode(ItemStack stack);

  public void setSecurityCode(ItemStack stack, int code);
}
