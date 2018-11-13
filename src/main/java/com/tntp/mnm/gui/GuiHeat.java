package com.tntp.mnm.gui;

import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.gui.container.ContainerHeat;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;

public class GuiHeat extends GuiMain {
  IHeatNode heatNode;

  public GuiHeat(IInventory player, IHeatNode tile) {
    super(new ContainerHeat(player, tile));
    setTabAt(5, GuiTabType.HEAT, "GuiHeat");
    setTabAt(4, GuiTabType.HEAT, "GuiHeat");
    setTabAt(3, GuiTabType.HEAT, "GuiHeat");
    setTabAt(2, GuiTabType.HEAT, "GuiHeat");
    setTabAt(1, GuiTabType.HEAT, "GuiHeat");
    setTabAt(0, GuiTabType.HEAT, "GuiHeat");
    heatNode = tile;
  }

  protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
    this.fontRendererObj.drawString(heatNode.getEK() + " / " + heatNode.getMaxEK(), 8, 6, 4210752);

  }

}
