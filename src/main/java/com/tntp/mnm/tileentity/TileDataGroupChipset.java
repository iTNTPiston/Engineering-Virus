package com.tntp.mnm.tileentity;

import java.util.List;

import com.tntp.mnm.gui.cont.ITileDataCont;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.item.ItemDataGroupChip;

import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileDataGroupChipset extends STilePOB implements ITileDataCont {

  public TileDataGroupChipset() {
    super(3);
  }

  /**
   * Get all groups in all next levels
   * 
   * @param groups
   * @param prefix
   * @return an itemstack containing exactly the prefix group, if none returns
   *         null
   */
  public ItemStack addGroups(List<ItemStack> groups, String prefix) {
    ItemDataGroupChip chip = MNMItems.data_group_chip;
    ItemStack returnStack = null;
    for (int i = 0; i < getSizeInventory(); i++) {
      ItemStack stack = getStackInSlot(i);
      if (stack != null && stack.getItem() == chip) {
        String group = chip.getGroupName(stack);
        if (group.startsWith(prefix)) {// must be prefixed
          if (group.length() == prefix.length()) {
            groups.add(stack);
            returnStack = stack;
          } else if (group.length() > prefix.length()) {
            if (prefix.length() == 0 || group.charAt(prefix.length()) == '.') {
              groups.add(stack);
            }
          }
        }
      }
    }
    return returnStack;
  }

  /**
   * Get all groups that begins with the prefix, not necessary to be in the next
   * level
   * 
   * @param groups
   * @param prefix
   * @return an itemstack containing exactly the prefix group, if none returns
   *         null
   */
  public ItemStack findGroupBeginWith(List<ItemStack> groups, String prefix) {
    ItemDataGroupChip chip = MNMItems.data_group_chip;
    ItemStack returnStack = null;
    for (int i = 0; i < getSizeInventory(); i++) {
      ItemStack stack = getStackInSlot(i);
      if (stack != null && stack.getItem() == chip) {
        String group = chip.getGroupName(stack).toLowerCase();
        if (group.startsWith(prefix)) {// must be prefixed
          groups.add(stack);
          if (group.length() == prefix.length()) {
            returnStack = stack;
          }
        }
      }
    }
    return returnStack;
  }

  @Override
  public String getContainerGui() {
    return "GuiContDataGroupChipset";
  }

  @Override
  public void addContainerSlots(List<Slot> slots) {
    for (int i = 0; i < 3; i++) {
      slots.add(new Slot(this, i, 39 + i * 41, 52));
    }

  }

  @Override
  public boolean canReadData() {
    return true;
  }
}
