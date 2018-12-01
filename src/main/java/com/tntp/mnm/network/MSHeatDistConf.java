package com.tntp.mnm.network;

import com.tntp.mnm.gui.conf.ContainerConfigHeatDistributor;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSHeatDistConf extends MAInt2<MSHeatDistConf> {
  private boolean in;

  public MSHeatDistConf(int windowID, int conf, boolean in) {
    super(windowID, conf);
    this.in = in;
  }

  public MSHeatDistConf() {
    super(0, 0);
    in = false;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    in = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeBoolean(in);
  }

  @Override
  public IMessage onMessage(MSHeatDistConf message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    if (player.openContainer.windowId == message.getI1()) {
      ContainerConfigHeatDistributor container = (ContainerConfigHeatDistributor) player.openContainer;
      container.setTileSides(message.getI2(), message.in);
    }
    return null;
  }

}
