package com.tntp.mnm.tileentity;

import java.util.HashSet;
import java.util.List;

import com.tntp.mnm.api.db.GroupItemDef;

public class STileDataGroupDefiner extends STileData {
  private HashSet<GroupItemDef> definedItems;

  public STileDataGroupDefiner(int size) {
    super(size);
  }

  @Override
  public int getUsedSpace() {
    return definedItems.size() * 4;
  }

  public boolean define(GroupItemDef def) {
    if (this.getUsedSpace() + 4 < this.getTotalSpaceFromDisks()) {
      definedItems.add(def);
      return true;
    }
    return false;
  }

}
