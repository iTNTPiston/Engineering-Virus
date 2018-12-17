package com.tntp.mnm.api.db;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GroupItemDef {
  public String groupId;
  public int itemId;

  public void toNBT(NBTTagCompound tag) {
    tag.setString("group", groupId);
    tag.setInteger("item", itemId);
  }

  public void fromNBT(NBTTagCompound tag) {
    groupId = tag.getString("group");
    itemId = tag.getInteger("item");
  }

  public int hashCode() {
    return groupId.hashCode() + itemId;
  }

  public boolean equals(Object o) {
    if (o instanceof GroupItemDef) {
      GroupItemDef d = (GroupItemDef) o;
      return d.groupId == groupId && d.itemId == itemId;
    }
    return false;
  }

}
