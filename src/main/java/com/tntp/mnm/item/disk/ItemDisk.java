package com.tntp.mnm.item.disk;

import com.tntp.mnm.item.SItem;
import com.tntp.mnm.tileentity.STileData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemDisk extends SItem {
  public int dataSize;// 256 items take 1, 1 data def takes 4

  public ItemDisk(int size) {
    dataSize = size;
  }

  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof STileData) {
      // tile can take disk
      ((STileData) tile).insertDisk(stack);
      return true;
    }
    return false;
  }

}
