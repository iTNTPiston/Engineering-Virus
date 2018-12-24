package com.tntp.mnm.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.tntp.mnm.api.db.ItemDef;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.item.disk.ItemDisk;
import com.tntp.mnm.util.ItemUtil;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TileDataDefinitionStorage extends STileData implements ITileSecuredCont {
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

  /**
   * 
   * @param stack
   * @return the new ID if not defined,-1 if disk full, or the defined id
   *         otherwise
   */
  public int getItemDefID(ItemStack stack) {
    for (int i = 0; i < definedItems.size(); i++) {
      if (ItemUtil.areItemAndTagEqual(definedItems.get(i).stack, stack)) {
        return definedItems.get(i).id;
      }
    }
    return -1;
  }

  public ItemStack getItemDef(int id) {
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
    if (getUsedSpace() + 4 <= getTotalSpaceFromDisks()) {
      ItemDef item = new ItemDef();
      ItemStack s = stack.copy();
      s.stackSize = 1;
      item.id = id;
      item.stack = stack;
      definedItems.add(item);
      return true;// defined
    }
    return false;
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    NBTTagList list = new NBTTagList();
    for (int i = 0; i < definedItems.size(); i++) {
      NBTTagCompound com = new NBTTagCompound();
      definedItems.get(i).toNBT(com);
      list.appendTag(com);
    }
    tag.setTag("defined_items", list);
    security.writeToNBT(tag);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    NBTTagList list = tag.getTagList("defined_items", NBT.TAG_COMPOUND);
    definedItems = new ArrayList<ItemDef>();
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound com = list.getCompoundTagAt(i);
      ItemDef def = new ItemDef();
      def.fromNBT(com);
      definedItems.add(def);
    }
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

}
