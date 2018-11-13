package com.tntp.mnm.api.ek;

/**
 * Marker interface for heat pipes
 * 
 * @author iTNTPiston
 *
 */
public interface IHeatConnector {
  public boolean forwardPipe(HeatPipe pipe, int comingFrom, boolean toSink);
}
