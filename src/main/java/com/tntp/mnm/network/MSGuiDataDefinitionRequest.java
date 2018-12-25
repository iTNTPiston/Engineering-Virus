package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiDataDefinitionRequest extends MAInt2<MSGuiDataDefinitionRequest> {
  public MSGuiDataDefinitionRequest(int windowID, int scrollTo) {
    super(windowID, scrollTo);
  }

  public MSGuiDataDefinitionRequest() {
    super(0, 0);
  }

  @Override
  public IMessage onMessage(MSGuiDataDefinitionRequest message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) player.openContainer;
      TileDataDefinitionTerminal tile = (TileDataDefinitionTerminal) cont.getTile();
      if (message.getI2() >= 0)
        tile.scrollTo(message.getI2());
      int defLength = tile.getDefinitionLength();
      int row = tile.getCurrentRow();
      MCGuiDataDefinitionResponse mes = new MCGuiDataDefinitionResponse(message.getI1(), defLength, row);
      return mes;
    }
    return null;
  }
}
