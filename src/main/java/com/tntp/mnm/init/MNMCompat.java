package com.tntp.mnm.init;

import java.util.HashMap;
import java.util.Map.Entry;

import com.tntp.mnm.compat.ICompat;
import com.tntp.mnm.compat.nei.NEICompat;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.Loader;

/**
 * Core class for loading compatibilities
 * 
 * @author iTNTPiston
 *
 */
public class MNMCompat {
  public static void loadCompats(boolean clientSide) {
    HashMap<String, Object> compats = new HashMap<String, Object>();
    compats.put("NotEnoughItems", new NEICompat());

    for (Entry<String, Object> e : compats.entrySet()) {
      if (Loader.isModLoaded(e.getKey())) {
        DebugUtil.log.info("Loading Compat for " + e.getKey());
        try {
          ((ICompat) e.getValue()).load(clientSide);
        } catch (Exception e1) {
          DebugUtil.log.error("Fail to load " + e.getKey() + " Compat");
          e1.printStackTrace();
        }
      } else {
        DebugUtil.log.info("Skip Compat for " + e.getKey());
      }
    }
  }
}
