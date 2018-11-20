package com.tntp.mnm.gui.process;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerProcessGeoThermalSmelter extends ContainerProcess {

  public ContainerProcessGeoThermalSmelter(IInventory playerInventory, ITileProcess tile) {
    super(playerInventory, tile);
  }

  @Override
  public void setupMachineSlots() {
    int x = 26;
    int y = 49;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        this.addSlotToContainer(new Slot(getTile(), i * 2 + j, x + 18 * i, y + 18 * j));
      }
    }
    x = 116;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        this.addSlotToContainer(new SlotOutput(getTile(), 4 + i * 2 + j, x + 18 * i, y + 18 * j));
      }
    }
  }

}
