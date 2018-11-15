package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHeatCollectorFirewall extends SBlock implements ITileEntityProvider {

  public BlockHeatCollectorFirewall() {
    super(Material.rock, "blockHeatCollectorFirewall");
  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileHeatCollectorFirewall();
  }

}
