package com.tntp.mnm.network;

import com.tntp.mnm.gui.diskkey.ContainerDiskKey;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class MSGuiDiskKey extends MAInt2<MSGuiDiskKey> {

  public MSGuiDiskKey(int window, int action) {
    super(window, action);
  }

  public MSGuiDiskKey() {
    super(0, 0);
  }

  @Override
  public IMessage onMessage(MSGuiDiskKey message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.getI1()) {
      ContainerDiskKey cont = (ContainerDiskKey) player.openContainer;
      cont.receiveActionFromClient(message.getI2(), player);
    }
    return null;
  }

}
