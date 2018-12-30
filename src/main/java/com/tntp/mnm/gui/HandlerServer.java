package com.tntp.mnm.gui;

import com.tntp.mnm.api.TileEntityConnection;
import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.api.neither.NeitherPipe;
import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.block.IBlockBidirectionalPipe;
import com.tntp.mnm.gui.conf.ContainerConfigHeatDistributor;
import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.gui.cont.ITileDataCont;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.gui.diskkey.ContainerDiskKey;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;
import com.tntp.mnm.gui.heat.ContainerHeat;
import com.tntp.mnm.gui.process.ContainerProcessGeoThermalSmelter;
import com.tntp.mnm.gui.process.ITileProcess;
import com.tntp.mnm.gui.structure.ContainerStructure;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.network.MCChatMsg;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.tileentity.STileHeatNode;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.tileentity.TileHeatDistributor;
import com.tntp.mnm.util.BlockUtil;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
    } else if (ID == MNMGuis.getGuiID("GuiConnection")) {
      Block b = world.getBlock(x, y, z);
      if (b instanceof IBlockBidirectionalPipe) {
        int sides = BlockUtil.pipeMetaToSide(world.getBlockMetadata(x, y, z));
        int side0 = sides >> 4;
        int side1 = sides & 15;
        ItemStack end0 = null;
        ItemStack end1 = null;
        TileEntityConnection pipe0 = null;
        TileEntityConnection pipe1 = null;
        int outSide0 = -1, outSide1 = -1;
        if (b == MNMBlocks.heat_pipe) {
          pipe0 = new HeatPipe(x, y, z);
          outSide0 = STileHeatNode.findHeatNode((HeatPipe) pipe0, side0, 3, world);
          pipe1 = new HeatPipe(x, y, z);
          outSide1 = STileHeatNode.findHeatNode((HeatPipe) pipe1, side1, 3, world);
        }
        if (b == MNMBlocks.neithernet_cable) {
          pipe0 = new NeitherPipe(x, y, z);
          outSide0 = STileNeithernet.findNeither((NeitherPipe) pipe0, side0, world);
          pipe1 = new NeitherPipe(x, y, z);
          outSide1 = STileNeithernet.findNeither((NeitherPipe) pipe1, side1, world);
        }
        if (outSide0 != -1) {
          TileEntity tile = (TileEntity) pipe0.getTileEntity(world);
          if (tile != null) {
            end0 = new ItemStack(tile.getBlockType());
          }
        }
        if (outSide1 != -1) {
          TileEntity tile = (TileEntity) pipe1.getTileEntity(world);
          if (tile != null) {
            end1 = new ItemStack(tile.getBlockType());
          }
        }
        return new ContainerConnection(player.inventory, end0, end1, new ItemStack(b));
      }
    } else if (ID == MNMGuis.getGuiID("GuiProcessGeoThermalSmelter")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof ITileProcess) {
        return new ContainerProcessGeoThermalSmelter(player.inventory, (ITileProcess) tile);
      }
    } else if (ID == MNMGuis.getGuiID("GuiConfigHeatDistributor")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileHeatDistributor) {
        return new ContainerConfigHeatDistributor(player.inventory, (TileHeatDistributor) tile);
      }
    } else if (MNMGuis.getGui(ID).startsWith("GuiCont")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      boolean canOpen = false;
      if (tile instanceof ITileDataCont) {
        if (((ITileDataCont) tile).canReadData()) {
          canOpen = true;
        }
      } else if (tile instanceof ITileSecuredCont) {
        Security s = ((ITileSecuredCont) tile).getSecurity();
        ItemStack using = player.getCurrentEquippedItem();
        if (s.securityCheck(using)) {
          canOpen = true;
        } else {
          MNMNetwork.network.sendTo(new MCChatMsg("mnm.message.security.secured", false), (EntityPlayerMP) player);
        }
      } else if (tile instanceof ITileCont) {
        canOpen = true;
      }
      if (canOpen) {
        return new ContainerCont(player.inventory, (ITileCont) tile);
      }
    } else if (MNMGuis.getGui(ID).startsWith("GuiStructure")) {
      return new ContainerStructure(player.inventory);
    } else if (ID == MNMGuis.getGuiID("GuiDiskKey")) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof ITileDiskKeyable) {
        return new ContainerDiskKey(player.inventory, (ITileDiskKeyable) tile);
      }
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  }

}
