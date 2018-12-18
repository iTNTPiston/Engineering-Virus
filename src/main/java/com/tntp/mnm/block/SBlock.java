package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.init.MNMCreativeTabs;
import com.tntp.mnm.util.UniversalUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * A template for blocks
 * 
 * @author iTNTPiston
 *
 */
public class SBlock extends Block {

  public SBlock(Material mat) {
    super(mat);
  }

  @SideOnly(Side.CLIENT)
  public Object[] getTooltipArgs() {
    return UniversalUtil.EMPTY_OBJ_ARRAY;
  }

}
