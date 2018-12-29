package com.tntp.mnm.item;

import java.util.List;

import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.util.LocalUtil;

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
      list.add(LocalUtil.localize("mnm.tooltip.data_group_arg_s", groupName));
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
    if (stack == null)
      return "";
    if (!stack.hasTagCompound())
      return "";
    return getGroupNameFromNBT(stack.getTagCompound());
  }

  public ItemStack getGroupIcon(ItemStack stack) {
    if (stack == null)
      return null;
    if (!stack.hasTagCompound())
      return null;
    return getGroupIconFromNBT(stack.getTagCompound());
  }

  /**
   * Return the iconStack, with an NBTTag containing the last part of the group
   * name (the part after last dot)
   * 
   * @param stack
   * @return
   */
  public ItemStack getGroupIconWithNameTag(ItemStack stack) {
    ItemStack iconStack = getGroupIcon(stack);
    if (iconStack == null)
      return null;
    NBTTagCompound stackTag = iconStack.hasTagCompound() ? iconStack.getTagCompound() : new NBTTagCompound();
    String groupName = MNMItems.data_group_chip.getGroupName(stack);
    stackTag.setString("MNM|DataGroupNameTag", groupName);
    iconStack.setTagCompound(stackTag);
    return iconStack;
  }

  /**
   * Get group name from an iconstack returned from getGroupIconWithNameTag. If it
   * is not such stack, null is returned
   * 
   * @param iconStack
   * @return
   */
  public String getTaggedNameFromIcon(ItemStack iconStack) {
    if (iconStack == null || !iconStack.hasTagCompound())
      return null;
    NBTTagCompound tag = iconStack.getTagCompound();
    if (tag.hasKey("MNM|DataGroupNameTag")) {
      return tag.getString("MNM|DataGroupNameTag");
    }
    return null;
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
    if (!isGroupNameValid(group))
      return;

    NBTTagCompound groupTag = new NBTTagCompound();
    groupTag.setString("group_name", group);
    ItemStack s = icon.copy();
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

  public static boolean isGroupNameValid(String group) {
    if (group == null || group.length() == 0 || group.endsWith("."))
      return false;
    int dotPos = group.indexOf('.');
    while (dotPos != -1) {// cannot have two consecutive dots
      int nextDotPos = group.indexOf('.', dotPos + 1);
      if (nextDotPos == dotPos + 1)
        return false;
      dotPos = nextDotPos;
    }
    return true;
  }
}
