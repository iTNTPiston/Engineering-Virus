package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.gui.cont.ITileSecuredCont;

import net.minecraft.inventory.Slot;

public class TileDiskStorage extends STileDataStorage implements ITileSecuredCont {

  public TileDiskStorage() {
    super(1);
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

}
