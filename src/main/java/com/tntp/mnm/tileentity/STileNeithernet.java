package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.neither.NeitherPipe;

/**
 * Neithernet tiles are connected 1-to-1
 * 
 * @author iTNTPiston
 *
 */
public class STileNeithernet extends STile {
  private NeitherPipe pipe;

  public int getPortSide() {
    return this.getBlockMetadata();
  }

  public NeitherPipe getPipe() {
    return pipe;
  }

  /**
   * Exclude intermediate connection such as routers, which should override
   * this<br>
   * Ports must also override this
   * 
   * @param list
   */
  public void addFinalTilesTo(List<STileNeithernet> list) {
    list.add(this);
  }

}
