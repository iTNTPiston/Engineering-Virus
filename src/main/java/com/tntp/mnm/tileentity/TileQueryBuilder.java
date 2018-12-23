package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.GroupSearchResult;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.db.MainframeTPQuery;
import com.tntp.mnm.api.db.QueryExecuter;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.cont.ITileSecuredCont;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileQueryBuilder extends STileNeithernetInventory implements ITileSecuredCont {

  private String currentGroupName;
  private int currentFirstGroupIndex;
  private int currentScrollIndex;

  private ItemStack[] cachedGroupList;
  private ItemStack[] cachedItemGroup;

  private Security security;

  public TileQueryBuilder() {
    super(12);// 0-8 inventory, 9,10,11 chip,12-26 display
    security = new Security(this);
  }

  public void scroll(int direction) {

  }

  public void selectGroup(String str) {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      GroupSearchResult result = mf.getGroup(str);
      cachedGroupList = new ItemStack[result.getGroupChipList().size()];
      result.getGroupChipList().toArray(cachedGroupList);
      cachedItemGroup = new ItemStack[result.getGroupItems().size()];
      result.getGroupItems().toArray(cachedItemGroup);
      currentGroupName = str;
    } else {
      cachedGroupList = null;
      cachedItemGroup = null;
      currentGroupName = "";
    }
  }

  public void executePut() {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
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

}
