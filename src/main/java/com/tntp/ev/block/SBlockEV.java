package com.tntp.ev.block;

import com.tntp.ev.init.EVCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * A template for blocks
 * 
 * @author iTNTPiston
 *
 */
public class SBlockEV extends Block {

  protected SBlockEV(Material mat, String regName) {
    super(mat);
    this.setBlockName(regName);
    this.setCreativeTab(EVCreativeTabs.instance);
  }

}
