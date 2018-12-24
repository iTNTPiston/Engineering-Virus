package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.TileQueryBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiQueryBuilder extends MAInt2<MSGuiQueryBuilder> {

  public MSGuiQueryBuilder(int windowID, int buttonID) {
    super(windowID, buttonID);
  }

  public MSGuiQueryBuilder() {
    super(0, 0);
  }

  @Override
  public IMessage onMessage(MSGuiQueryBuilder message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) player.openContainer;
      TileQueryBuilder tile = (TileQueryBuilder) cont.getTile();
      return tile.receiveClientGuiMessage(message.getI2());
    }
    return null;
  }

}
