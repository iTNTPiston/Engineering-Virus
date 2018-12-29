package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.gui.cont.ITileDataCont;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Slot;

/**
 * Use this to organize the data, calls MF functions
 * 
 * @author iTNTPiston
 *
 */
public class TileDataIntegrityChipset extends STilePOB implements ITileDataCont {

  private boolean needsUpdate;
  private boolean mfDebugMode;

  public TileDataIntegrityChipset() {
    super(0);
  }

  @Override
  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (needsUpdate) {
        boolean debug = false;
        Mainframe mf = getPort().getMainframe();
        if (mf != null) {
          debug = mf.isReadyToDebug();
        }
        if (mfDebugMode != debug) {
          mfDebugMode = debug;
          worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), EVENT_DEBUG_MODE, mfDebugMode ? 1 : 0);
        }
      }
    }
  }

  @Override
  public void openInventory() {
    needsUpdate = true;
    mfDebugMode = false;
  }

  @Override
  public void closeInventory() {
    needsUpdate = false;
  }

  @Override
  public String getContainerGui() {
    return "GuiContDataIntegrityChipset";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {

  }

  @Override
  public boolean canReadData() {
    return getPort().getMainframe() != null;
  }

  @Override
  public boolean receiveClientEvent(int event, int param) {
    if (super.receiveClientEvent(event, param))
      return true;
    if (event == EVENT_DEBUG_MODE) {
      mfDebugMode = param == 1;
      return true;
    }
    return false;
  }

  public void receiveClientAction(int buttonID, int flag) {
    // 0 - debug
    // 1 - execute definition
    Mainframe mf = getPort().getMainframe();
    if (mf != null) {
      if (buttonID == 0) {
        if (mf.isReadyToDebug()) {
          mf.finishDebugging();
        } else if (!mf.isBootingDebug()) {
          mf.startDebugging();
        }
      } else if (buttonID == 1) {
        if (mf.isReadyToDebug()) {
          mf.organizeDefinitions(flag);
        }
      }
    }
  }

  /**
   * for gui button state
   * 
   * @return
   */
  @SideOnly(Side.CLIENT)
  public boolean isMFInDebugMode() {
    return mfDebugMode;
  }

}
