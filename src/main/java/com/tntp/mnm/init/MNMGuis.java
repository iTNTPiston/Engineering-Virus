package com.tntp.mnm.init;

import java.util.Hashtable;

import com.tntp.mnm.core.MNMMod;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class MNMGuis {
  private static int nextID = 0;
  private static Hashtable<String, Integer> identifierToGuiId = new Hashtable<String, Integer>();
  @SidedProxy(clientSide = "com.tntp.mnm.gui.HandlerClient", serverSide = "com.tntp.mnm.gui.HandlerServer")
  private static IGuiHandler guiHandler;

  private static int getNextGuiID() {
    return nextID++;
  }

  public static void assignGuiID(String identifier) {
    identifierToGuiId.put(identifier, getNextGuiID());
  }

  public static void loadGuis() {
    assignGuiID("GuiHeat");
    assignGuiID("GuiHeatPipe");
    NetworkRegistry.INSTANCE.registerGuiHandler(MNMMod.MODID, guiHandler);

  }

  public static int getGuiID(String identifier) {
    return identifierToGuiId.get(identifier);
  }

}
