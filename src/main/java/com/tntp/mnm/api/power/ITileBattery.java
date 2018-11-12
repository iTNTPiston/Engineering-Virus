package com.tntp.mnm.api.power;

public interface ITileBattery extends ITileWire {
  public int getMaxEnergy();

  public int getCurrentEnergy();

  public void setCurrentEnergy(int e);

  public IElectron produceElectron();

  public void destroyElectron(IElectron e);

  public int getEMF();

  public int produceEMF();
}
