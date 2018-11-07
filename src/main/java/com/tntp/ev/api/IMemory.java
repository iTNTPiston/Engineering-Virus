package com.tntp.ev.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IMemory {
  public int memSize();

  public IProgram access(IProgram accessor, String memLoc);

  public boolean canAccess(IProgram accessor);

  public NBTTagCompound access(IProgram accessor, int memLoc);
}
