package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.Mainframe;
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
      if (tile != null && !(tile instanceof TileNeithernetPort)) {
        tile.addFinalTilesTo(list);
      }
    }
  }

  @Override
  public boolean isFinalTile() {
    return false;
  }

  @Override
  public Mainframe connectToMainframe() {
    return port.getMainframe();
  }
}
