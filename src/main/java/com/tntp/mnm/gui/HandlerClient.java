package com.tntp.mnm.gui;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.gui.heat.ContainerHeatPipe;
import com.tntp.mnm.gui.heat.GuiHeat;
import com.tntp.mnm.gui.heat.GuiHeatPipe;
import com.tntp.mnm.gui.structure.GuiStructureGeoThermalSmelter;
import com.tntp.mnm.gui.structure.GuiStructureHeatCollectorFirewall;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.tileentity.STileHeatNode;
import com.tntp.mnm.tileentity.TileGeoThermalSmelter;
import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HandlerClient extends HandlerServer {
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    SGui gui = null;
    TileEntity tile = world.getTileEntity(x, y, z);
    if (ID == MNMGuis.getGuiID("GuiHeat")) {
      if (tile instanceof IHeatNode) {
        gui = new GuiHeat(player.inventory, (IHeatNode) tile, x, y, z);
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
          TileEntity te = (TileEntity) p.getEnd(world);
          if (te != null) {
            end0 = new ItemStack(te.getBlockType());
          }
        }
        p = new HeatPipe(x, y, z);
        int outSide1 = STileHeatNode.findHeatNode(p, side1, 3, world);
        if (outSide1 != -1) {
          TileEntity te = (TileEntity) p.getEnd(world);
          if (te != null) {
            end1 = new ItemStack(te.getBlockType());
          }
        }
        gui = new GuiHeatPipe(new ContainerHeatPipe(player.inventory, end0, end1), x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiStructureHeatCollectorFirewall")) {
      if (tile instanceof TileHeatCollectorFirewall) {
        gui = new GuiStructureHeatCollectorFirewall(player.inventory,
            ((TileHeatCollectorFirewall) tile).getInventoryName(), x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiStructureGeoThermalSmelter")) {
      if (tile instanceof TileGeoThermalSmelter) {
        gui = new GuiStructureGeoThermalSmelter(player.inventory, ((TileGeoThermalSmelter) tile).getInventoryName(), x,
            y, z);
      }
    }
    if (gui != null) {
      ItemStack stack = player.getCurrentEquippedItem();
      if (stack != null && stack.getItem() instanceof SItemTool) {
        SItemTool toolItem = (SItemTool) stack.getItem();
        toolItem.setGuiTabsWithCapability(stack, world, x, y, z, gui, toolItem.getGuiCapability());
      }
      return gui;
    }
    return null;
  }
}
