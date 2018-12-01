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

  private IIcon out;

  public BlockHeatDistributor(Material mat, String regName) {
    super(Material.iron, "heatDistributor");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    this.blockIcon = reg.registerIcon(tex);
    out = reg.registerIcon(tex + "_out");

  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileHeatDistributor();
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    TileEntity t = world.getTileEntity(x, y, z);
    if (t instanceof TileHeatDistributor) {
      if (((TileHeatDistributor) t).isSourceSide(side)) {
        return out;
      }
    }
    return blockIcon;
  }

}
