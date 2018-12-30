package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MCChatMsg extends MAStr1<MCChatMsg> {
  private boolean localized;

  public MCChatMsg(String str1, boolean localized) {
    super(str1);
    this.localized = localized;
  }

  public MCChatMsg() {
    super(null);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeBoolean(localized);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    localized = buf.readBoolean();
  }

  public boolean isLocalized() {
    return localized;
  }

  @Override
  public IMessage onMessage(MCChatMsg message, MessageContext ctx) {
    return null;
  }

}
