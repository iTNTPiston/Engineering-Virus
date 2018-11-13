package com.tntp.mnm.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;

public class SBlockModelSpecial extends SBlock {
  protected int modelId;

  protected SBlockModelSpecial(Material mat, String regName) {
    super(mat, regName);
  }

  public void setModelId(int i) {
    modelId = i;
  }

  public int getRenderType() {
    return modelId;
  }

}
