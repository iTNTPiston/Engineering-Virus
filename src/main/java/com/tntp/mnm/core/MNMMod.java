package com.tntp.mnm.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MNMMod.MODID, version = MNMMod.VERSION)
public class MNMMod {
  public static final String MODID = "metalnetworkmainframe";
  public static final String VERSION = "1.7.10-1.0.0";
  @SidedProxy(clientSide = "com.tntp.mnm.core.ClientProxy", serverSide = "com.tntp.mnm.core.Proxy")
  public static Proxy proxy;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit(event);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }
}
