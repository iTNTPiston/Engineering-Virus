package com.tntp.mnm.item.tools;

import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.item.SItemTool;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSmallHammer extends SItemTool {

  public ItemSmallHammer() {
    super("smallHammer");
  }

  @Override
  public boolean onToolUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    Block b = world.getBlock(x, y, z);
    if (b == MNMBlocks.heat_pipe) {
      if (!world.isRemote) {
        int meta = world.getBlockMetadata(x, y, z);
        meta = (meta + 1) & 15;
        world.setBlockMetadataWithNotify(x, y, z, meta, 3);
      }
      return true;
    }
    return false;
  }

}
