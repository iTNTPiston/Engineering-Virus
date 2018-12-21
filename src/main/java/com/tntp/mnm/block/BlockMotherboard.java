package com.tntp.mnm.block;

import com.tntp.mnm.tileentity.TileHeatDistributor;
import com.tntp.mnm.util.BlockUtil;
import com.tntp.mnm.util.RandomUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMotherboard extends SBlockModelSpecial {
  private IIcon[] icons;

  public BlockMotherboard() {
    super(Material.iron);
    this.setBlockBounds(0, 0, 0, 1, 1 / 8f, 1);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    icons = new IIcon[16];
    for (int i = 0; i < 16; i++)
      icons[i] = reg.registerIcon(tex + "_" + i);
    this.blockIcon = reg.registerIcon(tex);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    return icons[meta];
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    super.onBlockAdded(world, x, y, z);
    if (!world.isRemote) {
      world.setBlockMetadataWithNotify(x, y, z, RandomUtil.RAND.nextInt(16), 2);
    }
  }

}
