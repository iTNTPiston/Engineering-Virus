package com.tntp.mnm.item.tools;

import java.util.List;

import com.tntp.mnm.block.SBlock;
import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.util.LocalUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemDiskKey extends SItemTool {

  public ItemDiskKey() {
    super(GuiTabType.DISK_KEY);
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {
    return getStoredTileType(stack).length() > 0 ? EnumRarity.uncommon : EnumRarity.common;
  }

  public void clearDiskKey(ItemStack stack) {
    if (stack == null || !stack.hasTagCompound())
      return;
    NBTTagCompound tag = stack.getTagCompound();
    NBTTagCompound diskKey = new NBTTagCompound();
    diskKey.setString("type", "");
    tag.setTag("MNM|DiskKey", diskKey);
    stack.setTagCompound(tag);
  }

  public String getStoredTileType(ItemStack stack) {
    if (stack == null || !stack.hasTagCompound())
      return "";
    NBTTagCompound tag = stack.getTagCompound();
    if (!tag.hasKey("MNM|DiskKey"))
      return "";
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

  public int getStoredSpace(ItemStack stack) {
    if (stack == null || !stack.hasTagCompound())
      return 0;
    NBTTagCompound tag = stack.getTagCompound();
    if (!tag.hasKey("MNM|DiskKey"))
      return 0;
    return getStoredSpaceFromNBT(tag.getCompoundTag("MNM|DiskKey"));
  }

  public static String getStoredTileTypeFromNBT(NBTTagCompound diskKey) {
    return diskKey.getString("type");
  }

  public static NBTTagCompound getStoredContentFromNBT(NBTTagCompound diskKey) {
    return diskKey.getCompoundTag("content");
  }

  public static int getStoredSpaceFromNBT(NBTTagCompound diskKey) {
    return diskKey.getInteger("space");
  }

  /**
   * 
   * @param stack
   * @return true if the data is written successfully
   */
  public static boolean writeToDiskKey(ItemStack stack, String type, NBTTagCompound data, int space) {
    if (stack == null)
      return false;
    NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    NBTTagCompound diskKey = tag.getCompoundTag("MNM|DiskKey");
    if (getStoredTileTypeFromNBT(diskKey).length() != 0)
      return false;// already has data
    diskKey.setString("type", type);
    diskKey.setTag("content", data);
    diskKey.setInteger("space", space);
    tag.setTag("MNM|DiskKey", diskKey);
    stack.setTagCompound(tag);
    return true;
  }

  /**
   * Transfer the data from tile to item
   * 
   * @param tile
   * @param validDiskKey
   * @return true if the transfer is successful
   */
  public boolean onTransferToDiskKey(ITileDiskKeyable tile, ItemStack validDiskKey) {
    if (((STileData) tile).isTransferringData)
      return false;
    if (tile.onPreTransferToDiskKey(validDiskKey)) {
      STileData t = (STileData) tile;
      t.isTransferringData = true;
      String type = tile.diskKeyType();
      NBTTagCompound data = new NBTTagCompound();
      t.writeDataToNBT(data);
      if (writeToDiskKey(validDiskKey, type, data, t.getUsedSpace())) {
        t.clearData();
        t.pendingDiskEjection = true;
        t.markDirty();
        return true;
      } else {
        t.isTransferringData = false;
      }
    }
    return false;
  }

  /**
   * Transfer the data from item to tile
   * 
   * @param tile
   * @param validDiskKey
   * @return true if the transfer is successful
   */
  public boolean onTransferFromDiskKey(ITileDiskKeyable tile, ItemStack validDiskKey) {
    if (((STileData) tile).isTransferringData)
      return false;
    if (tile.onPreTransferFromDiskKey(validDiskKey)) {
      STileData t = (STileData) tile;
      t.isTransferringData = true;
      if (!t.hasData()) {
        // tile must be empty
        String type = getStoredTileType(validDiskKey);
        if (tile.diskKeyType().equals(type)) {
          // type must match
          NBTTagCompound data = getStoredContent(validDiskKey);
          if (t.getTotalSpaceFromDisks() >= getStoredSpace(validDiskKey)) {
            // tile must have enough space
            // read
            t.readDataFromNBT(data);
            t.markDirty();
            t.isTransferringData = false;
            clearDiskKey(validDiskKey);
            return true;
          }
        }
      }
      t.isTransferringData = false;
    }

    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
    String type = getStoredTileType(stack);
    if (type.length() > 0) {
      int space = getStoredSpace(stack);
      tooltip.add(LocalUtil.localize("mnm.tooltip.disk_key.type_arg_s",
          LocalUtil.localize("mnm.tooltip.disk_key.type." + type)));
      tooltip.add(LocalUtil.localize("mnm.tooltip.disk_key.space_arg_d", space));
    }
  }

  public boolean isDiskKey(ItemStack stack) {
    return stack != null && stack.getItem() == this;
  }

}
