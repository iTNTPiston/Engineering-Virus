package com.tntp.ev.core;

import com.tntp.ev.init.EVBlocks;
import com.tntp.ev.init.EVItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Proxy {
  public void preInit(FMLPreInitializationEvent event) {
    EVBlocks.loadBlocks();
    EVItems.loadItems();
  }

  public void init(FMLInitializationEvent event) {

  }

  public void postInit(FMLPostInitializationEvent event) {

  }

}
