package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MCGuiQueryBuilderRow extends MAInt3<MCGuiQueryBuilderRow> {

  public MCGuiQueryBuilderRow(int windowID, int current, int total) {
    super(windowID, current, total);
  }

  public MCGuiQueryBuilderRow() {
    super(0, 0, 0);
  }

  @Override
  public IMessage onMessage(MCGuiQueryBuilderRow message, MessageContext ctx) {
    return null;
  }

}
