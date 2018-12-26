package com.tntp.mnm.tileentity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tntp.mnm.api.db.GroupItemMapping;

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

}
