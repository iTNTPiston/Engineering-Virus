package com.tntp.mnm.core;

import com.tntp.mnm.init.MNMCompat;
import com.tntp.mnm.init.MNMRender;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends Proxy {
  public void init(FMLInitializationEvent event) {
    super.init(event);
    MNMRender.loadRenderers();
  }

}
