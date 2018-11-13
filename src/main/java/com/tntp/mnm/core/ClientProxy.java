package com.tntp.mnm.core;

import com.tntp.mnm.init.MNMRender;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends Proxy {
  public void init(FMLInitializationEvent event) {
    super.init(event);
    MNMRender.loadRenderers();
  }
}
