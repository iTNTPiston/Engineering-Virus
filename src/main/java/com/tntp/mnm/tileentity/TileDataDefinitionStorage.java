package com.tntp.mnm.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.tntp.mnm.api.db.ItemDef;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;
import com.tntp.mnm.item.disk.ItemDisk;
import com.tntp.mnm.util.ItemUtil;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TileDataDefinitionStorage extends STileData implements ITileSecuredCont, ITileDiskKeyable {

  private List<ItemDef> definedItems;
  private Security security;

  public TileDataDefinitionStorage() {
    super(5);
    security = new Security(this);
    definedItems = new ArrayList<ItemDef>();
  }

  @Override
  public int getUsedSpace() {
    return definedItems.size() * 4;
  }

  public List<ItemDef> getDefinedItems() {
    return definedItems;
  }

  public void dumpDefinitions(Queue<ItemDef> queue) {
    if (isTransferringData)
      return;
    while (getUsedSpace() + 4 <= getTotalSpaceFromDisks() && !queue.isEmpty()) {
      definedItems.add(queue.poll());
    }
    markDirty();
  }

  /**
   * This is a safety action. This should never has any effect
   */
  public void removeNullDefinitions() {
    if (isTransferringData)
      return;
    for (Iterator<ItemDef> iter = definedItems.iterator(); iter.hasNext();) {
      ItemDef def = iter.next();
      if (def.stack == null || def.stack.stackSize <= 0 || def.stack.getItem() == null)
        iter.remove();
    }
    markDirty();
  }

  public void removeDefinitionsNotIn(Set<Integer> definitionsToKeep) {
    if (isTransferringData)
      return;
    for (Iterator<ItemDef> iter = definedItems.iterator(); iter.hasNext();) {
      ItemDef def = iter.next();
      if (!definitionsToKeep.contains(def.id))
        iter.remove();
    }
    markDirty();
  }

  /**
   * 
   * @param stack
   * @return the new ID if not defined,-1 if disk full, or the defined id
   *         otherwise
   */
  public int getItemDefID(ItemStack stack) {
    if (isTransferringData)
      return -1;
    for (int i = 0; i < definedItems.size(); i++) {
      if (ItemUtil.areItemAndTagEqual(definedItems.get(i).stack, stack)) {
        return definedItems.get(i).id;
      }
    }
    return -1;
  }

  /**
   * Return a new copy of the defined item
   * 
   * @param id
   * @return
   */
  public ItemStack getItemDef(int id) {
    if (isTransferringData)
      return null;
    for (int i = 0; i < definedItems.size(); i++) {
      if (definedItems.get(i).id == id) {
        ItemStack s = definedItems.get(i).stack.copy();
        s.stackSize = 1;
        return s;
      }
    }
    return null;
  }

  public boolean defineItem(ItemStack stack, int id) {
    if (isTransferringData)
      return false;
    if (getUsedSpace() + 4 <= getTotalSpaceFromDisks()) {
      ItemDef item = new ItemDef();
      ItemStack s = stack.copy();
      s.stackSize = 1;
      item.id = id;
      item.stack = s;
      definedItems.add(item);
      markDirty();
      return true;// defined
    }
    return false;
  }

  public void undefineItem(int id) {
    if (isTransferringData)
      return;
    for (Iterator<ItemDef> iter = definedItems.iterator(); iter.hasNext();) {
      ItemDef def = iter.next();
      if (def.id == id)
        iter.remove();
    }
    markDirty();
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    security.writeToNBT(tag);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    security = new Security(this);
    security.readFromNBT(tag);
  }

  @Override
  public String getContainerGui() {
    return "GuiContDataDefinitionStorage";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    for (int i = 0; i < 5; i++) {
      slots.add(new SlotDecorative(this, i, 9 + i * 18, 21));
    }
  }

  @Override
  public Security getSecurity() {
    return security;
  }

  @Override
  public void writeDataToNBT(NBTTagCompound tag) {
    NBTTagList list = new NBTTagList();
    for (int i = 0; i < definedItems.size(); i++) {
      NBTTagCompound com = new NBTTagCompound();
      definedItems.get(i).toNBT(com);
      list.appendTag(com);
    }
    tag.setTag("defined_items", list);
  }

  @Override
  public void readDataFromNBT(NBTTagCompound tag) {
    NBTTagList list = tag.getTagList("defined_items", NBT.TAG_COMPOUND);
    definedItems = new ArrayList<ItemDef>();
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound com = list.getCompoundTagAt(i);
      ItemDef def = new ItemDef();
      def.fromNBT(com);
      definedItems.add(def);
    }
  }

  @Override
  public void clearData() {
    definedItems.clear();
    markDirty();
  }

  @Override
  public String diskKeyType() {
    return "definition";
  }

  @Override
  public boolean onPreTransferToDiskKey(ItemStack validDiskKey) {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      return mf.verifyDefinitionCanBeTakenOut(definedItems);
    }
    return false;
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
