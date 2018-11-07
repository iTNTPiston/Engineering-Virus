package com.tntp.ev.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IMemory {
  public int memSize();

  public IProgram accessProgram(IProgram accessor, String memLoc);

  public boolean canAccess(IProgram accessor);

  public Object access(IProgram accessor, String memLoc);

  public Object memWrite(IProgram accessor, String memLoc, Object obj);
}
