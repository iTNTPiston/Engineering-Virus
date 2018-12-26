package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.GroupSearchResult;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.db.MainframeTPQuery;
import com.tntp.mnm.api.db.QueryExecuter;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.init.MNMItems;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileQueryBuilder extends STileNeithernetInventory implements ITileSecuredCont {

  private String currentGroupName;
  private int currentFirstGroupIndex;
  private int currentScrollIndex;

  private ItemStack[] cachedGroupChips;
  private ItemStack[] cachedItemGroup;
  private ItemStack cachedCurrentGroupIcon;

  private Security security;

  private int scanTotal;
  private int scanCD;
  private boolean needsUpdate;

  public TileQueryBuilder() {
    super(33);// 0-8 inventory, 9,10,11 chip,12-26 display,27-32 group
    security = new Security(this);
    scanTotal = 200;
  }

  @Override
  public void updateEntity() {
    super.updateEntity();
    if (worldObj != null && !worldObj.isRemote) {
      if (needsUpdate) {
        if (scanCD <= 0) {
          scanCD = scanTotal;
          updateCache();
          markDirty();
        } else {
          scanCD--;
        }
      }
    }
  }

  public void updateCache() {
    selectGroup(currentGroupName);
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

  public IMessage receiveClientGuiMessage(int buttonID) {
    // server only
    if (buttonID == 0) {
      executePut();
    } else if (buttonID == 2) {
      if (currentGroupName != null && currentGroupName.length() > 0) {
        int dotPos = currentGroupName.lastIndexOf('.');
        if (dotPos == -1)
          selectGroup("");
        else
          selectGroup(currentGroupName.substring(0, dotPos));
      }
    } else if (buttonID >= 3 && buttonID <= 7) {
      int groupIndex = currentFirstGroupIndex + buttonID - 3;
      if (groupIndex < cachedGroupChips.length) {
        String groupName = MNMItems.data_group_chip.getGroupName(cachedGroupChips[groupIndex]);
        selectGroup(groupName);
      }
    } else if (buttonID == 8) {
      scrollGroupTo(currentFirstGroupIndex - 1);
    } else if (buttonID == 9) {
      scrollGroupTo(currentFirstGroupIndex + 1);
    }
    return null;
  }

  public void scroll(int direction) {

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

  public void selectGroup(String str) {
    if (str == null)
      str = "";
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      GroupSearchResult result = mf.getGroup(str);
      cachedGroupChips = new ItemStack[result.getGroupChipList().size()];
      result.getGroupChipList().toArray(cachedGroupChips);
      cachedItemGroup = result.getGroupItems();
      cachedCurrentGroupIcon = result.getCurrentGroup();
      currentGroupName = str;
    } else {
      cachedGroupChips = new ItemStack[5];
      currentFirstGroupIndex = 0;
      cachedItemGroup = null;
      cachedCurrentGroupIcon = null;
      currentGroupName = "";
    }
    scrollGroupTo(0);
    ItemStack currentGroup = MNMItems.data_group_chip.getGroupIconWithNameTag(cachedCurrentGroupIcon);
    this.setInventorySlotContents(27, currentGroup);

  }

  public void executePut() {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      System.out.println("Connected to MF");
      MainframeTPQuery query = new MainframeTPQuery();
      query.addTP(-1, 0);
      QueryExecuter qe = new QueryExecuter(query, this, 0, 8);
      mf.sendQueryToCPU(qe);
    }
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

}
