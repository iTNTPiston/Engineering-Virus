package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileSmartChest;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSmartChest extends BlockContainer {

  public BlockSmartChest() {
    super(Material.iron);
    this.setBlockName("blockSmartChest");
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileSmartChest(18);
  }

}
