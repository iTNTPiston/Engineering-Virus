package com.tntp.mnm.init;

import com.tntp.mnm.block.BlockCentralProcessor;
import com.tntp.mnm.block.BlockMainframeCasing;
import com.tntp.mnm.block.BlockMainframeCasingFramed;
import com.tntp.mnm.block.BlockNetworkMainframe;
import com.tntp.mnm.block.BlockSmartChest;
import com.tntp.mnm.tileentity.TileSmartChest;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.block.Block;

@ObjectHolder("metalnetworkmainframe")
public class MNMBlocks {
  public static final Block blockMainframeCasing = null;
  public static final Block blockNetworkMainframeOn = null;
  public static final Block blockNetworkMainframeOff = null;
  public static final Block blockCentralProcessor = null;

  public static void loadBlocks() {
    DebugUtil.log.info("Loading Blocks");
    GameRegistry.registerBlock(new BlockNetworkMainframe(false), "blockNetworkMainframeOff");
    GameRegistry.registerBlock(new BlockNetworkMainframe(true), "blockNetworkMainframeOn");
    GameRegistry.registerBlock(new BlockCentralProcessor(), "blockCentralProcessor");
    // GameRegistry.registerBlock(new BlockMainframeCasing(),
    // "blockMainframeCasing");
    // GameRegistry.registerBlock(new BlockMainframeCasingFramed(),
    // "blockMainframeCasingFramed");

    // GameRegistry.registerBlock(new BlockSmartChest(), "blockSmartChest");
    // GameRegistry.registerTileEntity(TileSmartChest.class, "tileSmartChest");
  }

}
