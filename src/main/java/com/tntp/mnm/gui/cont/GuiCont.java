package com.tntp.mnm.gui.cont;

import com.tntp.mnm.gui.SGui;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class GuiCont extends SGui {

  public GuiCont(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(new ContainerCont(playerInventory, machine), machine.getInventoryName(), x, y, z);
  }

}
