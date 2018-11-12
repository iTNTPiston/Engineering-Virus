package com.tntp.mnm.api.power;

public interface ITileResistor extends ITileWire {
  public int getPower();

  public int getHeat();

  public int getSpecificHeat();

  public void dissipate(int energy);

  public void loseHeatToEnvironment();

  public void aquirePower();

  public void work();

  public void losePower();

  public boolean isWorking();
}
