package com.tntp.mnm.item.tools;

import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCommonWrench extends SItemTool {

  public ItemCommonWrench() {
    super("commonWrench", GuiTabType.PROCESS);
  }

  @Override
  public boolean onToolUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    Block b = world.getBlock(x, y, z);
    if (b == MNMBlocks.heatPipe) {
      if (!world.isRemote && !BlockUtil.pipeConnectionStable(world, x, y, z, MNMBlocks.heatPipe))
        BlockUtil.pipeDetectConnection(world, x, y, z, 2, MNMBlocks.heatPipe);
      return true;
    }
    return false;
  }

}
