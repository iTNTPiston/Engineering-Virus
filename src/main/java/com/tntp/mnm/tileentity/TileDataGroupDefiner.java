package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.item.ItemDataGroupChip;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TileDataGroupDefiner extends STilePOB implements ITileCont {
  public TileDataGroupDefiner() {
    super(2);
  }

  public void defineGroup(String groupName) {
    // get icon stack
    ItemStack iconStack = getStackInSlot(0);

    // get chip
    ItemStack chipStack = getStackInSlot(1);

    ItemDataGroupChip.writeGroupToItem(chipStack, groupName, iconStack);

  }

  @Override
  public String getContainerGui() {
    return "GuiContDataGroupDefiner";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    slots.add(new Slot(this, 0, 105, 19));
    slots.add(new Slot(this, 1, 105, 46));
  }

}
