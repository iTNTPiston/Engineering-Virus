package com.tntp.mnm.api.power;

public interface ITileWire {
  /**
   * How many electrons can it hold?
   * 
   * @return
   */
  public int getCapacity();

  public IElectron getFirstElectron();

  public IElectron getLastElectron();

  public IElectron getElectron(int i);

  public boolean hasSpaceForMoreElectron();

  public int decideOutputSide();

  public boolean canSendElectron();

  public IElectron sendElectron();

  public void receiveElectron(IElectron toBeRecept, int side);

  public double getPotential(int side);

  public int[] getOutputSides();

  public int[] getInputSides();

  public boolean isOutputSide(int i);

  public boolean isInputSide(int i);

  /**
   * how many electrons can pass at a time
   * 
   * @return
   */
  public int getCrossSection();

  public int getEnergyLossDistance();

}
