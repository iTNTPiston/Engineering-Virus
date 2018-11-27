package com.tntp.mnm.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHeatDistributor extends SBlockContainer {

  public BlockHeatDistributor(Material mat, String regName) {
    super(Material.iron, "heatDistributor");
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    // TODO Auto-generated method stub
    return null;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
    return this.getIcon(p_149673_5_, p_149673_1_.getBlockMetadata(p_149673_2_, p_149673_3_, p_149673_4_));
  }

}
