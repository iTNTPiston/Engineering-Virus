package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.GroupSearchResult;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.init.MNMItems;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileGroupMapper extends STileNeithernetInventory implements ITileSecuredCont {

  private Security security;
  private int cachedItemDef;

  public TileGroupMapper() {
    super(3);
    security = new Security(this);
  }

  @Override
  public String getContainerGui() {
    return "GuiContGroupMapper";
  }

  public int receiveClientGuiSearch(String searchGroup) {
    searchGroup(searchGroup);
    int def = searchItem();
    return def;
  }

  public IMessage receiveClientGuiMessage(int buttonID) {
    // server only
    // 0 - search
    // 1 - add
    // 2 - remove
    // 3 -remove all
    if (buttonID == 0) {

    }
    return null;
  }

  public void searchGroup(String group) {
    if (group == null)
      group = "";
    Mainframe mf = connectToMainframe();
    ItemStack foundGroup;
    if (mf != null) {
      foundGroup = mf.searchGroupChip(group);
    } else {
      foundGroup = null;
    }
    ItemStack iconStack = MNMItems.data_group_chip.getGroupIconWithNameTag(foundGroup);
    this.setInventorySlotContents(2, iconStack);
  }

  /**
   * Search for the stack put in slot 0
   * 
   * @param toSearch
   * @return the definition of the item
   */
  public int searchItem() {
    ItemStack toSearch = getStackInSlot(0).copy();
    toSearch.stackSize = 1;
    Mainframe mf = connectToMainframe();
    int def = -1;
    if (mf != null) {
      def = mf.defineItem(toSearch);
    }
    if (def < 0)
      toSearch = null;
    this.setInventorySlotContents(1, toSearch);
    cachedItemDef = def;
    return def;
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    slots.add(new Slot(this, 0, 13, 53));
    slots.add(new SlotDecorative(this, 1, 49, 53));
    slots.add(new SlotDecorative(this, 2, 49, 20));

  }

  @Override
  public Security getSecurity() {
    return security;
  }

  @Override
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    security.writeToNBT(tag);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    security = new Security(this);
    security.readFromNBT(tag);
  }

  public void setItemDefCache(int def) {
    cachedItemDef = def;
  }

  public int getItemDefCache() {
    return cachedItemDef;
  }

}
