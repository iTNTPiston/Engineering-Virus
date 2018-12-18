package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.tileentity.TileHeatDistributor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHeatDistributor extends SBlockContainer {

  private IIcon[] icons;

  public BlockHeatDistributor() {
    super(Material.iron);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    icons = new IIcon[6];
    for (int i = 0; i < 6; i++)
      icons[i] = reg.registerIcon(tex + "_" + i);
    this.blockIcon = icons[0];
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileHeatDistributor();
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    return icons[side];
  }

}
