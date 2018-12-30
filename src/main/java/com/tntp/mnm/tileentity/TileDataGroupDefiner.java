package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.gui.cont.ITileDataCont;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.item.ItemDataGroupChip;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDataGroupDefiner extends STilePOB implements ITileDataCont {

  public TileDataGroupDefiner() {
    super(2);
  }

  public void defineGroup(String groupName) {
    // get icon stack
    ItemStack iconStack = getStackInSlot(0);

    // get chip
    ItemStack chipStack = getStackInSlot(1);

    ItemDataGroupChip.writeGroupToItem(chipStack, groupName, iconStack);
    this.setInventorySlotContents(1, chipStack);
    markDirty();
  }

  @Override
  public String getContainerGui() {
    return "GuiContDataGroupDefiner";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    slots.add(new Slot(this, 0, 126, 19));
    slots.add(new Slot(this, 1, 126, 46));
  }

  @Override
  public boolean canReadData() {
    return true;
  }

}
