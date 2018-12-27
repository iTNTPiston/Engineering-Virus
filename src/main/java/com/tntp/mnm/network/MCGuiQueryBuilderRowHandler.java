package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;
import com.tntp.mnm.tileentity.TileQueryBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MCGuiQueryBuilderRowHandler implements IMessageHandler<MCGuiQueryBuilderRow, IMessage> {

  @Override
  public IMessage onMessage(MCGuiQueryBuilderRow message, MessageContext ctx) {
    Container c = Minecraft.getMinecraft().thePlayer.openContainer;
    if (c.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) c;
      TileQueryBuilder tile = (TileQueryBuilder) cont.getTile();
      tile.setCurrentScrollIndex(message.getI2());
      tile.setCacheRowTotal(message.getI3());
    }
    return null;
  }

}
