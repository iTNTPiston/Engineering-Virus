package com.tntp.ev.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;

@Mod(modid = EngineeringVirus.MODID, version = EngineeringVirus.VERSION)
public class EngineeringVirus {
  public static final String MODID = "Engineering Virus";
  public static final String VERSION = "1.0";

  @EventHandler
  public void init(FMLInitializationEvent event) {
    // some example code
    System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

  }
}
