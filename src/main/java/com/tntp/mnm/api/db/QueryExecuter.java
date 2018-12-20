package com.tntp.mnm.api.db;

import net.minecraft.inventory.IInventory;

public class QueryExecuter implements Comparable<QueryExecuter> {
  public IQuery query;
  public IInventory inv;
  public int start;
  public int end;

  public QueryExecuter(IQuery q, IInventory v, int s, int e) {
    query = q;
    inv = v;
    start = s;
    end = e;
  }

  @Override
  public int compareTo(QueryExecuter o) {
    // high priority value is executed first
    return o.query.getPriority() - query.getPriority();
  }

}
