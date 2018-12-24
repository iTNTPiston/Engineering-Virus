package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MCGuiDataStorageResponse extends MAInt2<MCGuiDataStorageResponse> {

  public MCGuiDataStorageResponse(int windowID, int usedSpace) {
    super(windowID, usedSpace);
  }

  public MCGuiDataStorageResponse() {
    super(0, 0);
  }

  @Override
  public IMessage onMessage(MCGuiDataStorageResponse message, MessageContext ctx) {
    return null;
  }

}
