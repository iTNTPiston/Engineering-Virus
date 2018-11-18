package com.tntp.mnm.compat.nei;

import com.tntp.mnm.compat.ICompat;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "ICompat", modid = "NotEnoughItems")
public class NEICompat implements ICompat {

  @Override
  @Optional.Method(modid = "NotEnoughItems")
  public void load(boolean clientSide) {
    if (clientSide) {
      API.registerNEIGuiHandler(new IGuiMainHandler());
    }
  }

}
