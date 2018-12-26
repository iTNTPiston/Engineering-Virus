package com.tntp.mnm.gui.cont;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.tileentity.STileData;

import net.minecraft.inventory.IInventory;

public class GuiContBasicGroupMappingStorage extends GuiContData {

  public GuiContBasicGroupMappingStorage(IInventory playerInventory, STileData machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_basic_group_mapping_storage_overlay.png");
  }

}
