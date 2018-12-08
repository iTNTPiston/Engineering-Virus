package com.tntp.mnm.gui.structure;

import com.tntp.mnm.gui.SContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerStructure extends SContainer {

  public ContainerStructure(IInventory playerInventory) {
    super(playerInventory, 0, null);
  }

  @Override
  public void setupMachineSlots(IInventory i) {
  }

  @Override
  public boolean canInteractWith(EntityPlayer p_75145_1_) {
    return true;
  }

}
