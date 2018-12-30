package com.tntp.mnm.tileentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class STileDataStorage extends STileData implements ITileDiskKeyable {
  private static final int ITEM_PER_BYTE = 128;
  private HashMap<Integer, Integer> map;

  public STileDataStorage(int size) {
    super(size);
    map = new HashMap<Integer, Integer>();
  }

  @Override
  public int getUsedSpace() {
    int size = 0;
    for (int value : map.values()) {
      size += spaceNeeded(value);
    }
    return size;
  }

  public HashMap<Integer, Integer> getData(Mainframe mf) {
    if (!checkAndRememberMainframe(mf))
      return new HashMap<Integer, Integer>();
    return map;
  }

  /**
   * 
   * @return quantity left to be taken away
   */
  public int takeAway(Mainframe mf, int id, int qty) {
    if (isTransferringData)
      return qty;
    if (!checkAndRememberMainframe(mf))
      return qty;
    // how much is stored
    int stock = map.containsKey(id) ? map.get(id) : 0;
    // stored is less than or equal need
    if (stock <= qty) {
      qty -= stock;
      map.remove(id);// take all
    } else {
      // stored has more than need
      stock -= qty;
      // need becomes 0
      qty = 0;
      map.put(id, stock);
    }
    markDirty();
    // return qty not taken
    return qty;
  }

  /**
   * 
   * @param id
   * @param qty
   * @return qty that cannot be put in
   */
  public int putIn(Mainframe mf, int id, int qty) {
    if (isTransferringData)
      return qty;
    if (!checkAndRememberMainframe(mf))
      return qty;
    if (qty == 0)
      return 0;

    int stock = map.containsKey(id) ? map.get(id) : 0;// stored

    // space left in the disks, counting definition
    int spaceLeft = this.getTotalSpaceFromDisks() - this.getUsedSpace();
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
    markDirty();
    return qty;
  }

  public int findQuantityFor(Mainframe mf, int id) {
    if (isTransferringData)
      return 0;
    if (!checkAndRememberMainframe(mf))
      return 0;
    Integer qty = map.get(id);
    if (qty == null)
      return 0;
    return qty;
  }

  public int spaceNeeded(int stackSize) {
    return (int) Math.ceil(stackSize / (double) ITEM_PER_BYTE);
  }

  public int itemCapacity(int dataspace) {
    if (dataspace < 0)
      return 0;
    return dataspace * ITEM_PER_BYTE;
  }

  @Override
  public void writeDataToNBT(NBTTagCompound tag) {
    NBTTagList hash = new NBTTagList();
    for (Entry<Integer, Integer> e : map.entrySet()) {
      NBTTagCompound t = new NBTTagCompound();
      t.setInteger("key", e.getKey());
      t.setInteger("value", e.getValue());
      hash.appendTag(t);
    }
    tag.setTag("HashMap", hash);
  }

  @Override
  public void readDataFromNBT(NBTTagCompound tag) {
    map = new HashMap<Integer, Integer>();
    NBTTagList hash = tag.getTagList("HashMap", NBT.TAG_COMPOUND);
    for (int i = 0; i < hash.tagCount(); i++) {
      NBTTagCompound t = hash.getCompoundTagAt(i);
      map.put(t.getInteger("key"), t.getInteger("value"));
    }
  }

  @Override
  public void clearData() {
    map.clear();
    markDirty();
  }

  @Override
  public String diskKeyType() {
    return "items";
  }

  @Override
  public boolean onPreTransferToDiskKey(ItemStack validDiskKey) {
    return true;
  }

  @Override
  public void onPostTransferToDiskKey(ItemStack validDiskKey) {
  }

  @Override
  public boolean onPreTransferFromDiskKey(ItemStack validDiskKey) {
    return true;
  }

  @Override
  public void onPostTransferFromDiskKey(ItemStack validDiskKey) {
  }

}
