package com.tntp.mnm.init;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.item.ItemDataGroupChip;
import com.tntp.mnm.item.disk.ItemDisk;
import com.tntp.mnm.item.tools.ItemCommonWrench;
import com.tntp.mnm.item.tools.ItemAccessor;
import com.tntp.mnm.item.tools.ItemDiskKey;
import com.tntp.mnm.item.tools.ItemEraserCard;
import com.tntp.mnm.item.tools.ItemIDCard;
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
  public static final ItemMeterStick meter_stick = null;
  public static final ItemToolUniversal tool_universal = null;
  public static final ItemCommonWrench common_wrench = null;
  public static final ItemScrewDriver screw_driver = null;
  public static final ItemAccessor accessor = null;
  public static final ItemSmallHammer small_hammer = null;
  public static final ItemDataGroupChip data_group_chip = null;

  public static final ItemDisk disk_4mb = null;
  public static final ItemIDCard id_card = null;
  public static final ItemEraserCard eraser_card = null;
  public static final ItemDiskKey disk_key = null;

  public static void loadItems() {
    // GameRegistry.registerItem(new ItemWrench(), "wrench");
    // GameRegistry.registerItem(new ItemToolBag("toolBag", 5), "toolBag");

    regItem(new ItemMeterStick(), "meter_stick");
    regItem(new ItemToolUniversal(), "tool_universal");
    regItem(new ItemCommonWrench(), "common_wrench");
    regItem(new ItemScrewDriver(), "screw_driver");
    regItem(new ItemAccessor(), "accessor");
    regItem(new ItemSmallHammer(), "small_hammer");
    regItem(new ItemIDCard(), "id_card");
    regItem(new ItemEraserCard(), "eraser_card");
    regItem(new ItemDiskKey(), "disk_key");
    regItem(new ItemDataGroupChip(), "data_group_chip");
    regItem(new ItemDisk(4), "disk_4mb");
    regItem(new ItemDisk(64), "disk_64mb");
    regItem(new ItemDisk(256), "disk_256mb");
    regItem(new ItemDisk(1024), "disk_1024mb");
    regItem(new ItemDisk(2048), "disk_2048mb");
  }

  private static void regItem(Item item, String name) {
    item.setTextureName(MNMMod.MODID + ":" + name);
    item.setCreativeTab(MNMCreativeTabs.instance);
    item.setUnlocalizedName(name);
    GameRegistry.registerItem(item, name);
  }
}
