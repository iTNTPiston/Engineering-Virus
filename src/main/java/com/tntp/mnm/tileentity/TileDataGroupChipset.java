package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.gui.cont.ITileDataCont;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TileDataGroupChipset extends STilePOB implements ITileDataCont {

  public TileDataGroupChipset() {
    super(3);
  }

  public void addGroups(List<ItemStack> groups, String prefix) {

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
