package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileGroupMapper;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MCGuiGroupMapperItemDefHandler implements IMessageHandler<MCGuiGroupMapperItemDef, IMessage> {

  @Override
  public IMessage onMessage(MCGuiGroupMapperItemDef message, MessageContext ctx) {
    Container c = Minecraft.getMinecraft().thePlayer.openContainer;
    if (c.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) c;
      TileGroupMapper tile = (TileGroupMapper) cont.getTile();
      tile.setItemDefCache(message.getI2());
    }
    return null;
  }

}
