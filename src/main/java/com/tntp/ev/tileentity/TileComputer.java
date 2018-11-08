package com.tntp.ev.tileentity;

import com.tntp.ev.api.digital.IDisk;
import com.tntp.ev.api.digital.IMemory;
import com.tntp.ev.api.digital.IProcessor;

import net.minecraft.tileentity.TileEntity;

public class TileComputer extends TileEntity {
  private IProcessor[] processors;
  private IMemory memory;
  private IDisk disk;

}
