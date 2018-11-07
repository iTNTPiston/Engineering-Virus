package com.tntp.ev.api;

import com.tntp.ev.virtual.VirtualItemStack;

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
}
