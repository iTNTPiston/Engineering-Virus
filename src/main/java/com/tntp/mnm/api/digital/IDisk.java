package com.tntp.ev.api.digital;

import com.tntp.ev.virtual.VirtualItemStack;

import net.minecraft.item.ItemStack;

/**
 * Hard disk that contains VirtualItemStacks
 * 
 * @author iTNTPiston
 *
 */
public interface IDisk {
  public VirtualItemStack getStackAt(int i);

  public void setStackAt(int i, VirtualItemStack vis);

  public int diskSize();

  public boolean canAccess(IProgram p);

  public boolean writeItemStack(ItemStack i);
}
