package com.tntp.mnm.gui.process;

import com.tntp.mnm.init.MNMResources;

public class GuiProcessGeoThermalSmelter extends GuiProcess {

  public GuiProcessGeoThermalSmelter(ContainerProcess container, String title, int x, int y, int z) {
    super(container, title, x, y, z, 77, 47,
        MNMResources.getResource("textures/guis/guiGeoThermalSmelter_overlay.png"));
  }

}
