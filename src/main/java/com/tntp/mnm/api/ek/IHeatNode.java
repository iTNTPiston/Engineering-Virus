package com.tntp.mnm.api.ek;

public interface IHeatNode {
  public int getEK();

  public int getMaxEK();

  public void setEK(int ek);

  public void setMaxEK(int ek);

  public HeatPipe[] getIn();

  public HeatPipe[] getOut();

  public boolean connectPipe(HeatPipe pipe, int comingFrom, boolean toSink);
}
