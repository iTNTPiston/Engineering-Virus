package com.tntp.mnm.api.power;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class ElectronTypes {
  private static Map<String, Class<? extends IElectron>> nameToClassMap = new HashMap<String, Class<? extends IElectron>>();
  private static Map<Class<? extends IElectron>, String> classToNameMap = new HashMap<Class<? extends IElectron>, String>();

  public static void registerElectronType(Class<? extends IElectron> c, String name) {
    nameToClassMap.put(name, c);
    classToNameMap.put(c, name);
  }

  public static String getElectronTypeFromClass(Class<? extends IElectron> c) {
    return classToNameMap.get(c);
  }

  public static Class<? extends IElectron> getElectronClassFromName(String name) {
    return nameToClassMap.get(name);
  }

  public static IElectron createAndLoadElectronFromNBT(NBTTagCompound nbt) {
    String name = nbt.getString("ei");
    Class<? extends IElectron> c = getElectronClassFromName(name);
    if (c != null) {
      try {
        IElectron e = c.newInstance();
        e.readFromNBT(nbt);
        return e;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static void writeElectronToNBT(IElectron e, NBTTagCompound nbt) {
    nbt.setString("ei", getElectronTypeFromClass(e.getClass()));
    e.writeToNBT(nbt);
  }

}
