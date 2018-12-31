package com.tntp.mnm.item.tools;

import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.util.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemCommonWrench extends SItemTool {

  public ItemCommonWrench() {
    super(GuiTabType.PROCESS);
  }

  @Override
  public boolean onToolUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    Block b = world.getBlock(x, y, z);
    if (b == MNMBlocks.heat_pipe || b == MNMBlocks.neithernet_cable) {
      if (!world.isRemote && !BlockUtil.pipeConnectionStable(world, x, y, z, MNMBlocks.heat_pipe))
        BlockUtil.pipeDetectConnection(world, x, y, z, 2, MNMBlocks.heat_pipe);
      return true;
    } else if (b == MNMBlocks.central_processor) {
      if (!world.isRemote) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileCentralProcessor) {
          Mainframe mf = ((TileCentralProcessor) te).getMainframe();
          if (mf.canHarvest()) {
            world.setBlockToAir(x, y, z);
            EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(b));
            world.spawnEntityInWorld(item);
          }
        }
      }
    }
    return false;
  }

}
