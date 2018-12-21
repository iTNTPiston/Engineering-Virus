package com.tntp.mnm.item;

import java.util.List;

import com.tntp.mnm.init.MNMItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemDataGroupChip extends SItem {
  public ItemDataGroupChip() {
  }

  /**
   * allows items to add custom lines of information to the mouseover description
   */
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
    String groupName = getGroupName(stack);
    if (groupName.length() != 0) {
      list.add("<LOCAL>Group: " + groupName);
    }
  }

  /**
   * Render Pass sensitive version of hasEffect()
   */
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return pass == 0 && getGroupName(par1ItemStack).length() > 0;
  }

  /**
   * 
   * @param stack
   * @return group name, or empty string if there is no group name
   */
  public String getGroupName(ItemStack stack) {
    if (!stack.hasTagCompound())
      return "";
    return getGroupNameFromNBT(stack.getTagCompound());
  }

  public ItemStack getGroupIcon(ItemStack stack) {
    if (!stack.hasTagCompound())
      return null;
    return getGroupIconFromNBT(stack.getTagCompound());
  }

  private static String getGroupNameFromNBT(NBTTagCompound stackTag) {
    NBTTagCompound groupTag = stackTag.getCompoundTag("MNM|DataGroup");
    String groupName = groupTag.getString("group_name");
    return groupName;
  }

  private static ItemStack getGroupIconFromNBT(NBTTagCompound stackTag) {
    NBTTagCompound groupTag = stackTag.getCompoundTag("MNM|DataGroup");
    ItemStack stack = ItemStack.loadItemStackFromNBT(groupTag);
    return stack;
  }

  /**
   * Write the group info to the chip. The icon stack will remain unchanged
   * 
   * @param stack
   * @param group
   * @param icon
   */
  public static void writeGroupToItem(ItemStack stack, String group, ItemStack icon) {
    if (icon == null)
      return;
    if (stack == null || stack.getItem() != MNMItems.data_group_chip)
      return;
    if (group == null || group.length() == 0)
      return;
    NBTTagCompound groupTag = new NBTTagCompound();
    groupTag.setString("group_name", group);
    ItemStack s = icon.copy();
    s.stackSize = 1;
    s.writeToNBT(groupTag);
    NBTTagCompound stackTag;
    if (!stack.hasTagCompound()) {
      stackTag = new NBTTagCompound();
    } else {
      stackTag = stack.getTagCompound();
    }
    stackTag.setTag("MNM|DataGroup", groupTag);
    stack.setTagCompound(stackTag);
  }
}
