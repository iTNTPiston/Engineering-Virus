package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MCGuiDataDefinitionResponseHandler implements IMessageHandler<MCGuiDataDefinitionResponse, IMessage> {

  @Override
  public IMessage onMessage(MCGuiDataDefinitionResponse message, MessageContext ctx) {
    Container c = Minecraft.getMinecraft().thePlayer.openContainer;
    if (c.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) c;
      TileDataDefinitionTerminal tile = (TileDataDefinitionTerminal) cont.getTile();
      tile.setDefinitionLengthCache(message.getI2());
      tile.setCurrentRowForClient(message.getI3());
    }
    return null;
  }

}
