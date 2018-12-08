package com.tntp.mnm.gui.cont;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public interface ITileCont extends IInventory {
  public String getContainerGui();

  public void addContainerSlots(List<Slot> slots);
}
