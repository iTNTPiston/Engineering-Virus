package com.tntp.mnm.gui.cont;

import com.tntp.mnm.init.MNMResources;

import net.minecraft.inventory.IInventory;

public class GuiContSecurityEncoder extends GuiCont {

  public GuiContSecurityEncoder(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/guiCont_securityencoder_overlay.png");
  }

}
