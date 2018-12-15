package com.tntp.mnm.tileentity;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.util.IntHashMap;

public class TileDataStorage extends STileData {
  private HashMap<Integer, Integer> map;

  public TileDataStorage(int size) {
    super(size);
  }

  @Override
  public int getUsedSpace() {
    int size = map.size();
    for (int value : map.values()) {
      size += spaceNeeded(value);
    }
    return size;
  }

  public HashMap<Integer, Integer> getData() {
    return map;
  }

  /**
   * 
   * @param query
   * @return true if this call terminates the query
   */
  public boolean takeAway(HashMap<Integer, Integer> query) {
    boolean finished = true;
    for (Entry<Integer, Integer> e : query.entrySet()) {
      if (e.getValue() == 0) {
        continue;// skip empty ones
      }
      int key = e.getKey();
      int value = e.getValue();
      int stock = map.containsKey(key) ? map.get(key) : 0;
      if (stock <= value) {
        value -= stock;
        e.setValue(value);
        map.remove(key);
      } else {
        stock -= value;
        value = 0;
        e.setValue(0);
        map.put(key, stock);
      }
      // If there are leftovers
      if (value != 0)
        finished = false;
    }
    return finished;
  }

  public boolean putIn(HashMap<Integer, Integer> query) {
    boolean finished = true;
    for (Entry<Integer, Integer> e : query.entrySet()) {
      if (e.getValue() == 0) {
        continue;
      }

      int key = e.getKey();
      int value = e.getValue();

      int additionalSpace = 0;
      int stock = 0;
      if (!map.containsKey(key)) {
        additionalSpace = 1;
      } else {
        stock = map.get(key);
      }
      int spaceLeft = this.getTotalSpaceFromDisks() - this.getUsedSpace() - additionalSpace;
      int maximumPut = itemCapacity(spaceLeft);
      if (value <= maximumPut) {
        map.put(key, stock + value);
        value = 0;
        e.setValue(value);
      } else {
        map.put(key, stock + maximumPut);
        value -= maximumPut;
        e.setValue(value);
      }

      if (value != 0)
        finished = false;
    }
    return finished;
  }

  public int spaceNeeded(int stackSize) {
    return (int) Math.ceil(stackSize / 256.0);
  }

  public int itemCapacity(int dataspace) {
    if (dataspace < 0)
      return 0;
    return dataspace * 256;
  }

}
