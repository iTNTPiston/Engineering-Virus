package com.tntp.mnm.gui.structure;

import java.util.List;

import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class GuiStructureHeatCollectorFirewall extends GuiStructure {

  public GuiStructureHeatCollectorFirewall(IInventory player, String title, int x, int y, int z) {
    super(player, title, x, y, z);
  }

  @Override
  public void setupStructure() {
    ItemStack hc = new ItemStack(MNMBlocks.heat_collector_firewall);
    setMainStack(hc);
    Structure heatCollector = this.newStructure(hc, 1f, 0.2f, 0.2f, null);
    icons.add(heatCollector);
    Structure firewall = this.newStructure(new ItemStack(MNMBlocks.firewall), 1f, 1f, 0.2f,
        LocalUtil.localize("mnm.gui.struct.heat_collector.boost"));
    icons.add(firewall);
    for (int x = 1; x <= 3; x++) {
      for (int z = 1; z <= 3; z++) {
        if (x == 2 && z == 2)
          this.setStructureAt(heatCollector, x, 0, z);
        else
          this.setStructureAt(firewall, x, 0, z);
      }
    }

  }

  protected List<String> getMainTooltip(List<String> emptyList) {
    return LocalUtil.localizeList("mnm.gui.struct.heat_collector.main_");
  }
}
