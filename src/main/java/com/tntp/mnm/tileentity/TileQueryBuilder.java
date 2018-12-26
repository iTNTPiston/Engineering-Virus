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
import com.tntp.mnm.network.SMessage;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileQueryBuilder extends STileNeithernetInventory implements ITileSecuredCont {

  private String currentGroupName;
  private int currentFirstGroupIndex;
  private int currentScrollIndex;

  private ItemStack[] cachedGroupIcons;
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
    if (buttonID == 0)
      executePut();
    return null;
  }

  public void scroll(int direction) {

  }

  public void selectGroup(String str) {
    if (str == null)
      str = "";
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      GroupSearchResult result = mf.getGroup(str);
      cachedGroupIcons = new ItemStack[result.getGroupChipList().size()];
      result.getGroupChipList().toArray(cachedGroupIcons);
      cachedItemGroup = result.getGroupItems();
      cachedCurrentGroupIcon = result.getCurrentGroup();
      currentGroupName = str;
    } else {
      cachedGroupIcons = new ItemStack[5];
      currentFirstGroupIndex = 0;
      cachedItemGroup = null;
      cachedCurrentGroupIcon = null;
      currentGroupName = "";
    }
    if (currentFirstGroupIndex + 5 > cachedGroupIcons.length) {
      currentFirstGroupIndex = cachedGroupIcons.length - 5;
    }
    if (currentFirstGroupIndex < 0)
      currentFirstGroupIndex = 0;
    for (int i = 0; i < 5; i++) {
      int j = currentFirstGroupIndex + i;
      ItemStack iconStack;
      if (j < cachedGroupIcons.length)
        iconStack = MNMItems.data_group_chip.getGroupIcon(cachedGroupIcons[j]);
      else
        iconStack = null;
      this.setInventorySlotContents(28 + i, iconStack);
    }
    ItemStack currentGroup = MNMItems.data_group_chip.getGroupIcon(cachedCurrentGroupIcon);
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
    for (int i = 0; i < 6; i++) {
      slots.add(new SlotDecorative(this, i + 27, 43 + i * 18, 20));
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
