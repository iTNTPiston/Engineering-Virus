package com.tntp.mnm.gui;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.gui.heat.ContainerHeat;
import com.tntp.mnm.gui.heat.ContainerHeatPipe;
import com.tntp.mnm.gui.process.ContainerProcessGeoThermalSmelter;
import com.tntp.mnm.gui.process.ITileProcess;
import com.tntp.mnm.gui.structure.ContainerStructure;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.tileentity.STileHeatNode;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HandlerServer implements IGuiHandler {

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == MNMGuis.getGuiID("GuiHeat")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof IHeatNode) {
        return new ContainerHeat(player.inventory, (IHeatNode) tile);
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
        return new ContainerHeatPipe(player.inventory, end0, end1);
      }
    } else if (ID == MNMGuis.getGuiID("GuiProcessGeoThermalSmelter")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof ITileProcess) {
        return new ContainerProcessGeoThermalSmelter(player.inventory, (ITileProcess) tile);
      }
    } else if (MNMGuis.getGui(ID).startsWith("GuiStructure")) {
      return new ContainerStructure(player.inventory);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  }

}
