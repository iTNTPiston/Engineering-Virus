package com.tntp.mnm.gui.cont;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.tileentity.STileData;

import net.minecraft.inventory.IInventory;

public class GuiContDiskStorage extends GuiContData {

  public GuiContDiskStorage(IInventory playerInventory, STileData machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_disk_storage_overlay.png");
  }

}
