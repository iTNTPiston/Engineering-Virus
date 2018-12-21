package com.tntp.mnm.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class SItemSpecialModel extends SItem {

  public SItemSpecialModel() {
    super();
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister reg) {
    this.itemIcon = reg.registerIcon("minecraft:apple");
  }

}
