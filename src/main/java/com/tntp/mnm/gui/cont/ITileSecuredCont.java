package com.tntp.mnm.gui.cont;

import com.tntp.mnm.api.security.Security;

/**
 * Those gui must be opened with a security card
 * 
 * @author iTNTPiston
 *
 */
public interface ITileSecuredCont extends ITileCont {
  public Security getSecurity();
}
