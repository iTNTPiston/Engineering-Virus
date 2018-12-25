package com.tntp.mnm.core;

import com.tntp.mnm.event.ClientEvent;
import com.tntp.mnm.init.MNMCompat;
import com.tntp.mnm.init.MNMRender;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends Proxy {
  public void init(FMLInitializationEvent event) {
    super.init(event);
    MNMRender.loadRenderers();
  }

  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
    MinecraftForge.EVENT_BUS.register(new ClientEvent());
  }

}
