package com.tntp.mnm.core;

import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Proxy {
  public void preInit(FMLPreInitializationEvent event) {
    MNMBlocks.loadBlocks();
    MNMItems.loadItems();
  }

  public void init(FMLInitializationEvent event) {

  }

  public void postInit(FMLPostInitializationEvent event) {

  }

}
