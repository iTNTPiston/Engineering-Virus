package com.tntp.mnm.compat.nei;

import java.util.Collections;
import java.util.List;

import com.tntp.mnm.gui.GuiMain;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class IGuiMainHandler implements INEIGuiHandler {

  @Override
  public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
    return currentVisibility;
  }

  @Override
  public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
    return Collections.emptyList();
  }

  @Override
  public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
    return null;
  }

  @Override
  public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
    return false;
  }

  @Override
  public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
    if (gui instanceof GuiMain) {
      return ((GuiMain) gui).hideItemPanel(x, y, w, h);
    }
    return false;
  }

}
