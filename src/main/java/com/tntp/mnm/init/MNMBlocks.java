package com.tntp.mnm.init;

import com.tntp.mnm.block.BlockAuxiliaryComputingUnit;
import com.tntp.mnm.block.BlockCentralProcessor;
import com.tntp.mnm.block.BlockChimney;
import com.tntp.mnm.block.BlockFirewall;
import com.tntp.mnm.block.BlockGeoThermalSmelter;
import com.tntp.mnm.block.BlockHeatCollectorFirewall;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.block.BlockNetworkMainframe;
import com.tntp.mnm.item.ItemBlockTooltip;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileGeoThermalSmelter;
import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.block.Block;

@ObjectHolder("metalnetworkmainframe")
public class MNMBlocks {
  public static final Block blockFirewall = null;
  public static final Block blockHeatCollectorFirewall = null;
  public static final Block blockHeatPipe = null;
  public static final Block blockChimney = null;
  public static final Block blockGeoThermalSmelter = null;

  public static final Block blockMainframeCasing = null;
  public static final Block blockNetworkMainframeOn = null;
  public static final Block blockNetworkMainframeOff = null;
  public static final Block blockCentralProcessor = null;
  public static final Block blockAuxiliaryComputingUnit = null;

  public static void loadBlocks() {
    DebugUtil.log.info("Loading Blocks");
    GameRegistry.registerBlock(new BlockFirewall(), "blockFirewall");
    GameRegistry.registerBlock(new BlockHeatCollectorFirewall(), "blockHeatCollectorFirewall");
    GameRegistry.registerTileEntity(TileHeatCollectorFirewall.class, "tileHeatCollectorFirewall");
    GameRegistry.registerBlock(new BlockGeoThermalSmelter(), "blockGeoThermalSmelter");
    GameRegistry.registerTileEntity(TileGeoThermalSmelter.class, "tileGeoThermalSmelter");
    GameRegistry.registerBlock(new BlockChimney(), "blockChimney");
    GameRegistry.registerBlock(new BlockHeatPipe(), "blockHeatPipe");

    GameRegistry.registerBlock(new BlockNetworkMainframe(false), "blockNetworkMainframeOff");
    GameRegistry.registerBlock(new BlockNetworkMainframe(true), "blockNetworkMainframeOn");
    GameRegistry.registerBlock(new BlockCentralProcessor(), "blockCentralProcessor");
    GameRegistry.registerTileEntity(TileCentralProcessor.class, "tileCentralProcessor");
    GameRegistry.registerBlock(new BlockAuxiliaryComputingUnit(), "blockAuxiliaryComputingUnit");
    // GameRegistry.registerBlock(new BlockMainframeCasing(),
    // "blockMainframeCasing");
    // GameRegistry.registerBlock(new BlockMainframeCasingFramed(),
    // "blockMainframeCasingFramed");

  }

}
