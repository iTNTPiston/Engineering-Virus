package com.tntp.mnm.gui;

import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.gui.container.ContainerHeat;
import com.tntp.mnm.init.MNMGuis;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
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
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  }

}
