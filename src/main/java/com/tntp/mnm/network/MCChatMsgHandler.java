package com.tntp.mnm.network;

import com.tntp.mnm.util.ClientUtil;
import com.tntp.mnm.util.LocalUtil;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MCChatMsgHandler implements IMessageHandler<MCChatMsg, IMessage> {

  @Override
  public IMessage onMessage(MCChatMsg message, MessageContext ctx) {
    String mes = message.isLocalized() ? message.getStr1() : LocalUtil.localize(message.getStr1());
    ClientUtil.printChatMessage(mes);
    return null;
  }

}
