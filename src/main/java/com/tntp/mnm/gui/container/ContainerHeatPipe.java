package com.tntp.mnm.gui.container;

import com.tntp.mnm.api.ek.HeatPipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerHeatPipe extends SContainer {
  private ItemStack node1;
  private ItemStack node2;

  public ContainerHeatPipe(IInventory playerInventory, ItemStack node1, ItemStack node2) {
    super(playerInventory, 0);
    this.node1 = node1;
    this.node2 = node2;
  }

  public ItemStack getNode1() {
    return node1;
  }

  public ItemStack getNode2() {
    return node2;
  }

  @Override
  public void setupMachineSlots() {
  }

}
