package com.tntp.mnm.gui.diskkey;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ITileDiskKeyable extends IInventory {
  public String diskKeyType();

  /**
   * called right before the transfer is going to happen
   * 
   * @param validDiskKey
   * @return true if the transfer should be allowed. There is no need to check if
   *         the disk key is valid(aka is a disk key and is empty)
   */
  public boolean onPreTransferToDiskKey(ItemStack validDiskKey);

  /**
   * called only if the transfer is successful
   * 
   * @param validDiskKey
   */
  public void onPostTransferToDiskKey(ItemStack validDiskKey);

  /**
   * called right before the transfer is going to happen
   * 
   * @param validDiskKey
   * @return true if the transfer should be allowed. There is no need to check if
   *         the disk key is valid(aka is a disk key and is empty)
   */
  public boolean onPreTransferFromDiskKey(ItemStack validDiskKey);

  /**
   * called only if the transfer is successful
   * 
   * @param validDiskKey
   */
  public void onPostTransferFromDiskKey(ItemStack validDiskKey);

}
