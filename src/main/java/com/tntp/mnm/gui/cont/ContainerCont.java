package com.tntp.mnm.gui.cont;

import java.util.List;

import com.tntp.mnm.gui.SContainer;
import com.tntp.mnm.tileentity.STile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerCont extends SContainer {
  private ITileCont tile;

  public ContainerCont(IInventory playerInventory, int slots, ITileCont machine) {
    super(playerInventory, slots, machine);
    tile = machine;
  }

  @Override
  public void setupMachineSlots(IInventory machine) {
    ITileCont t = (ITileCont) machine;
    List<Slot> slots = t.getContainerSlots();
    for (Slot s : slots) {
      this.addSlotToContainer(s);
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer p_75145_1_) {
    return ((STile) tile).isValidInWorld();
  }

}
