package com.tntp.mnm.init;

import com.tntp.mnm.block.BlockMainframeCasing;
import com.tntp.mnm.block.BlockMainframeCasingFramed;
import com.tntp.mnm.block.BlockSmartChest;
import com.tntp.mnm.tileentity.TileSmartChest;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.block.Block;

@ObjectHolder("engineeringvirus")
public class EVBlocks {
  public static final Block blockMainframeCasing = null;

  public static void loadBlocks() {
    DebugUtil.log.info("Loading Blocks");
    GameRegistry.registerBlock(new BlockMainframeCasing(), "blockMainframeCasing");
    GameRegistry.registerBlock(new BlockMainframeCasingFramed(), "blockMainframeCasingFramed");

    // GameRegistry.registerBlock(new BlockSmartChest(), "blockSmartChest");
    // GameRegistry.registerTileEntity(TileSmartChest.class, "tileSmartChest");
  }

}
