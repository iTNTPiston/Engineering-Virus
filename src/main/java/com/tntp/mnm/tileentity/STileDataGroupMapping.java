package com.tntp.mnm.tileentity;

import java.util.HashSet;
import java.util.Set;

import com.tntp.mnm.api.db.GroupItemMapping;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class STileDataGroupMapping extends STileData {
  private HashSet<GroupItemMapping> definedItems;

  public STileDataGroupMapping(int size) {
    super(size);
    definedItems = new HashSet<GroupItemMapping>();
  }

  @Override
  public int getUsedSpace() {
    return definedItems.size() * 4;
  }

  public boolean define(GroupItemMapping def) {
    if (this.getUsedSpace() + 4 < this.getTotalSpaceFromDisks()) {
      definedItems.add(def);
      return true;
    }
    return false;
  }

  public void findMapping(Set<Integer> itemIDS, String groupName) {
    for (GroupItemMapping mapping : definedItems) {
      if (mapping.groupId.equals(groupName)) {
        itemIDS.add(mapping.itemId);
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    NBTTagList list = new NBTTagList();
    for (GroupItemMapping mapping : definedItems) {
      NBTTagCompound mappingTag = new NBTTagCompound();
      mapping.toNBT(mappingTag);
      list.appendTag(mappingTag);
    }
    tag.setTag("defined_mappings", list);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    NBTTagList list = tag.getTagList("defined_mappings", NBT.TAG_COMPOUND);
    definedItems = new HashSet<GroupItemMapping>();
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound mappingTag = list.getCompoundTagAt(i);
      GroupItemMapping mapping = new GroupItemMapping();
      mapping.fromNBT(mappingTag);
      definedItems.add(mapping);
    }
  }

}
