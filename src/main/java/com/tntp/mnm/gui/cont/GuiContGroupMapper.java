package com.tntp.mnm.gui.cont;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.tileentity.STileData;

import net.minecraft.inventory.IInventory;

public class GuiContGroupMapper extends GuiCont {
  public GuiContGroupMapper(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_group_mapper_overlay.png");
  }
}
