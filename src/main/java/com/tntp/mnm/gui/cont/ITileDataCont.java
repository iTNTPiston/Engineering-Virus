package com.tntp.mnm.gui.cont;

public interface ITileDataCont extends ITileCont {
  /**
   * If the current situation allows player to open gui with data reader<br>
   * For example, if there are a data access port around, etc.
   * 
   * @return
   */
  public boolean canReadData();
}
