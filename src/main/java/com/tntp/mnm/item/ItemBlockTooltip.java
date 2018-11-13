package com.tntp.mnm.item;

import java.util.List;

import com.tntp.mnm.block.SBlock;
import com.tntp.mnm.util.LocalUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTooltip extends ItemBlock {

  public ItemBlockTooltip(Block p_i45328_1_) {
    super(p_i45328_1_);
    // TODO Auto-generated constructor stub
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
    String name = stack.getUnlocalizedName() + ".tooltip_";
    SBlock block = (SBlock) this.field_150939_a;
    tooltip.addAll(LocalUtil.localizeList(name, block.getTooltipArgs()));
  }

}
