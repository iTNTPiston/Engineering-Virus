package com.tntp.mnm.tileentity;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.tntp.mnm.api.db.IQuery;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.db.QueryExecuter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * controller
 * 
 * @author iTNTPiston
 *
 */
public class TileCentralProcessor extends STile {
  /*
   * A TileCentral Processor is associated with a Mainframe object
   */
  private Mainframe mainframe;
  private PriorityQueue<QueryExecuter> queue;
  private int executionLeft;
  /**
   * task per tick, depends on CPU tier and ACUs
   */
  private int executionPerTick;
  /**
   * maxmium tasks it can store (depends on RAM)
   */
  private int maxQueueSize;

  private int scanCD;
  private int scanTotal = 200;

  /**
   * Debug mode will prevent new query to be added to the queue. When the old
   * queries finish executing, the Mainframe can be debugged.
   */
  private boolean debugMode;

  public TileCentralProcessor() {
    mainframe = new Mainframe(this, generateRandomMainframeID());
    queue = new PriorityQueue<QueryExecuter>();
    executionPerTick = 1;
    executionLeft = 0;
    maxQueueSize = 10;
  }

  public Mainframe getMainframe() {
    return mainframe;
  }

  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      // init mainframe
      if (mainframe.getWorld() == null) {
        mainframe.setWorld(worldObj);
      }
      if (scanCD <= 0) {
        // mandatory scan
        mainframe.scan();
        scanCD = scanTotal;
      } else {
        scanCD--;
      }

      // execution power check
      //
      if (executionLeft < executionPerTick) {
        executionLeft += executionPerTick;
      }
      // execution loop
      while (executionLeft > 0) {
        if (queue.isEmpty())
          break;// no more task
        System.out.println("Executing Query");
        QueryExecuter qe = queue.poll();
        if (qe.execute(mainframe))// only if it is executed successfully
          executionLeft--;
      }

      mainframe.setNeedScan();
    }
  }

  public boolean addQuery(QueryExecuter query) {
    System.out.println("CPU received Query");
    if (!debugMode && queue.size() < maxQueueSize) {
      // debug mode disallow new query to be queued.
      System.out.println("Query Queued");
      queue.add(query);
      return true;
    }
    return false;
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    NBTTagCompound mf = new NBTTagCompound();
    mainframe.writeToNBT(mf);
    tag.setTag("mainframe", mf);
    tag.setString("mf_rid", mainframe.mainframeRandomID);
    tag.setBoolean("debug", debugMode);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    String id = tag.getString("mf_rid");
    NBTTagCompound mf = (NBTTagCompound) tag.getTag("mainframe");
    mainframe = new Mainframe(this, id);
    mainframe.readFromNBT(mf);
    debugMode = tag.getBoolean("debug");
  }

  public void setDebugMode(boolean mode) {
    debugMode = mode;
  }

  /**
   * 
   * @return true if debug mode is enabled on the CPU
   */
  public boolean isDebugModeOn() {
    return debugMode;
  }

  /**
   * 
   * @return true if debug mode is enabled and the queue is empty
   */
  public boolean isDebugModeReady() {
    return debugMode && queue.isEmpty();
  }

  public static String generateRandomMainframeID() {
    StringBuilder build = new StringBuilder();
    SecureRandom random = new SecureRandom();
    for (int i = 0; i < 50; i++) {
      char next = (char) ('A' + random.nextInt(26));
      build.append(next);
    }
    return build.toString();
  }

  /**
   * Create a new instance of the mainframe
   */
  public boolean resetMainframeForRecovery(String oldID) {
    mainframe = new Mainframe(this, generateRandomMainframeID());
    mainframe.setWorld(worldObj);
    mainframe.scan();
    return mainframe.recover(oldID);
  }
}
