package com.tntp.mnm.gui.cont;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;

import net.minecraft.inventory.IInventory;

public class GuiContDataDefinitionTerminal extends GuiCont {
  private int row = 0;

  public GuiContDataDefinitionTerminal(IInventory playerInventory, TileDataDefinitionTerminal machine, int x, int y,
      int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_data_definition_terminal_overlay.png");

  }

}
