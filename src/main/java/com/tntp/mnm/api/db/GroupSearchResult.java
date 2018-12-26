package com.tntp.mnm.api.db;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class GroupSearchResult {
  private ArrayList<ItemStack> groupChipList;
  private ItemStack[] groupItems;
  private ItemStack currentGroup;
  private String searchedGroup;

  public GroupSearchResult(String searchFor) {
    searchedGroup = searchFor;
    groupChipList = new ArrayList<ItemStack>();
  }

  public ArrayList<ItemStack> getGroupChipList() {
    return groupChipList;
  }

  public ItemStack[] getGroupItems() {
    return groupItems;
  }

  public void setGroupItems(ItemStack[] stacks) {
    groupItems = stacks;
  }

  public String getGroup() {
    return searchedGroup;
  }

  public void setCurrentGroup(ItemStack s) {
    currentGroup = s;
  }

  public ItemStack getCurrentGroup() {
    return currentGroup;
  }
}
