package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.SlotDecorative;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.util.ItemUtil;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileDataDefinitionTerminal extends STileNeithernetInventory implements ITileSecuredCont {
  private ItemStack[] cachedDefinition;
  private int clientDefinitionLengthCache;
  private int rowTotal;
  private int rowCurrent;
  private boolean needsUpdate;
  private int scanTotal;
  private int scanCD;
  private Security security;

  public TileDataDefinitionTerminal() {
    super(32);
    scanTotal = 200;
    scanCD = 200;
    security = new Security(this);
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

  public void scrollTo(int row) {
    if (cachedDefinition == null)
      return;
    if (row + 4 > rowTotal) {
      row = rowTotal - 4;
    }
    if (row < 0)
      row = 0;
    rowCurrent = row;
    int startIndex = rowCurrent * 8;
    int end = Math.min(startIndex + 32, cachedDefinition.length);
    for (int i = 0, j = startIndex; j < startIndex + 32; i++, j++) {
      if (j < end)
        this.setInventorySlotContents(i, cachedDefinition[j]);
      else
        this.setInventorySlotContents(i, null);
    }

  }

  public void updateCache() {
    // server only
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      cachedDefinition = mf.getDefinitions();
    } else {
      cachedDefinition = null;
    }
    int l = cachedDefinition == null ? 0 : cachedDefinition.length;
    rowTotal = (int) Math.max(4, Math.ceil(l / 8.0));
    scrollTo(rowCurrent);
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

  @Override
  public String getContainerGui() {
    return "GuiContDataDefinitionTerminal";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 8; j++) {
        slots.add(new SlotDecorative(this, i * 8 + j, 17 + j * 18, 20 + i * 18));
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

  public void setDefinitionLengthCache(int c) {
    clientDefinitionLengthCache = c;
    rowTotal = (int) Math.max(4, Math.ceil(c / 8.0));
  }

  public int getDefinitionLengthCache() {
    return clientDefinitionLengthCache;
  }

  public int getCurrentRow() {
    return rowCurrent;
  }

  public void setCurrentRowForClient(int r) {
    rowCurrent = r;
  }

  public int getTotalRow() {
    return rowTotal;
  }

  public int getDefinitionLength() {
    if (cachedDefinition == null)
      return 0;
    return cachedDefinition.length;
  }

  public int getDefinedIDForClient(ItemStack stack) {
    if (stack == null)
      return -1;
    for (int i = 0; i < getSizeInventory(); i++) {
      if (ItemUtil.areItemAndTagEqual(getStackInSlot(i), stack))
        return i + rowCurrent * 8;
    }
    return -1;
  }

}
