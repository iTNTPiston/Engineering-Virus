package com.tntp.mnm.init;

import java.util.ArrayList;
import java.util.Hashtable;

import com.tntp.mnm.core.MNMMod;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class MNMGuis {
  private static int nextID = 0;
  private static Hashtable<String, Integer> identifierToGuiId = new Hashtable<String, Integer>();
  private static ArrayList<String> guiIdToIdentifier = new ArrayList<String>();
  @SidedProxy(clientSide = "com.tntp.mnm.gui.HandlerClient", serverSide = "com.tntp.mnm.gui.HandlerServer")
  private static IGuiHandler guiHandler;

  private static int getNextGuiID() {
    return nextID++;
  }

  public static void assignGuiID(String identifier) {
    identifierToGuiId.put(identifier, getNextGuiID());
    guiIdToIdentifier.add(identifier);
  }

  public static void loadGuis() {

    assignGuiID("GuiHeat");
    assignGuiID("GuiHeatPipe");
    assignGuiID("GuiStructureHeatCollectorFirewall");
    assignGuiID("GuiStructureGeoThermalSmelter");
    assignGuiID("GuiProcessGeoThermalSmelter");
    assignGuiID("GuiConfigHeatDistributor");
    assignGuiID("GuiContSecurityEncoder");
    assignGuiID("GuiContDataGroupDefiner");

    NetworkRegistry.INSTANCE.registerGuiHandler(MNMMod.MODID, guiHandler);

  }

  public static int getGuiID(String identifier) {
    return identifierToGuiId.get(identifier);
  }

  public static String getGui(int id) {
    return guiIdToIdentifier.get(id);
  }

}
