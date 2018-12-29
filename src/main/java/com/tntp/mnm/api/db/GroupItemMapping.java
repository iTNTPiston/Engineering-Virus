package com.tntp.mnm.api.db;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GroupItemMapping {
  public final String groupId;
  public final int itemId;

  public GroupItemMapping(String g, int i) {
    groupId = g;
    itemId = i;
  }

  public void toNBT(NBTTagCompound tag) {
    tag.setString("group", groupId);
    tag.setInteger("item", itemId);
  }

  public static GroupItemMapping fromNBT(NBTTagCompound tag) {
    String g = tag.getString("group");
    int i = tag.getInteger("item");
    return new GroupItemMapping(g, i);
  }

  public int hashCode() {
    return groupId.hashCode() + itemId;
  }

  public boolean equals(Object o) {
    if (o instanceof GroupItemMapping) {
      GroupItemMapping d = (GroupItemMapping) o;
      return d.groupId == groupId && d.itemId == itemId;
    }
    return false;
  }

}
