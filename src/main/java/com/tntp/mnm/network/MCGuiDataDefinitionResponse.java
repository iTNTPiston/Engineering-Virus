package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MCGuiDataDefinitionResponse extends MAInt3<MCGuiDataDefinitionResponse> {

  public MCGuiDataDefinitionResponse(int windowID, int defLength, int row) {
    super(windowID, defLength, row);
  }

  public MCGuiDataDefinitionResponse() {
    super(0, 0, 0);
  }

  @Override
  public IMessage onMessage(MCGuiDataDefinitionResponse message, MessageContext ctx) {
    return null;
  }

}
