package com.tntp.mnm.network;

import com.tntp.mnm.gui.conf.ContainerConfigHeatDistributor;
import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.tileentity.TileDataGroupDefiner;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiDataGroupDefine extends MAStr1<MSGuiDataGroupDefine> {
  private int windowID;

  public MSGuiDataGroupDefine(int window, String groupName) {
    super(groupName);
    windowID = window;
  }

  public MSGuiDataGroupDefine() {
    super("");
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    windowID = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeInt(windowID);
  }

  @Override
  public IMessage onMessage(MSGuiDataGroupDefine message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.windowID) {
      ContainerCont container = (ContainerCont) player.openContainer;
      ITileCont t = container.getTile();
      if (t instanceof TileDataGroupDefiner) {
        ((TileDataGroupDefiner) t).defineGroup(message.getStr1());
      }
    }
    return null;
  }

}
