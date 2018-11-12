package com.tntp.mnm.core;

import com.tntp.mnm.init.EVBlocks;
import com.tntp.mnm.init.EVItems;

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
