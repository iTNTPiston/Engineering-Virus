package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MCChatMsg extends MAStr1<MCChatMsg> {

  public MCChatMsg(String str1) {
    super(str1);
  }

  public MCChatMsg() {
    super(null);
  }

  @Override
  public IMessage onMessage(MCChatMsg message, MessageContext ctx) {
    return null;
  }

}
