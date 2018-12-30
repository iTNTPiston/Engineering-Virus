package com.tntp.mnm.tileentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.tntp.mnm.api.db.GroupSearchResult;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.db.MainframeTPQuery;
import com.tntp.mnm.api.db.QueryExecuter;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.network.MCGuiQueryBuilderRow;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileQueryBuilder extends STileNeithernetInventory implements ITileSecuredCont {

  private String currentGroupName;
  private int currentFirstGroupIndex;
  private int currentScrollIndex;

  private ItemStack[] cachedGroupChips;
  private ItemStack[] cachedItemsInGroup;
  private int[] cachedItemsInGroupDef;
  private ItemStack cachedCurrentGroupIcon;

  private Security security;

  private int scanTotal;
  private int scanCD;
  private boolean needsUpdate;

  private int cacheRowTotal;
  private HashMap<Integer, Integer> takeMap;

  public TileQueryBuilder() {
    super(33);// 0-8 inventory, 9,10,11 chip,12-26 display,27-32 group
    security = new Security(this);
    scanTotal = 200;
    takeMap = new HashMap<Integer, Integer>();
  }

  @Override
  public void onBreakingContainer() {
    for (int i = 12; i < this.getSizeInventory(); i++) {
      this.setInventorySlotContents(i, null);
    }
  }

  @Override
  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (needsUpdate) {
        if (scanCD <= 0) {
          scanCD = scanTotal;
          updateCache();
          updateSelectedItems();
          markDirty();
        } else {
          scanCD--;
        }
      }
    }
  }

  public void updateCache() {
    selectGroup(currentGroupName, true, true);

  }

  public void updateSelectedItems() {
    for (int i = 12; i < 27; i++) {
      int defIndex = currentScrollIndex * 5 + i - 12;
      if (defIndex < cachedItemsInGroupDef.length && getStackInSlot(i) != null) {
        int def = cachedItemsInGroupDef[defIndex];
        ItemStack s = getStackInSlot(i);
        NBTTagCompound tag = s.hasTagCompound() ? s.getTagCompound() : new NBTTagCompound();
        // set tag
        Integer v = takeMap.get(def);
        int takeQty = v == null ? 0 : v;
        tag.setInteger("MNM|QueryBuilderSelected", takeQty);
        s.setTagCompound(tag);
      }
    }
  }

  public void selectSlot(int i, int qty) {
    // i is 0-14 inclusive
    // qty is either -1,1 or 2 (1stack) 3(half) 4(clear)
    int defIndex = currentScrollIndex * 5 + i;
    System.out.println(defIndex);
    if (defIndex < cachedItemsInGroupDef.length && getStackInSlot(i + 12) != null) {
      int maxStackSize = getStackInSlot(i + 12).getMaxStackSize();
      System.out.println(maxStackSize);
      int def = cachedItemsInGroupDef[defIndex];
      Integer v = takeMap.get(def);
      int takeQty = v == null ? 0 : v;
      switch (qty) {
      case -1:
        takeQty--;
        break;
      case 1:
        takeQty++;
        break;
      case 2:
        takeQty += maxStackSize;
        break;
      case 3:
        takeQty += maxStackSize / 2;
        break;
      case 4:
        takeQty = 0;
      }
      if (takeQty == 0)
        takeMap.remove(def);
      else
        takeMap.put(def, takeQty);
    }
    scanCD = 0;
  }

  @Override
  public void openInventory() {
    needsUpdate = true;
    scanCD = 0;
  }

  @Override
  public void closeInventory() {
    needsUpdate = false;
  }

  public IMessage receiveClientGuiMessage(int windowID, int buttonID) {
    // server only
    if (buttonID == 0) {
      executePut();
    } else if (buttonID == 1) {
      executeTake();
    } else if (buttonID == 2) {
      if (currentGroupName != null && currentGroupName.length() > 0) {
        int dotPos = currentGroupName.lastIndexOf('.');
        if (dotPos == -1)
          selectGroup("", false, false);
        else
          selectGroup(currentGroupName.substring(0, dotPos), false, false);
      }
    } else if (buttonID >= 3 && buttonID <= 7) {
      int groupIndex = currentFirstGroupIndex + buttonID - 3;
      if (groupIndex < cachedGroupChips.length) {
        String groupName = MNMItems.data_group_chip.getGroupName(cachedGroupChips[groupIndex]);
        selectGroup(groupName, false, false);
      }
    } else if (buttonID == 8) {
      scrollGroupTo(currentFirstGroupIndex - 1);
    } else if (buttonID == 9) {
      scrollGroupTo(currentFirstGroupIndex + 1);
    } else if (buttonID == 10) {
      scrollItemsTo(currentScrollIndex - 1);
    } else if (buttonID == 11) {
      scrollItemsTo(currentScrollIndex + 1);
    } else {
      // select slots
      int qtyCode = (buttonID - 12) / 15;
      if (qtyCode == 0)
        qtyCode = -1;
      int slot = (buttonID - 12) % 15;
      selectSlot(slot, qtyCode);
    }
    return new MCGuiQueryBuilderRow(windowID, currentScrollIndex, rowTotal());
  }

  public void scrollItemsTo(int row) {
    int rowTotal = rowTotal();
    if (row + 3 > rowTotal) {
      row = rowTotal - 3;
    }
    if (row < 0) {
      row = 0;
    }
    currentScrollIndex = row;
    if (cachedItemsInGroup != null) {
      int startIndex = currentScrollIndex * 5;
      int end = Math.min(startIndex + 15, cachedItemsInGroup.length);
      for (int i = 12, j = startIndex; j < startIndex + 15; i++, j++) {
        if (j < end)
          this.setInventorySlotContents(i, cachedItemsInGroup[j]);
        else
          this.setInventorySlotContents(i, null);
      }
    } else {
      for (int j = 12; j < 27; j++) {
        this.setInventorySlotContents(j, null);
      }
    }
    updateSelectedItems();
  }

  private int rowTotal() {
    int l = cachedItemsInGroup == null ? 0 : cachedItemsInGroup.length;
    return (int) Math.max(3, Math.ceil(l / 5.0));
  }

  public void scrollGroupTo(int firstGroupIndex) {
    if (firstGroupIndex + 5 > cachedGroupChips.length) {
      firstGroupIndex = cachedGroupChips.length - 5;
    }
    if (firstGroupIndex < 0)
      firstGroupIndex = 0;
    currentFirstGroupIndex = firstGroupIndex;
    for (int i = 0; i < 5; i++) {
      int j = currentFirstGroupIndex + i;
      ItemStack iconStack;
      if (j < cachedGroupChips.length) {
        iconStack = MNMItems.data_group_chip.getGroupIconWithNameTag(cachedGroupChips[j]);
      } else {
        iconStack = null;
      }
      this.setInventorySlotContents(28 + i, iconStack);
    }

  }

  public void selectGroup(String str, boolean keepGroupIndex, boolean keepItemIndex) {
    if (str == null)
      str = "";
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      GroupSearchResult result = mf.getGroup(str);
      cachedGroupChips = new ItemStack[result.getGroupChipList().size()];
      result.getGroupChipList().toArray(cachedGroupChips);
      cachedItemsInGroup = result.getGroupItems();
      cachedItemsInGroupDef = result.getGroupItemsDef();
      cachedCurrentGroupIcon = result.getCurrentGroup();
      currentGroupName = str;
    } else {
      cachedGroupChips = new ItemStack[5];
      currentFirstGroupIndex = 0;
      cachedItemsInGroup = new ItemStack[15];
      cachedItemsInGroupDef = new int[15];
      cachedCurrentGroupIcon = null;
      currentGroupName = "";
    }
    scrollGroupTo(keepGroupIndex ? currentFirstGroupIndex : 0);
    ItemStack currentGroup = MNMItems.data_group_chip.getGroupIconWithNameTag(cachedCurrentGroupIcon);
    this.setInventorySlotContents(27, currentGroup);

    scrollItemsTo(keepItemIndex ? currentScrollIndex : 0);

  }

  public void executePut() {
    takeMap.clear();
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      System.out.println("Connected to MF");
      MainframeTPQuery query = new MainframeTPQuery();
      query.addTP(-1, 0);
      QueryExecuter qe = new QueryExecuter(query, this, 0, 8);
      mf.sendQueryToCPU(qe);
    }
    scanCD = 5;
  }

  public void executeTake() {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      System.out.println("Connected to MF");
      MainframeTPQuery query = new MainframeTPQuery();
      for (Entry<Integer, Integer> e : takeMap.entrySet()) {
        query.addTP(e.getKey(), e.getValue());
      }
      takeMap.clear();
      QueryExecuter qe = new QueryExecuter(query, this, 0, 8);
      mf.sendQueryToCPU(qe);
    }
    scanCD = 5;
  }

  @Override
  public String getContainerGui() {
    return "GuiContQueryBuilder";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        slots.add(new Slot(this, j + i * 3, 12 + j * 18, 41 + i * 18));
      }
    }
    for (int j = 0; j < 3; j++) {
      slots.add(new Slot(this, 9 + j, 12 + j * 18, 96));
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 5; j++) {
        slots.add(new SlotDecorative(this, 12 + i * 5 + j, 71 + j * 18, 38 + i * 18));
      }
    }
    slots.add(new SlotDecorative(this, 27, 27, 20));
    for (int i = 0; i < 5; i++) {
      slots.add(new SlotDecorative(this, i + 28, 61 + i * 18, 20));
    }

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

  public String getCurrentGroupName() {
    return currentGroupName;
  }

  public int getCurrentScrollIndex() {
    return currentScrollIndex;
  }

  public void setCurrentScrollIndex(int currentScrollIndex) {
    this.currentScrollIndex = currentScrollIndex;
  }

  public int getCacheRowTotal() {
    return cacheRowTotal;
  }

  public void setCacheRowTotal(int cacheRowTotal) {
    this.cacheRowTotal = cacheRowTotal;
  }

}
