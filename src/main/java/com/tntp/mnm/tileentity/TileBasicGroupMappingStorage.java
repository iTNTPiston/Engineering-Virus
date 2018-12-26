package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class TileBasicGroupMappingStorage extends STileDataGroupMapping implements ITileSecuredCont {
  private Security security;

  public TileBasicGroupMappingStorage() {
    super(3);
    security = new Security(this);
  }

  @Override
  public String getContainerGui() {
    return "GuiContBasicGroupMappingStorage";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    for (int i = 0; i < 3; i++) {
      slots.add(new SlotDecorative(this, i, 9 + i * 18, 21));
    }

  }

  @Override
  public Security getSecurity() {
    return security;
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    security.writeToNBT(tag);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    security = new Security(this);
    security.readFromNBT(tag);
  }

}
