package com.tntp.mnm.block;

import com.tntp.mnm.model.SimpleObjRenderer;

import net.minecraft.block.material.Material;

public class SBlockModelSpecial extends SBlock {

  protected SBlockModelSpecial(Material mat, String regName) {
    super(mat, regName);
  }

  public int getRenderType() {
    return SimpleObjRenderer.id;
  }

}
