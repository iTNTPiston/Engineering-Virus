package com.tntp.mnm.network;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.tileentity.TileDataGroupDefiner;
import com.tntp.mnm.tileentity.TileDataIntegrityChipset;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiDataIntegrityChipset extends MAInt3<MSGuiDataIntegrityChipset> {

  public MSGuiDataIntegrityChipset(int window, int button, int flag) {
    super(window, button, flag);
  }

  public MSGuiDataIntegrityChipset() {
    super(0, 0, 0);
  }

  @Override
  public IMessage onMessage(MSGuiDataIntegrityChipset message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.getI1()) {
      ContainerCont container = (ContainerCont) player.openContainer;
      ITileCont t = container.getTile();
      if (t instanceof TileDataIntegrityChipset) {
        ((TileDataIntegrityChipset) t).receiveClientAction(message.getI2(), message.getI3());
      }
    }
    return null;
  }

}
