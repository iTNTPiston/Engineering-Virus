package com.tntp.mnm.block;

import com.tntp.mnm.model.BlockRenderingHelper;

import net.minecraft.block.material.Material;

public abstract class SBlockContainerModelSpecial extends SBlockContainer {

  protected SBlockContainerModelSpecial(Material mat) {
    super(mat);
  }

  public int getRenderType() {
    return BlockRenderingHelper.id;
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public boolean isOpaqueCube() {
    return false;
  }

}
