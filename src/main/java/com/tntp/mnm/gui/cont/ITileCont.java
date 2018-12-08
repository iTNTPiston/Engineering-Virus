package com.tntp.mnm.gui.cont;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public interface ITileCont extends IInventory {
  public void addContainerSlots(List<Slot> slots);
}
