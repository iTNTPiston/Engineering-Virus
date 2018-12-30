package com.tntp.mnm.tileentity;

import net.minecraft.tileentity.TileEntity;

/**
 * Super class of (hopefully) all tileentities in MNM
 * 
 * @author iTNTPiston
 *
 */
public class STile extends TileEntity {
  protected static final int EVENT_MF_CONNECTION = 0, EVENT_DISK_NUM = 1, EVENT_DEBUG_MODE = 2;

  public boolean isValidInWorld() {
    return this.hasWorldObj() && this.worldObj.getTileEntity(xCoord, yCoord, zCoord) == this;
  }

  /**
   * Called by breakBlock() before items are dropped, useful for setting
   * decorative slots to null
   */
  public void onBreakingContainer() {

  }
}
