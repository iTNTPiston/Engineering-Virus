package com.tntp.mnm.network;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class MNMNetwork {
  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MNMMod.MODID);
  private static int id = 0;

  public static void loadMessages(boolean clientSide) {

    // regMS all messages received on server side
    regMS(MSPlayerGui.class);
    if (clientSide) {
      // regMCC all messages received on client side
      // regMCC(MCUnivMacScreen.class, new MCUnivMacScreenHandler());

    } else {
      // regMCS all messages received on client side
      // regMCS(MCUnivMacScreen.class);
    }
  }

  public static <REQ extends SMessage<REQ>> void regMS(Class<REQ> c) {
    registerMessage(c, Side.SERVER);
  }

  public static <REQ extends SMessage<REQ>> void regMCS(Class<REQ> c) {
    registerMessage(c, Side.CLIENT);
  }

  public static <REQ extends SMessage<REQ>, REPLY extends IMessage> void regMCC(Class<REQ> c,
      IMessageHandler<REQ, REPLY> handler) {
    network.registerMessage(handler, c, id++, Side.CLIENT);
  }

  public static <REQ extends SMessage<REQ>> void registerMessage(Class<REQ> c, Side side) {
    try {
      REQ req = c.newInstance();
      network.registerMessage(req, c, id++, side);
    } catch (Exception e) {
      DebugUtil.log.error("Cannot instantiate message " + c.getSimpleName() + ", check constructor.");
    }
  }

}
