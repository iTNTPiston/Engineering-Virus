package com.tntp.mnm.gui.security;

import net.minecraft.item.ItemStack;

public interface ITileSecured {
  public boolean securityCheck(ItemStack using);
}
