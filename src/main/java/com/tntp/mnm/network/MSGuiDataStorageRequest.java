package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileQueryBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiDataStorageRequest extends MAInt1<MSGuiDataStorageRequest> {

  public MSGuiDataStorageRequest(int windowID) {
    super(windowID);
  }

  public MSGuiDataStorageRequest() {
    super(0);
  }

  @Override
  public IMessage onMessage(MSGuiDataStorageRequest message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.getI1()) {
      ContainerCont cont = (ContainerCont) player.openContainer;
      STileData tile = (STileData) cont.getTile();
      MCGuiDataStorageResponse mes = new MCGuiDataStorageResponse(message.getI1(), tile.receiveDataPullRequest());
      return mes;
    }
    return null;
  }

}
