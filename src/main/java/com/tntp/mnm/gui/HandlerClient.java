package com.tntp.mnm.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class HandlerClient extends HandlerServer {
  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return new GuiMain();
  }
}
