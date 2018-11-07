package com.tntp.ev.tileentity;

import com.tntp.ev.api.IDisk;
import com.tntp.ev.api.IMemory;
import com.tntp.ev.api.IProcessor;

import net.minecraft.tileentity.TileEntity;

public class TileComputer extends TileEntity {
  private IProcessor[] processors;
  private IMemory memory;
  private IDisk disk;

}
