package com.tntp.mnm.item.tools;

import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.tileentity.STileData;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemDiskKey extends SItemTool {

  public ItemDiskKey() {
    super();
  }

  public String getStoredTileType(ItemStack stack) {
    if (stack == null || !stack.hasTagCompound())
      return null;
    NBTTagCompound tag = stack.getTagCompound();
    if (!tag.hasKey("MNM|DiskKey"))
      return null;
    return getStoredTileTypeFromNBT(tag.getCompoundTag("MNM|DiskKey"));
  }

  public NBTTagCompound getStoredContent(ItemStack stack) {
    if (stack == null || !stack.hasTagCompound())
      return null;
    NBTTagCompound tag = stack.getTagCompound();
    if (!tag.hasKey("MNM|DiskKey"))
      return null;
    return getStoredContentFromNBT(tag.getCompoundTag("MNM|DiskKey"));
  }

  public static String getStoredTileTypeFromNBT(NBTTagCompound diskKey) {
    return diskKey.getString("type");
  }

  public static NBTTagCompound getStoredContentFromNBT(NBTTagCompound diskKey) {
    return diskKey.getCompoundTag("content");
  }

  /**
   * 
   * @param stack
   * @return true if the data is written successfully
   */
  public static boolean writeToDiskKey(ItemStack stack, String type, NBTTagCompound data) {
    if (stack == null)
      return false;
    NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    NBTTagCompound diskKey = tag.getCompoundTag("MNM|DiskKey");
    if (getStoredTileTypeFromNBT(diskKey).length() != 0)
      return false;// already has data
    diskKey.setString("type", type);
    diskKey.setTag("content", data);
    tag.setTag("MNM|DiskKey", diskKey);
    stack.setTagCompound(tag);
    return true;
  }

  /**
   * Transfer the data from
   * 
   * @param tile
   * @param validDiskKey
   * @return true if the transfer is successful
   */
  public boolean onTransferToDiskKey(ITileDiskKeyable tile, ItemStack validDiskKey) {
    if (tile.onPreTransferToDiskKey(validDiskKey)) {
      String type = tile.diskKeyType();
      NBTTagCompound data = new NBTTagCompound();
      ((STileData) tile).writeDataToNBT(data);
    }
  }

  public boolean onTransferFromDiskKey(ITileDiskKeyable tile, ItemStack validDiskKey) {

  }

}
