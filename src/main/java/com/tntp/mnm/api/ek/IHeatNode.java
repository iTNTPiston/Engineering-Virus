package com.tntp.mnm.api.ek;

public interface IHeatNode {
  public int getEK();

  public int getMaxEK();

  public void setEK(int ek);

  public void setMaxEK(int ek);

  public HeatPipe[] getIn();

  public HeatPipe[] getOut();

  public int connectPipe(HeatPipe pipe, int comingFrom, int toSink);

  public String getInventoryName();
}
