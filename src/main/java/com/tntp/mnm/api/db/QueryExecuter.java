package com.tntp.mnm.api.db;

import com.tntp.mnm.tileentity.STile;

import net.minecraft.inventory.IInventory;

public class QueryExecuter implements Comparable<QueryExecuter> {
  private IQuery query;
  private IInventory inv;
  private int start;
  private int end;

  public QueryExecuter(IQuery q, IInventory v, int s, int e) {
    query = q;
    inv = v;
    start = s;
    end = e;
  }

  /**
   * 
   * @param mainframe
   * @return true if the execution was successfull
   */
  public boolean execute(Mainframe mainframe) {
    if (inv instanceof STile) {// TileEntity validity check
      if (!((STile) inv).isValidInWorld())
        return false;
    }
    System.out.println("Query Execution Start");
    query.execute(mainframe, inv, start, end);
    return true;
  }

  @Override
  public int compareTo(QueryExecuter o) {
    // high priority value is executed first
    return o.query.getPriority() - query.getPriority();
  }

}
