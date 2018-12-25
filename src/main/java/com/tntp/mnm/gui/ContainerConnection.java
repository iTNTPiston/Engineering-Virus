package com.tntp.mnm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerConnection extends SContainer {

  public ContainerConnection(IInventory playerInventory, ItemStack left, ItemStack right, ItemStack pipe) {
    super(playerInventory, 3, new InventoryDisplay(left, pipe, right));

  }

  @Override
  public void setupMachineSlots(IInventory i) {
    this.addSlotToContainer(new SlotDecorative(i, 0, 35, 55));
    this.addSlotToContainer(new SlotDecorative(i, 1, 80, 55));
    this.addSlotToContainer(new SlotDecorative(i, 2, 125, 55));

  }

  @Override
  public boolean canInteractWith(EntityPlayer p_75145_1_) {
    return true;
  }

}
