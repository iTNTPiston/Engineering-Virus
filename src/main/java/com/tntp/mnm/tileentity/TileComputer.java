package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.digital.IDisk;
import com.tntp.mnm.api.digital.IMemory;
import com.tntp.mnm.api.digital.IProcessor;

import net.minecraft.tileentity.TileEntity;

public class TileComputer extends TileEntity {
  private IProcessor[] processors;
  private IMemory memory;
  private IDisk disk;

}
