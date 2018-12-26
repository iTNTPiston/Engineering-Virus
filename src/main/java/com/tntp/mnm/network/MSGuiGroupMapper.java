package com.tntp.mnm.network;

import com.tntp.mnm.gui.conf.ContainerConfigHeatDistributor;
import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.tileentity.TileDataGroupDefiner;
import com.tntp.mnm.tileentity.TileGroupMapper;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSGuiGroupMapper extends MAStr1<MSGuiGroupMapper> {
  private int windowID;
  private int buttonID;

  public MSGuiGroupMapper(int window, int button, String groupName) {
    super(groupName);
    windowID = window;
    buttonID = button;
  }

  public MSGuiGroupMapper() {
    super("");
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    windowID = buf.readInt();
    buttonID = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeInt(windowID);
    buf.writeInt(buttonID);
  }

  @Override
  public IMessage onMessage(MSGuiGroupMapper message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.windowID) {
      ContainerCont container = (ContainerCont) player.openContainer;
      ITileCont t = container.getTile();
      if (t instanceof TileGroupMapper) {
        if (message.buttonID == 0) {
          return new MCGuiGroupMapperItemDef(message.windowID,
              ((TileGroupMapper) t).receiveClientGuiSearch(message.getStr1()));
        }
        return ((TileGroupMapper) t).receiveClientGuiMessage(message.buttonID);
      }
    }
    return null;
  }

}
