package com.tntp.mnm.api.db;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemDef {
  public ItemStack stack;
  public int id;

  public void toNBT(NBTTagCompound tag) {
    stack.writeToNBT(tag);
    tag.setInteger("MNM|ItemDefID", id);
  }

  public void fromNBT(NBTTagCompound tag) {
    stack = ItemStack.loadItemStackFromNBT(tag);
    id = tag.getInteger("MNM|ItemDefID");
  }
}
