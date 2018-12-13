package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.neither.NeitherPipe;

/**
 * Neithernet tiles are connected 1-to-1
 * 
 * @author iTNTPiston
 *
 */
public abstract class STileNeither extends STile {
  private NeitherPipe pipe;

  public int getPortSide() {
    return this.getBlockMetadata();
  }

}
