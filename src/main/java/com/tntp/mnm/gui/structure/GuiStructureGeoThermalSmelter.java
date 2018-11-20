package com.tntp.mnm.gui.structure;

import java.util.List;

import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class GuiStructureGeoThermalSmelter extends GuiStructure {

  public GuiStructureGeoThermalSmelter(IInventory player, String title, int x, int y, int z) {
    super(player, title, x, y, z);
  }

  @Override
  public void setupStructure() {
    ItemStack mainStack = new ItemStack(MNMBlocks.blockGeoThermalSmelter);
    setMainStack(mainStack);
    Structure smelter = newStructure(mainStack, 1f, 0.2f, 0.2f, null);
    icons.add(smelter);
    Structure firewall = newStructure(new ItemStack(MNMBlocks.blockFirewall), 1f, 0.8f, 0.2f, null);
    icons.add(firewall);
    ItemStack heatPipe = new ItemStack(MNMBlocks.blockHeatPipe);
    Structure downPipe = newStructure(heatPipe, 0.2f, 1f, 0.2f,
        LocalUtil.localize("mnm.gui.struct.geo_thermal_smelter.down_pipe"));
    Structure topPipe = newStructure(heatPipe, 1f, 0.2f, 1f,
        LocalUtil.localize("mnm.gui.struct.geo_thermal_smelter.top_pipe"));
    icons.add(downPipe);
    icons.add(topPipe);
    Structure chimney = newStructure(new ItemStack(MNMBlocks.blockChimney), 0.2f, 0.2f, 1f, null);
    icons.add(chimney);
    for (int x = 1; x <= 3; x++) {
      for (int z = 1; z <= 3; z++) {
        for (int y = 0; y <= 1; y++)
          setStructureAt(firewall, x, y, z);
      }
    }
    setStructureAt(smelter, 2, 0, 3);
    setStructureAt(downPipe, 2, 0, 2);
    setStructureAt(chimney, 2, 1, 3);
    setStructureAt(topPipe, 2, 2, 3);
  }

  protected List<String> getMainTooltip(List<String> emptyList) {
    return LocalUtil.localizeList("mnm.gui.struct.geo_thermal_smelter.main_");
  }

}
