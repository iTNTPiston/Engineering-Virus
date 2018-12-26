package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class TileBasicDiskStorage extends STileDataStorage implements ITileSecuredCont {
  private Security security;

  public TileBasicDiskStorage() {
    super(1);
    security = new Security(this);
  }

  @Override
  public String getContainerGui() {
    return "GuiContBasicDiskStorage";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    slots.add(new SlotDecorative(this, 0, 9, 21));
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

  @Override
  public Security getSecurity() {
    return security;
  }

}
