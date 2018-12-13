package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.Mainframe;

import net.minecraft.tileentity.TileEntity;

/**
 * controller
 * 
 * @author iTNTPiston
 *
 */
public class TileCentralProcessor extends STile {
  private Mainframe mainframe;

  public TileCentralProcessor() {
    mainframe = new Mainframe(this);
  }

  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (mainframe.getWorld() == null) {
        mainframe.setWorld(worldObj);
      }
    }
  }
}
