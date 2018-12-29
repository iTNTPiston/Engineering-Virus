package com.tntp.mnm.tileentity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tntp.mnm.api.db.GroupItemMapping;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class STileDataGroupMapping extends STileData implements ITileDiskKeyable {
  private HashSet<GroupItemMapping> definedItems;

  public STileDataGroupMapping(int size) {
    super(size);
    definedItems = new HashSet<GroupItemMapping>();
  }

  @Override
  public int getUsedSpace() {
    return definedItems.size() * 4;
  }

  public boolean addMapping(GroupItemMapping def) {
    if (isTransferringData)
      return false;
    if (this.getUsedSpace() + 4 <= this.getTotalSpaceFromDisks()) {
      definedItems.add(def);
      markDirty();
      return true;
    }
    return false;
  }

  public void removeMapping(GroupItemMapping def) {
    if (isTransferringData)
      return;
    definedItems.remove(def);
    markDirty();
  }

  public void removeAll(int definition) {
    if (isTransferringData)
      return;
    for (Iterator<GroupItemMapping> iter = definedItems.iterator(); iter.hasNext();) {
      GroupItemMapping gim = iter.next();
      if (gim.itemId == definition)
        iter.remove();
    }
    markDirty();
  }

  public void findMapping(Set<Integer> itemIDS, String groupName) {
    for (GroupItemMapping mapping : definedItems) {
      if (mapping.groupId.equals(groupName)) {
        itemIDS.add(mapping.itemId);
      }
    }
  }

  @Override
  public void writeDataToNBT(NBTTagCompound tag) {
    NBTTagList list = new NBTTagList();
    for (GroupItemMapping mapping : definedItems) {
      NBTTagCompound mappingTag = new NBTTagCompound();
      mapping.toNBT(mappingTag);
      list.appendTag(mappingTag);
    }
    tag.setTag("defined_mappings", list);
  }

  @Override
  public void readDataFromNBT(NBTTagCompound tag) {
    NBTTagList list = tag.getTagList("defined_mappings", NBT.TAG_COMPOUND);
    definedItems = new HashSet<GroupItemMapping>();
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound mappingTag = list.getCompoundTagAt(i);
      GroupItemMapping mapping = new GroupItemMapping("", 0);
      mapping.fromNBT(mappingTag);
      definedItems.add(mapping);
    }
  }

  @Override
  public void clearData() {
    definedItems.clear();
    markDirty();
  }

  @Override
  public String diskKeyType() {
    return "mapping";
  }

  @Override
  public boolean onPreTransferToDiskKey(ItemStack validDiskKey) {
    return true;
  }

  @Override
  public void onPostTransferToDiskKey(ItemStack validDiskKey) {

  }

  @Override
  public boolean onPreTransferFromDiskKey(ItemStack validDiskKey) {
    return true;
  }

  @Override
  public void onPostTransferFromDiskKey(ItemStack validDiskKey) {
  }

}
