package com.tntp.mnm.api.db;

import com.tntp.mnm.tileentity.STile;

public class Port<T extends STile> {
  private Mainframe mf;
  private T tile;

  public Mainframe getMainframe() {
    return mf;
  }

  public T getTile() {
    return tile;
  }
}
