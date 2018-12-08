package com.tntp.mnm.gui.process;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.tileentity.TileGeoThermalSmelter;

import net.minecraft.inventory.IInventory;

public class GuiProcessGeoThermalSmelter extends GuiProcess {

  public GuiProcessGeoThermalSmelter(IInventory playerInventory, TileGeoThermalSmelter tile, int x, int y, int z) {
    super(new ContainerProcessGeoThermalSmelter(playerInventory, tile), tile.getInventoryName(), x, y, z, 77, 47,
        MNMResources.getResource("textures/guis/guiGeoThermalSmelter_overlay.png"));
  }

}
