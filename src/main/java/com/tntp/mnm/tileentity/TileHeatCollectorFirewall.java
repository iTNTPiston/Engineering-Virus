package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatSource;

import net.minecraft.tileentity.TileEntity;

public class TileHeatCollectorFirewall extends TileEntity implements IHeatSource {
  private static int BASE = 4, BOOST = 2, RESCAN = 200;
  private int ek;
  private int rate;
  private int rescanCD;
  private boolean rescaned;

  public TileHeatCollectorFirewall() {
    ek = 0;
    rate = 0;
    rescanCD = 10;
    rescaned = false;
  }

  @Override
  public List<HeatPipe> getIn() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<HeatPipe> getOut() {
    // TODO Auto-generated method stub
    return null;
  }
}
