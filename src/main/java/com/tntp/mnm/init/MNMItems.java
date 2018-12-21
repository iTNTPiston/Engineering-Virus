package com.tntp.mnm.init;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.item.ItemDataGroupChip;
import com.tntp.mnm.item.ItemToolBag;
import com.tntp.mnm.item.SItemTool;
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

  public static void loadItems() {
    // GameRegistry.registerItem(new ItemWrench(), "wrench");
    // GameRegistry.registerItem(new ItemToolBag("toolBag", 5), "toolBag");

    GameRegistry.registerItem(new ItemEKToolUniversal(), "ek_tool_universal");
    GameRegistry.registerItem(new ItemMeterStick(), "meter_stick");
    GameRegistry.registerItem(new ItemToolUniversal(), "tool_universal");
    GameRegistry.registerItem(new ItemCommonWrench(), "common_wrench");
    GameRegistry.registerItem(new ItemScrewDriver(), "screw_driver");
    GameRegistry.registerItem(new ItemDataReader(), "data_reader");
    GameRegistry.registerItem(new ItemSmallHammer(), "small_hammer");

    GameRegistry.registerItem(new ItemDataGroupChip(), "data_group_chip");
  }

  private static void regItem(Item item, String name) {
    item.setTextureName(MNMMod.MODID + ":" + name);
    item.setCreativeTab(MNMCreativeTabs.instance);
    item.setUnlocalizedName(name);
    GameRegistry.registerItem(item, name);
  }
}
