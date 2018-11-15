package com.tntp.mnm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.tntp.mnm.block.BlockHeatPipe;

public class DebugUtil {
  public static final int VERSION = 1;
  public static Logger log = LogManager.getLogger("EV-Log");

  @Test
  public void test() {
    for (int i = 0; i < 16; i++) {
      BlockHeatPipe.sideToMeta(BlockHeatPipe.metaToSide(i));

    }
  }
}
