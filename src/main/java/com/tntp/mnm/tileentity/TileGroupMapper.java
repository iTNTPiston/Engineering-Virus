package com.tntp.mnm.tileentity;

import java.util.Iterator;
import java.util.LinkedList;
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
  private ItemStack groupChip;
  private LinkedList<ItemStack> candidateChips;

  public TileGroupMapper() {
    super(9);
    security = new Security(this);
    candidateChips = new LinkedList<ItemStack>();
  }

  @Override
  public String getContainerGui() {
    return "GuiContGroupMapper";
  }

  public int receiveClientGuiSearchItem() {
    int def = searchItem();
    return def;
  }

  public void receiveClientGuiSearchGroup(String searchGroup) {
    searchGroup(searchGroup);
  }

  public void openInventory() {
    for (int i = 1; i < 9; i++)
      this.setInventorySlotContents(i, null);
    groupChip = null;
    cachedItemDef = -1;
    candidateChips = new LinkedList<ItemStack>();
  }

  public void closeInventory() {
    candidateChips = null;
  }

  public IMessage receiveClientGuiMessageButton(int buttonID) {
    if (buttonID >= 5) {
      switchGroup(buttonID - 5);
      return null;
    }
    // server only
    // 0 - search
    // 1 - add
    // 2 - remove
    // 3 -remove all
    if (groupChip == null)
      return null;
    String groupName = MNMItems.data_group_chip.getGroupName(groupChip);
    if (buttonID != 3 && (groupName == null || groupName.length() == 0))
      return null;// remove all does not require group
    if (getStackInSlot(1) == null)
      return null;
    ItemStack stack = getStackInSlot(1);

    Mainframe mf = connectToMainframe();
    if (mf != null) {
      switch (buttonID) {
      case 1:
        mf.addToGroup(groupName, stack);
        break;
      case 2:
        mf.removeFromGroup(groupName, stack);
        break;
      case 3:
        mf.removeFromAllGroups(stack);
        break;
      }

    }
    return null;
  }

  public void switchGroup(int id) {
    if (id == 0) {
      // kick out current group
      if (!candidateChips.isEmpty()) {
        // if has more candidates
        groupChip = candidateChips.removeFirst();
        ItemStack iconStack = MNMItems.data_group_chip.getGroupIconWithNameTag(groupChip);
        this.setInventorySlotContents(2, iconStack);
      }
    } else {
      if (id <= candidateChips.size()) {
        // id=1 corresponds to the first in candidateChips, etc
        groupChip = candidateChips.remove(id - 1);
        ItemStack iconStack = MNMItems.data_group_chip.getGroupIconWithNameTag(groupChip);
        this.setInventorySlotContents(2, iconStack);
      }
    }
    setCandidateSlots();
  }

  public void setCandidateSlots() {
    int i = 3;
    for (Iterator<ItemStack> iter = candidateChips.iterator(); iter.hasNext() && i < 9;) {
      ItemStack stack = iter.next();
      ItemStack iconStack = MNMItems.data_group_chip.getGroupIconWithNameTag(stack);
      this.setInventorySlotContents(i, iconStack);
      i++;
    }
    for (; i < 9; i++) {
      this.setInventorySlotContents(i, null);
    }
  }

  public void searchGroup(String group) {
    if (group == null)
      group = "";
    Mainframe mf = connectToMainframe();
    candidateChips.clear();
    if (mf != null) {
      mf.searchGroupChip(group, candidateChips);
    }
    if (candidateChips.isEmpty()) {
      groupChip = null;
    } else {
      groupChip = candidateChips.removeFirst();
    }
    ItemStack iconStack = MNMItems.data_group_chip.getGroupIconWithNameTag(groupChip);
    this.setInventorySlotContents(2, iconStack);
    setCandidateSlots();
  }

  /**
   * Search for the stack put in slot 0
   * 
   * @param toSearch
   * @return the definition of the item
   */
  public int searchItem() {
    if (getStackInSlot(0) == null) {
      this.setInventorySlotContents(1, null);
      return -1;
    }
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
    slots.add(new Slot(this, 0, 13, 64));
    slots.add(new SlotDecorative(this, 1, 49, 63));
    slots.add(new SlotDecorative(this, 2, 68, 41));
    for (int i = 0; i < 2; i++)
      for (int j = 0; j < 3; j++)
        slots.add(new SlotDecorative(this, 3 + i * 3 + j, 11 + j * 18, 22 + i * 18));

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
