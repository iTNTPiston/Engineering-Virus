package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.init.EVCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * A template for blocks
 * 
 * @author iTNTPiston
 *
 */
public class SBlock extends Block {

  public SBlock(Material mat, String regName) {
    super(mat);
    this.setBlockName(regName);
    this.setCreativeTab(EVCreativeTabs.instance);
    this.setBlockTextureName(MNMMod.MODID + ":" + regName);
  }

}
