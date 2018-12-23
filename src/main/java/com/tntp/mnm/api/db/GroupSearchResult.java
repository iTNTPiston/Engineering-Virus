package com.tntp.mnm.api.db;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class GroupSearchResult {
  private ArrayList<ItemStack> groupChipList;
  private ArrayList<ItemStack> groupPage;
  private String searchedGroup;

  public GroupSearchResult(String searchFor) {
    searchedGroup = searchFor;
    groupChipList = new ArrayList<ItemStack>();
    groupPage = new ArrayList<ItemStack>();
  }

  public ArrayList<ItemStack> getGroupChipList() {
    return groupChipList;
  }

  public ArrayList<ItemStack> getGroupItems() {
    return groupPage;
  }

  public String getGroup() {
    return searchedGroup;
  }
}
