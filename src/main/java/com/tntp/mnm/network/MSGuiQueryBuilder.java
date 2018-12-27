package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.TileQueryBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiQueryBuilder extends MAInt2<MSGuiQueryBuilder> {

  public MSGuiQueryBuilder(int windowID, int buttonID) {
    super(windowID, buttonID);
    // 0 - PUT
    // 1 - TAKE
    // 2 - RETURN
    // 3,4,5,6,7 - groups
    // 8 - group scroll left
    // 9 group scroll right
    // 10 items scroll up
    // 11 items scroll down
    // 12-26 add 1 stack
    // 27+[0-14] subtract 1 stack
    // 42+[0-14] add 1
    // 57+[0-14] subtract 1
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
      return tile.receiveClientGuiMessage(message.getI1(), message.getI2());
    }
    return null;
  }

}
