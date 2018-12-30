package com.tntp.mnm.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class DebugUtil {
  public static Logger log = LogManager.getLogger("MNM");

  @Test
  public void test() {
    HashMap<Integer, String> map = new HashMap<Integer, String>();
    map.put(1, "hey");
    map.put(2, "haha");
    for (Iterator<Entry<Integer, String>> iter = map.entrySet().iterator(); iter.hasNext();) {
      iter.next();
      iter.remove();
    }
    System.out.println(map.isEmpty());
  }
}
