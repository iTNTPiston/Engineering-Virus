package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.Port;
import com.tntp.mnm.api.neither.NeitherPipe;

/**
 * A Neithernet port on board, different from a routing neithernet port
 * 
 * @author iTNTPiston
 *
 */
public class TileNeithernetPort extends STileNeithernet {
  private Port<STileNeithernet> port;

  public Port<STileNeithernet> getPort() {
    return port;
  }

  @Override
  public void addFinalTilesTo(List<STileNeithernet> list) {
    if (port.getMainframe() != null && getPipe() != null) {
      STileNeithernet tile = this.getPipe().getEnd(worldObj);
      if (tile != null) {
        tile.addFinalTilesTo(list);
      }
    }
  }
}
