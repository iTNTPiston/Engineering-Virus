package com.tntp.ev.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EngineeringVirus.MODID, version = EngineeringVirus.VERSION)
public class EngineeringVirus {
  public static final String MODID = "Engineering Virus";
  public static final String VERSION = "1.0";
  @SidedProxy(clientSide = "com.tntp.ev.core.ClientProxy", serverSide = "com.tntp.ev.core.Proxy")
  public Proxy proxy;

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
