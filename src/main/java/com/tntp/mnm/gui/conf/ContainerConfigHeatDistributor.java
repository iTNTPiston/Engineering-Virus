package com.tntp.mnm.gui.conf;

import java.awt.Container;

import com.tntp.mnm.gui.SContainer;
import com.tntp.mnm.tileentity.TileHeatDistributor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerConfigHeatDistributor extends SContainer {
  private TileHeatDistributor tile;
  private boolean[] last;

  public ContainerConfigHeatDistributor(IInventory playerInventory, TileHeatDistributor machine) {
    super(playerInventory, 6, machine);
    last = new boolean[6];
    tile = machine;
  }

  @Override
  public void addCraftingToCrafters(ICrafting craft) {
    super.addCraftingToCrafters(craft);
    for (int i = 0; i < 6; i++)
      craft.sendProgressBarUpdate(this, i, tile.isSinkSide(i) ? 1 : 0);
  }

  @Override
  public void updateProgressBar(int bar, int progress) {
    tile.setSide(bar, progress == 1);
  }

  public void setTileSides(int side, boolean in) {
    tile.setSide(side, in);
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (Object obj : this.crafters) {
      ICrafting c = (ICrafting) obj;
      for (int i = 0; i < 6; i++) {
        if (last[i] != tile.isSourceSide(i)) {
          c.sendProgressBarUpdate(this, i, tile.isSinkSide(i) ? 1 : 0);
        }
      }
    }
    for (int i = 0; i < 6; i++) {
      last[i] = tile.isSourceSide(i);
    }
  }

  @Override
  public void setupMachineSlots(IInventory machine) {
    for (int i = 0; i < 6; i++) {
      this.addSlotToContainer(new Slot(machine, i, 35 + i * 18, 50));
    }

  }

  @Override
  public boolean canInteractWith(EntityPlayer p_75145_1_) {
    return tile.isValidInWorld();
  }

}
