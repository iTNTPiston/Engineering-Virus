package com.tntp.mnm.init;

import com.tntp.mnm.block.BlockAuxiliaryComputingUnit;
import com.tntp.mnm.block.BlockCentralProcessor;
import com.tntp.mnm.block.BlockChimney;
import com.tntp.mnm.block.BlockFirewall;
import com.tntp.mnm.block.BlockGeoThermalSmelter;
import com.tntp.mnm.block.BlockHeatCollectorFirewall;
import com.tntp.mnm.block.BlockHeatDistributor;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.block.BlockNeithernetCable;
import com.tntp.mnm.block.BlockNeithernetPort;
import com.tntp.mnm.block.BlockNetworkMainframe;
import com.tntp.mnm.block.BlockSecurityEncoder;
import com.tntp.mnm.item.ItemBlockTooltip;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileGeoThermalSmelter;
import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;
import com.tntp.mnm.tileentity.TileHeatDistributor;
import com.tntp.mnm.tileentity.TileNeithernetPort;
import com.tntp.mnm.tileentity.TileSecurityEncoder;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.block.Block;

@ObjectHolder("metalnetworkmainframe")
public class MNMBlocks {
  public static final Block firewall = null;
  public static final Block heatCollectorFirewall = null;
  public static final BlockHeatPipe heatPipe = null;
  public static final Block chimney = null;
  public static final Block geoThermalSmelter = null;
  public static final Block heatDistributor = null;

  public static final BlockNeithernetCable neithernetCable = null;

  public static final Block mainframeCasing = null;
  public static final Block networkMainframeOn = null;
  public static final Block networkMainframeOff = null;
  public static final Block centralProcessor = null;
  public static final Block auxiliaryComputingUnit = null;

  public static void loadBlocks() {
    DebugUtil.log.info("Loading Blocks");
    GameRegistry.registerBlock(new BlockFirewall(), "firewall");
    GameRegistry.registerBlock(new BlockHeatCollectorFirewall(), "heatCollectorFirewall");
    GameRegistry.registerTileEntity(TileHeatCollectorFirewall.class, "tileHeatCollectorFirewall");
    GameRegistry.registerBlock(new BlockGeoThermalSmelter(), "geoThermalSmelter");
    GameRegistry.registerTileEntity(TileGeoThermalSmelter.class, "tileGeoThermalSmelter");
    GameRegistry.registerBlock(new BlockChimney(), "chimney");
    GameRegistry.registerBlock(new BlockHeatPipe(), "heatPipe");
    GameRegistry.registerBlock(new BlockHeatDistributor(), "heatDistributor");
    GameRegistry.registerTileEntity(TileHeatDistributor.class, "tileHeatDistributor");

    GameRegistry.registerBlock(new BlockNeithernetCable(), "neithernetCable");
    GameRegistry.registerBlock(new BlockNeithernetPort(), "neithernetPort");
    GameRegistry.registerTileEntity(TileNeithernetPort.class, "tileNeithernetPort");

    GameRegistry.registerBlock(new BlockSecurityEncoder(), "securityEncoder");
    GameRegistry.registerTileEntity(TileSecurityEncoder.class, "tileSecurityEncoder");

    GameRegistry.registerBlock(new BlockNetworkMainframe(false), "networkMainframeOff");
    GameRegistry.registerBlock(new BlockNetworkMainframe(true), "networkMainframeOn");
    GameRegistry.registerBlock(new BlockCentralProcessor(), "centralProcessor");
    GameRegistry.registerTileEntity(TileCentralProcessor.class, "tileCentralProcessor");
    GameRegistry.registerBlock(new BlockAuxiliaryComputingUnit(), "auxiliaryComputingUnit");
    // GameRegistry.registerBlock(new BlockMainframeCasing(),
    // "blockMainframeCasing");
    // GameRegistry.registerBlock(new BlockMainframeCasingFramed(),
    // "blockMainframeCasingFramed");

  }

}
