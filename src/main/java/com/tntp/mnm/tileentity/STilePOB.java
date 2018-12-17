package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.db.Port;

/**
 * Port on board
 * 
 * @author iTNTPiston
 *
 */
public class STilePOB extends STileInventory {
  private Port<STilePOB> port;

  public STilePOB(int size) {
    super(size);
  }

  public Port<STilePOB> getPort() {
    return port;
  }

}
