package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.db.Mainframe;

/**
 * Change the mainframeID remembered by STileData
 * 
 * @author iTNTPiston
 *
 */
public class TileMainframeRecoveryChipset extends STilePOB {

  public TileMainframeRecoveryChipset() {
    super(0);
  }

  public boolean attemptToRecover() {
    Mainframe mf = getPort().getMainframe();
    if (mf == null)
      return false;
    return mf.signalRecovery();
  }

}
