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
   * @return quantity left to be taken away
   */
  public int takeAway(int id, int qty) {
    // how much is stored
    int stock = map.containsKey(id) ? map.get(id) : 0;
    // stored is less than or equal need
    if (stock <= qty) {
      qty -= stock;
      map.remove(id);// take everything out
    } else {
      // stored has more than need
      stock -= qty;
      // need becomes 0
      qty = 0;
      map.put(id, stock);
    }
    // return qty not taken
    return qty;
  }

  /**
   * 
   * @param id
   * @param qty
   * @return qty that cannot be put in
   */
  public int putIn(int id, int qty) {
    int additionalSpace = 0;// space needed to store the definition
    int stock = 0;// stored

    if (!map.containsKey(id)) {
      additionalSpace = 1;
    } else {
      stock = map.get(id);
    }
    // space left in the disks, counting definition
    int spaceLeft = this.getTotalSpaceFromDisks() - this.getUsedSpace() - additionalSpace;
    // max qty to put
    int maximumPut = itemCapacity(spaceLeft);

    if (qty <= maximumPut) {
      // can put all
      map.put(id, stock + qty);
      qty = 0;
    } else {
      // cannot put all
      map.put(id, stock + maximumPut);
      qty -= maximumPut;
    }

    return qty;
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
