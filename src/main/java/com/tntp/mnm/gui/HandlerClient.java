package com.tntp.mnm.gui;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.gui.container.ContainerHeatPipe;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.item.ItemMNMTool;
import com.tntp.mnm.tileentity.STileHeatNode;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HandlerClient extends HandlerServer {
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    GuiMain gui = null;
    if (ID == MNMGuis.getGuiID("GuiHeat")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof IHeatNode) {
        gui = new GuiHeat(player.inventory, (IHeatNode) tile);
      }
    } else if (ID == MNMGuis.getGuiID("GuiHeatPipe")) {
      Block b = world.getBlock(x, y, z);
      if (b == MNMBlocks.blockHeatPipe) {
        int sides = BlockHeatPipe.metaToSide(world.getBlockMetadata(x, y, z));
        int side0 = sides >> 4;
        int side1 = sides & 15;
        ItemStack end0 = null;
        ItemStack end1 = null;
        HeatPipe p = new HeatPipe(x, y, z);
        int outSide0 = STileHeatNode.findHeatNode(p, side0, 3, world);
        if (outSide0 != -1) {
          TileEntity tile = (TileEntity) p.getEnd(world);
          if (tile != null) {
            end0 = new ItemStack(tile.getBlockType());
          }
        }
        p = new HeatPipe(x, y, z);
        int outSide1 = STileHeatNode.findHeatNode(p, side1, 3, world);
        if (outSide1 != -1) {
          TileEntity tile = (TileEntity) p.getEnd(world);
          if (tile != null) {
            end1 = new ItemStack(tile.getBlockType());
          }
        }
        gui = new GuiHeatPipe(new ContainerHeatPipe(player.inventory, end0, end1));
      }
    }
    if (gui != null) {
      ItemStack stack = player.getCurrentEquippedItem();
      if (stack != null && stack.getItem() instanceof ItemMNMTool) {
        ItemMNMTool toolItem = (ItemMNMTool) stack.getItem();
        toolItem.setGuiTabsWithCapability(stack, world, x, y, z, gui, toolItem.getGuiCapability());
      }
      return gui;
    }
    return null;
  }
}
