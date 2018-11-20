package com.tntp.mnm.gui.process;

import net.minecraft.inventory.IInventory;

public interface ITileProcess extends IInventory {
  public String getProcessGui();

  public int getTotalProgress();

  public int getCurrentProgress();

  public void setTotalProgress(int p);

  public void setCurrentProgress(int p);
}
