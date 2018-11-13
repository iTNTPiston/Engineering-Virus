package com.tntp.mnm.init;

import java.util.HashMap;

import com.tntp.mnm.core.MNMMod;

import net.minecraft.util.ResourceLocation;

/**
 * This is to prevent loading the same resource multiple times
 * 
 * @author iTNTPiston
 *
 */
public class MNMResources {
  private static HashMap<String, ResourceLocation> loadedResources = new HashMap<String, ResourceLocation>();

  public static ResourceLocation getResource(String location) {
    ResourceLocation rl = loadedResources.get(location);
    if (rl == null) {
      rl = new ResourceLocation(MNMMod.MODID, location);
      loadedResources.put(location, rl);
    }
    return rl;
  }
}
