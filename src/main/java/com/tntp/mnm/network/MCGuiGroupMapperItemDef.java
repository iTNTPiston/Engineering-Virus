package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MCGuiGroupMapperItemDef extends MAInt2<MCGuiGroupMapperItemDef> {

  public MCGuiGroupMapperItemDef(int windowID, int itemDef) {
    super(windowID, itemDef);
  }

  public MCGuiGroupMapperItemDef() {
    super(0, 0);
  }

  @Override
  public IMessage onMessage(MCGuiGroupMapperItemDef message, MessageContext ctx) {
    return null;
  }

}
