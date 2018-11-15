package com.tntp.mnm.block;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.init.MNMGuis;
import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
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

  // @Override
  // public boolean onBlockActivated(World world, int x, int y, int z,
  // EntityPlayer player, int side, float xx, float yy,
  // float zz) {
  // if (!world.isRemote) {
  // player.openGui(MNMMod.MODID, MNMGuis.getGuiID("GuiHeat"), world, x, y, z);
  // }
  // return false;
  // }

}
