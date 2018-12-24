package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.STileData;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MCGuiDataStorageResponseHandler implements IMessageHandler<MCGuiDataStorageResponse, IMessage> {

  @Override
  public IMessage onMessage(MCGuiDataStorageResponse message, MessageContext ctx) {
    Container c = Minecraft.getMinecraft().thePlayer.openContainer;
    if (c.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) c;
      STileData tile = (STileData) cont.getTile();
      tile.setUsedSpaceCache(message.getI2());
    }
    return null;
  }

}
