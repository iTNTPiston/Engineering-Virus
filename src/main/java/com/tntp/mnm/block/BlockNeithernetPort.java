package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileNeithernetPort;
import com.tntp.mnm.util.BlockUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNeithernetPort extends SBlockModelSpecial implements ITileEntityProvider {

  private IIcon off;

  public BlockNeithernetPort() {
    super(Material.iron);

  }

  @Override
  public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
    return new TileNeithernetPort();
  }

  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    super.onBlockPlacedBy(world, x, y, z, entity, stack);
    byte meta = BlockUtil.getRotationalMetaFromEntityPlacing(world, x, y, z, entity, stack);
    world.setBlockMetadataWithNotify(x, y, z, meta, 2);
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    super.onBlockAdded(world, x, y, z);
    if (!world.isRemote) {
      byte meta = BlockUtil.getRotationalMetaFromBlocks(world, x, y, z);
      world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    this.blockIcon = reg.registerIcon(tex);
    off = reg.registerIcon(tex + "_off");
  }

//  @Override
//  @SideOnly(Side.CLIENT)
//  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
//    TileEntity tile = world.getTileEntity(x, y, z);
//    if (tile instanceof TileNeithernetPort) {
//      if (((TileNeithernetPort) tile).isConnectedToMainframe()) {
//        return blockIcon;
//      }
//    }
//    return off;
//  }

}
