package com.tntp.ev.init;

import com.tntp.ev.block.BlockSmartChest;
import com.tntp.ev.tileentity.TileSmartChest;
import com.tntp.ev.util.DebugUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.block.Block;

@ObjectHolder("engineeringvirus")
public class EVBlocks {
  public static final Block blockSmartChest = null;

  public static void loadBlocks() {
    DebugUtil.log.info("Loading Blocks");
    GameRegistry.registerBlock(new BlockSmartChest(), "blockSmartChest");
    GameRegistry.registerTileEntity(TileSmartChest.class, "tileSmartChest");
  }
}
