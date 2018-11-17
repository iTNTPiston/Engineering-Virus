package com.tntp.mnm.core;

import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMCompat;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.init.MNMNetworkInit;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Proxy {

  public void preInit(FMLPreInitializationEvent event) {
    MNMBlocks.loadBlocks();
    MNMItems.loadItems();
    MNMGuis.loadGuis();
  }

  public void init(FMLInitializationEvent event) {
    MNMNetworkInit.loadNetwork(this instanceof ClientProxy);
  }

  public void postInit(FMLPostInitializationEvent event) {
    MNMCompat.loadCompats(this instanceof ClientProxy);
  }

}
