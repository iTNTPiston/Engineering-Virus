package com.tntp.mnm.gui.process;

import com.tntp.mnm.gui.SContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;

public abstract class ContainerProcess extends SContainer {
  private ITileProcess tile;
  private int lastTotal;
  private int lastCurrent;

  public ContainerProcess(IInventory playerInventory, ITileProcess tile) {
    super(playerInventory, tile.getSizeInventory(), tile);
    this.tile = tile;
    tile.openInventory();
  }

  public void onContainerClosed(EntityPlayer player) {
    tile.closeInventory();
  }

  @Override
  public void addCraftingToCrafters(ICrafting craft) {
    super.addCraftingToCrafters(craft);
    craft.sendProgressBarUpdate(this, 0, tile.getCurrentProgress());
    craft.sendProgressBarUpdate(this, 1, tile.getTotalProgress());
  }

  @Override
  public void updateProgressBar(int bar, int progress) {
    if (bar == 0)
      tile.setCurrentProgress(progress);
    else if (bar == 1)
      tile.setTotalProgress(progress);
  }

  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    int current = tile.getCurrentProgress();
    int total = tile.getTotalProgress();
    for (Object obj : this.crafters) {
      ICrafting c = (ICrafting) obj;
      if (lastCurrent != current) {
        c.sendProgressBarUpdate(this, 0, current);
      }
      if (lastTotal != total) {
        c.sendProgressBarUpdate(this, 1, total);
      }
    }
    lastCurrent = current;
    lastTotal = total;
  }

  public ITileProcess getTile() {
    return tile;
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return tile != null && tile.isUseableByPlayer(player);
  }

}
