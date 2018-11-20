package com.tntp.mnm.gui.structure;

import com.tntp.mnm.gui.SContainer;

import net.minecraft.inventory.IInventory;

public class ContainerStructure extends SContainer {

  public ContainerStructure(IInventory playerInventory) {
    super(playerInventory, 0, null);
  }

  @Override
  public void setupMachineSlots(IInventory i) {
  }

}
