package com.tntp.mnm.init;

import com.tntp.mnm.network.MNMNetwork;

public class MNMNetworkInit {
  public static void loadNetwork(boolean clientSide) {
    MNMNetwork.loadMessages(clientSide);
  }
}
