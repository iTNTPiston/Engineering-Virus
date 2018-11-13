package com.tntp.mnm.api.ek;

public interface IHeatNode {
  public HeatPipe[] getIn();

  public HeatPipe[] getOut();

  public boolean connectPipe(HeatPipe pipe, int comingFrom, boolean toSink);
}
