package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.gui.cont.ITileDataCont;

import net.minecraft.inventory.Slot;

public class TileDataGroup extends STilePOB implements ITileDataCont {

  public TileDataGroup() {
    super(3);
  }

  public void addGroupNameToList(List<String> groups) {

  }

  @Override
  public String getContainerGui() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean canReadData() {
    // TODO Auto-generated method stub
    return false;
  }
}
