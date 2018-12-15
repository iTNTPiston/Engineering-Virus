package com.tntp.mnm.item.disk;

import java.util.List;

import com.tntp.mnm.item.SItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemDisk extends SItem {
  public int dataSize;// 256 items take 1, 1 data def takes 4

  public ItemDisk(String regName, int size) {
    super(regName);
    dataSize = size;
  }

}
