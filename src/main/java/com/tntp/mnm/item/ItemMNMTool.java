package com.tntp.mnm.item;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.gui.GuiMain;
import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.init.MNMGuis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMNMTool extends SItemSpecialModel {
  // meterstick (heat pipe)
  // common wrench (machine progress, machine structure)
  // common screw driver(machine config)
  // heat resistive wrench(heat, heat pipe) adjust heat pipe
  // heat resistive screw driver(heat distributor)
  // thermometer (heat)
  private int guiCapabilities;

  public ItemMNMTool(String regName, GuiTabType... guiCap) {
    super(regName);
    this.setMaxStackSize(1);
    guiCapabilities = 0;
    for (GuiTabType gc : guiCap) {
      guiCapabilities += gc.flag();
    }
  }

  public void setGuiTabsWithCapability(ItemStack stack, World world, int x, int y, int z, GuiMain gui, int cap) {
    int i = 0;
    for (GuiTabType tab : GuiTabType.values()) {
      if (i >= 8)
        break;
      int flag = tab.flag();
      if ((cap & flag) == flag) {
        String id = tab.getGuiString(stack, world, x, y, z);
        if (id != null)
          gui.setTabAt(i++, tab, id);
      }
    }
  }

  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    String defaultGuiId = getDefaultGuiId(world, x, y, z, stack);
    if (defaultGuiId == null)
      return false;
    if (!world.isRemote) {
      player.openGui(MNMMod.MODID, MNMGuis.getGuiID(defaultGuiId), world, x, y, z);
    }
    return true;
  }

  public int getGuiCapability() {
    return guiCapabilities;
  }

  public String getDefaultGuiId(World world, int x, int y, int z, ItemStack stack) {
    for (GuiTabType tab : GuiTabType.values()) {
      int flag = tab.flag();
      if ((getGuiCapability() & flag) == flag) {
        String id = tab.getGuiString(stack, world, x, y, z);
        if (id != null)
          return id;
      }
    }
    return null;
  }

}
