package com.tntp.ev.block;

import com.tntp.ev.tileentity.TileSmartChest;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSmartChest extends BlockContainer {

  public BlockSmartChest() {
    super(Material.iron);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileSmartChest(18);
  }

}
