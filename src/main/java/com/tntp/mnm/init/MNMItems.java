package com.tntp.mnm.init;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.item.ItemDataGroupChip;
import com.tntp.mnm.item.ItemToolBag;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.item.disk.ItemDisk;
import com.tntp.mnm.item.tools.ItemCommonWrench;
import com.tntp.mnm.item.tools.ItemDataReader;
import com.tntp.mnm.item.tools.ItemEKToolUniversal;
import com.tntp.mnm.item.tools.ItemMeterStick;
import com.tntp.mnm.item.tools.ItemScrewDriver;
import com.tntp.mnm.item.tools.ItemSmallHammer;
import com.tntp.mnm.item.tools.ItemToolUniversal;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.item.Item;

@ObjectHolder("metalnetworkmainframe")
public class MNMItems {
  // public static final ItemWrench wrench = null;
  // public static final ItemToolBag toolBag = null;
  public static final SItemTool ek_tool_universal = null;
  public static final ItemMeterStick meter_stick = null;
  public static final ItemToolUniversal tool_universal = null;
  public static final ItemCommonWrench common_wrench = null;
  public static final ItemScrewDriver screw_driver = null;
  public static final ItemDataReader data_reader = null;
  public static final ItemSmallHammer small_hammer = null;
  public static final ItemDataGroupChip data_group_chip = null;

  public static final ItemDisk disk = null;

  public static void loadItems() {
    // GameRegistry.registerItem(new ItemWrench(), "wrench");
    // GameRegistry.registerItem(new ItemToolBag("toolBag", 5), "toolBag");

    regItem(new ItemEKToolUniversal(), "ek_tool_universal");
    regItem(new ItemMeterStick(), "meter_stick");
    regItem(new ItemToolUniversal(), "tool_universal");
    regItem(new ItemCommonWrench(), "common_wrench");
    regItem(new ItemScrewDriver(), "screw_driver");
    regItem(new ItemDataReader(), "data_reader");
    regItem(new ItemSmallHammer(), "small_hammer");
    regItem(new ItemDataGroupChip(), "data_group_chip");
    regItem(new ItemDisk(4), "disk");
  }

  private static void regItem(Item item, String name) {
    item.setTextureName(MNMMod.MODID + ":" + name);
    item.setCreativeTab(MNMCreativeTabs.instance);
    item.setUnlocalizedName(name);
    GameRegistry.registerItem(item, name);
  }
}
