package com.tntp.mnm.gui.cont;

import com.tntp.mnm.init.MNMResources;

import net.minecraft.inventory.IInventory;

public class GuiContQueryBuilder extends GuiCont {

  public GuiContQueryBuilder(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_query_builder_overlay.png");
  }

}
