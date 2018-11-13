package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatSource;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.tileentity.TileEntity;

public class TileHeatCollectorFirewall extends STileHeatNode implements IHeatSource {
  private static int BASE = 4, BOOST = 2;
  private int ek;
  private int rate;
  private int maxEK;

  public TileHeatCollectorFirewall() {
    ek = 0;
    rate = 0;

  }

  @Override
  public boolean isSourceSide(int side) {
    return side == DirUtil.D_U_PY;
  }

}
