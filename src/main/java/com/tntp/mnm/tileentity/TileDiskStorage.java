package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.cont.ITileSecuredCont;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class TileDiskStorage extends STileDataStorage implements ITileSecuredCont {
  private Security security;

  public TileDiskStorage() {
    super(1);
    security = new Security(this);
  }

  @Override
  public String getContainerGui() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    // TODO Auto-generated method stub

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
