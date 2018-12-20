package com.tntp.mnm.tileentity;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.tntp.mnm.api.db.IQuery;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.db.QueryExecuter;

import net.minecraft.tileentity.TileEntity;

/**
 * controller
 * 
 * @author iTNTPiston
 *
 */
public class TileCentralProcessor extends STile {
  private Mainframe mainframe;
  private PriorityQueue<QueryExecuter> queue;
  private int executionLeft;
  private int executionPerTick;
  private int maxQueueSize;

  public TileCentralProcessor() {
    mainframe = new Mainframe(this);
    queue = new PriorityQueue<QueryExecuter>();
  }

  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (mainframe.getWorld() == null) {
        mainframe.setWorld(worldObj);
      }
      if (executionLeft < executionPerTick) {
        executionLeft += executionPerTick;
      }
      while (executionLeft > 0) {
        if (queue.isEmpty())
          break;
        // TODO carry out execution
      }

      mainframe.setNeedScan();
    }
  }
}
