package com.tntp.mnm.block;

import java.util.Random;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.init.MNMBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockNetworkMainframe extends SBlock {
  @SideOnly(Side.CLIENT)
  private IIcon iconTick;
  @SideOnly(Side.CLIENT)
  private IIcon iconDead;

  private boolean isOn;

  public BlockNetworkMainframe(boolean on) {
    super(Material.iron, "networkMainframe" + (on ? "On" : "Off"));
    this.setBlockTextureName(MNMMod.MODID + ":networkMainframe");
    isOn = on;
    if (isOn) {
      this.setTickRandomly(true);
      this.setLightLevel(1);
    }
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    switch (meta) {
    case 0:
      return blockIcon;
    case 1:
      return iconDead;
    default:
      return iconTick;
    }
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister reg) {
    String tex = this.getTextureName();
    this.blockIcon = reg.registerIcon(tex + (isOn ? "_on" : "_off"));
    this.iconTick = reg.registerIcon(tex + "_tick");
    this.iconDead = reg.registerIcon(tex + "_dead");
  }

  public void updateTick(World world, int x, int y, int z, Random rand) {
    int meta = world.getBlockMetadata(x, y, z);
    if (meta == 0) {
      meta = rand.nextInt(14) + 2;
    } else if (meta != 1) {
      meta = 0;
    }
    world.setBlockMetadataWithNotify(x, y, z, meta, 3);
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return Item.getItemFromBlock(MNMBlocks.networkMainframeOff);
  }

}
