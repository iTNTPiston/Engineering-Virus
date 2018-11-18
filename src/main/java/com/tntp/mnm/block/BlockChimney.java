package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockChimney extends SBlock {

  private IIcon top;

  public BlockChimney() {
    super(Material.rock, "blockChimney");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    this.blockIcon = reg.registerIcon(MNMMod.MODID + ":blockFirewall");
    top = reg.registerIcon(tex);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    if (side == 0 || side == 1)
      return top;
    else
      return blockIcon;
  }
}
