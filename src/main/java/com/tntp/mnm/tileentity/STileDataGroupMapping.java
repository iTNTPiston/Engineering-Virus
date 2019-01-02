package com.tntp.mnm.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tntp.mnm.api.db.GroupItemMapping;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class STileDataGroupMapping extends STileData {
  private int usedSpace;

  public STileDataGroupMapping(int size) {
    super(size);
    // definedItems = new HashSet<GroupItemMapping>();
  }

  @Override
  public int getUsedSpace() {
    return usedSpace;
  }

  public void setUsedSpace(Mainframe mf, int space) {
    if (!checkAndRememberMainframe(mf))
      return;
    usedSpace = space;
    markDirty();
  }

  public boolean access(Mainframe mf) {
    if (!checkAndRememberMainframe(mf))
      return false;
    return true;
  }

//  public boolean addMapping(Mainframe mf, GroupItemMapping def) {
//    if (isTransferringData)
//      return false;
//    if (!checkAndRememberMainframe(mf))
//      return false;
//    if (this.getUsedSpace() + 4 <= this.getTotalSpaceFromDisks()) {
//      definedItems.add(def);
//      markDirty();
//      return true;
//    }
//    return false;
//  }

//  public void removeMapping(Mainframe mf, GroupItemMapping def) {
//    if (isTransferringData)
//      return;
//    if (!checkAndRememberMainframe(mf))
//      return;
//    definedItems.remove(def);
//    markDirty();
//  }

//  public void modifyMapping(Mainframe mf, int oldID, int newID) {
//    if (isTransferringData)
//      return;
//    if (!checkAndRememberMainframe(mf))
//      return;
//    ArrayList<GroupItemMapping> change = new ArrayList<GroupItemMapping>();
//    for (Iterator<GroupItemMapping> iter = definedItems.iterator(); iter.hasNext();) {
//      GroupItemMapping mapping = iter.next();
//      if (mapping.itemId == oldID) {
//        if (newID >= 0)
//          change.add(new GroupItemMapping(mapping.groupId, newID));
//        iter.remove();
//      }
//    }
//    for (GroupItemMapping newMapping : change) {
//      definedItems.add(newMapping);
//    }
//    markDirty();
//  }

//  public void removeAll(Mainframe mf, int definition) {
//    if (isTransferringData)
//      return;
//    if (!checkAndRememberMainframe(mf))
//      return;
//    for (Iterator<GroupItemMapping> iter = definedItems.iterator(); iter.hasNext();) {
//      GroupItemMapping gim = iter.next();
//      if (gim.itemId == definition)
//        iter.remove();
//    }
//    markDirty();
//  }

//  public void findMapping(Mainframe mf, Set<Integer> itemIDS, String groupName) {
//    if (!checkAndRememberMainframe(mf))
//      return;
//    for (GroupItemMapping mapping : definedItems) {
//      if (mapping.groupId.equals(groupName)) {
//        itemIDS.add(mapping.itemId);
//      }
//    }
//  }

  @Override
  public void writeDataToNBT(NBTTagCompound tag) {
//    NBTTagList list = new NBTTagList();
//    for (GroupItemMapping mapping : definedItems) {
//      NBTTagCompound mappingTag = new NBTTagCompound();
//      mapping.toNBT(mappingTag);
//      list.appendTag(mappingTag);
//    }
//    tag.setTag("defined_mappings", list);
  }

  @Override
  public void readDataFromNBT(NBTTagCompound tag) {
//    NBTTagList list = tag.getTagList("defined_mappings", NBT.TAG_COMPOUND);
//    definedItems = new HashSet<GroupItemMapping>();
//    for (int i = 0; i < list.tagCount(); i++) {
//      NBTTagCompound mappingTag = list.getCompoundTagAt(i);
//      GroupItemMapping mapping = GroupItemMapping.fromNBT(mappingTag);
//      definedItems.add(mapping);
//    }
  }

  @Override
  public void clearData() {
//    definedItems.clear();
//    markDirty();
  }

//  @Override
//  public String diskKeyType() {
//    return "mapping";
//  }
//
//  @Override
//  public boolean onPreTransferToDiskKey(ItemStack validDiskKey) {
//    return true;
//  }
//
//  @Override
//  public void onPostTransferToDiskKey(ItemStack validDiskKey) {
//
//  }
//
//  @Override
//  public boolean onPreTransferFromDiskKey(ItemStack validDiskKey) {
//    return true;
//  }
//
//  @Override
//  public void onPostTransferFromDiskKey(ItemStack validDiskKey) {
//  }

}
