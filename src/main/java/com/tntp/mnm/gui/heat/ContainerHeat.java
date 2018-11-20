package com.tntp.mnm.gui.heat;

import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.gui.SContainer;

import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;

public class ContainerHeat extends SContainer {
  private IHeatNode tile;
  private int lastEK;
  private int lastMaxEK;

  public ContainerHeat(IInventory playerInventory, IHeatNode tile) {
    super(playerInventory, 0, null);
    this.tile = tile;
  }

  @Override
  public void setupMachineSlots(IInventory i) {
  }

  public void addCraftingToCrafters(ICrafting craft) {
    super.addCraftingToCrafters(craft);
    craft.sendProgressBarUpdate(this, 0, tile.getEK());
    craft.sendProgressBarUpdate(this, 1, tile.getMaxEK());
  }

  public void updateProgressBar(int bar, int progress) {
    switch (bar) {
    case 0:
      tile.setEK(progress);
      break;
    case 1:
      tile.setMaxEK(progress);
      break;
    }
  }

  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    int ek = tile.getEK();
    int maxEk = tile.getMaxEK();
    for (Object obj : this.crafters) {
      ICrafting c = (ICrafting) obj;
      if (lastEK != ek) {
        c.sendProgressBarUpdate(this, 0, ek);
      }
      if (lastMaxEK != maxEk) {
        c.sendProgressBarUpdate(this, 1, maxEk);
      }
    }
    lastEK = ek;
    lastMaxEK = maxEk;
  }

}
