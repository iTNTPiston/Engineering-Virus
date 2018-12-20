package com.tntp.mnm.api.db;

import net.minecraft.inventory.IInventory;

public interface IQuery {
  /**
   * Execute the query
   * 
   * @param mf        the mainframe to execute the query on
   * @param inv       the inventory that the query interacts with, typically the
   *                  machine that sends the query
   * @param startSlot used to specify slots that the query should touch
   * @param endSlot
   */
  public void execute(Mainframe mf, IInventory inv, int startSlot, int endSlot);

  public int getPriority();
}
