package com.tntp.mnm.gui;

import com.tntp.mnm.api.ek.HeatPipe;
import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.gui.conf.GuiConfigHeatDistributor;
import com.tntp.mnm.gui.cont.GuiCont;
import com.tntp.mnm.gui.cont.GuiContDataDefinitionStorage;
import com.tntp.mnm.gui.cont.GuiContDataDefinitionTerminal;
import com.tntp.mnm.gui.cont.GuiContDataGroupChipset;
import com.tntp.mnm.gui.cont.GuiContDataGroupDefiner;
import com.tntp.mnm.gui.cont.GuiContGroupMapper;
import com.tntp.mnm.gui.cont.GuiContBasicDiskStorage;
import com.tntp.mnm.gui.cont.GuiContBasicGroupMappingStorage;
import com.tntp.mnm.gui.cont.GuiContQueryBuilder;
import com.tntp.mnm.gui.cont.GuiContSecurityEncoder;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.gui.diskkey.GuiDiskKey;
import com.tntp.mnm.gui.diskkey.ITileDiskKeyable;
import com.tntp.mnm.gui.heat.GuiHeat;
import com.tntp.mnm.gui.process.ContainerProcess;
import com.tntp.mnm.gui.process.ContainerProcessGeoThermalSmelter;
import com.tntp.mnm.gui.process.GuiProcessGeoThermalSmelter;
import com.tntp.mnm.gui.process.ITileProcess;
import com.tntp.mnm.gui.structure.GuiStructureGeoThermalSmelter;
import com.tntp.mnm.gui.structure.GuiStructureHeatCollectorFirewall;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.STileHeatNode;
import com.tntp.mnm.tileentity.TileDataDefinitionStorage;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;
import com.tntp.mnm.tileentity.TileDataGroupChipset;
import com.tntp.mnm.tileentity.TileDataGroupDefiner;
import com.tntp.mnm.tileentity.TileBasicDiskStorage;
import com.tntp.mnm.tileentity.TileBasicGroupMappingStorage;
import com.tntp.mnm.tileentity.TileGeoThermalSmelter;
import com.tntp.mnm.tileentity.TileGroupMapper;
import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;
import com.tntp.mnm.tileentity.TileHeatDistributor;
import com.tntp.mnm.tileentity.TileQueryBuilder;
import com.tntp.mnm.tileentity.TileSecurityEncoder;
import com.tntp.mnm.util.BlockUtil;

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
    } else if (ID == MNMGuis.getGuiID("GuiConnection")) {
      Block b = world.getBlock(x, y, z);
      if (b == MNMBlocks.heat_pipe || b == MNMBlocks.neithernet_cable) {
        gui = new GuiConnection(new ContainerConnection(player.inventory, null, null, null), x, y, z);
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
    } else if (ID == MNMGuis.getGuiID("GuiProcessGeoThermalSmelter")) {
      if (tile instanceof TileGeoThermalSmelter) {
        gui = new GuiProcessGeoThermalSmelter(player.inventory, (TileGeoThermalSmelter) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiConfigHeatDistributor")) {
      if (tile instanceof TileHeatDistributor) {
        gui = new GuiConfigHeatDistributor(player.inventory, (TileHeatDistributor) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContSecurityEncoder")) {
      if (tile instanceof TileSecurityEncoder) {
        gui = new GuiContSecurityEncoder(player.inventory, (ITileCont) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContDataGroupDefiner")) {
      if (tile instanceof TileDataGroupDefiner) {
        gui = new GuiContDataGroupDefiner(player.inventory, (ITileCont) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContDataDefinitionTerminal")) {
      if (tile instanceof TileDataDefinitionTerminal) {
        gui = new GuiContDataDefinitionTerminal(player.inventory, (TileDataDefinitionTerminal) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContQueryBuilder")) {
      if (tile instanceof TileQueryBuilder) {
        gui = new GuiContQueryBuilder(player.inventory, (ITileCont) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContBasicDiskStorage")) {
      if (tile instanceof TileBasicDiskStorage) {
        gui = new GuiContBasicDiskStorage(player.inventory, (TileBasicDiskStorage) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContDataDefinitionStorage")) {
      if (tile instanceof TileDataDefinitionStorage) {
        gui = new GuiContDataDefinitionStorage(player.inventory, (TileDataDefinitionStorage) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContDataGroupChipset")) {
      if (tile instanceof TileDataGroupChipset) {
        gui = new GuiContDataGroupChipset(player.inventory, (ITileCont) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContBasicGroupMappingStorage")) {
      if (tile instanceof TileBasicGroupMappingStorage) {
        gui = new GuiContBasicGroupMappingStorage(player.inventory, (STileData) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiContGroupMapper")) {
      if (tile instanceof TileGroupMapper) {
        gui = new GuiContGroupMapper(player.inventory, (ITileCont) tile, x, y, z);
      }
    } else if (ID == MNMGuis.getGuiID("GuiDiskKey")) {
      if (tile instanceof ITileDiskKeyable) {
        gui = new GuiDiskKey(player.inventory, (ITileDiskKeyable) tile, x, y, z);
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
