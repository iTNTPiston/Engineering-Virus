package com.tntp.mnm.item.tools;

import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.item.ItemMNMTool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemEKToolUniversal extends ItemMNMTool {

  public ItemEKToolUniversal() {
    super("itemEKToolUniversal", GuiTabType.HEAT, GuiTabType.HEAT_PIPE);
  }

}
