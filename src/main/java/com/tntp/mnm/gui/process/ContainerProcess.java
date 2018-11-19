package com.tntp.mnm.gui.process;

import com.tntp.mnm.gui.SContainer;

import net.minecraft.inventory.IInventory;

public class ContainerProcess extends SContainer {
  private ITileProcess tile;

  public ContainerProcess(IInventory playerInventory, ITileProcess tile) {
    super(playerInventory, tile.getSizeInventory());
  }

  @Override
  public void setupMachineSlots() {

  }

}
