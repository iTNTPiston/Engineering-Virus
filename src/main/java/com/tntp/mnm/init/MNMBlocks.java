package com.tntp.mnm.init;

import com.tntp.mnm.block.BlockBasicDiskStorage;
import com.tntp.mnm.block.BlockBasicGroupMappingStorage;
import com.tntp.mnm.block.BlockCentralProcessor;
import com.tntp.mnm.block.BlockChimney;
import com.tntp.mnm.block.BlockDataDefinitionStorage;
import com.tntp.mnm.block.BlockDataDefinitionTerminal;
import com.tntp.mnm.block.BlockDataGroupChipset;
import com.tntp.mnm.block.BlockDataGroupDefiner;
import com.tntp.mnm.block.BlockDataIntegrityChipset;
import com.tntp.mnm.block.BlockFirewall;
import com.tntp.mnm.block.BlockGeoThermalSmelter;
import com.tntp.mnm.block.BlockGroupMapper;
import com.tntp.mnm.block.BlockHeatCollectorFirewall;
import com.tntp.mnm.block.BlockHeatDistributor;
import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.block.BlockMainframeRecoveryChipset;
import com.tntp.mnm.block.BlockMotherboard;
import com.tntp.mnm.block.BlockNeithernetCable;
import com.tntp.mnm.block.BlockNeithernetPort;
import com.tntp.mnm.block.BlockQueryBuilder;
import com.tntp.mnm.block.BlockSecurityEncoder;
import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.tileentity.TileBasicDiskStorage;
import com.tntp.mnm.tileentity.TileBasicGroupMappingStorage;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileDataDefinitionStorage;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;
import com.tntp.mnm.tileentity.TileDataGroupChipset;
import com.tntp.mnm.tileentity.TileDataGroupDefiner;
import com.tntp.mnm.tileentity.TileDataIntegrityChipset;
import com.tntp.mnm.tileentity.TileGeoThermalSmelter;
import com.tntp.mnm.tileentity.TileGroupMapper;
import com.tntp.mnm.tileentity.TileHeatCollectorFirewall;
import com.tntp.mnm.tileentity.TileHeatDistributor;
import com.tntp.mnm.tileentity.TileMainframeRecoveryChipset;
import com.tntp.mnm.tileentity.TileNeithernetPort;
import com.tntp.mnm.tileentity.TileQueryBuilder;
import com.tntp.mnm.tileentity.TileSecurityEncoder;
import com.tntp.mnm.util.DebugUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

@ObjectHolder("metalnetworkmainframe")
public class MNMBlocks {
  public static final Block firewall = null;
  public static final Block heat_collector_firewall = null;
  public static final BlockHeatPipe heat_pipe = null;
  public static final Block chimney = null;
  public static final Block geo_thermal_smelter = null;
  public static final Block heat_distributor = null;

  public static final BlockNeithernetCable neithernet_cable = null;
  public static final Block neithernet_port = null;
  public static final Block mother_board = null;
  public static final Block central_processor = null;
  public static final Block data_group_definer = null;
  public static final Block security_encoder = null;
  public static final Block data_group_chipset = null;
  public static final Block data_integrity_chipset = null;
  public static final Block mainframe_recovery_chipset = null;

  public static final Block query_builder = null;

  public static void loadBlocks() {
    DebugUtil.log.info("Loading Blocks");
    regBlock(new BlockFirewall(), "firewall");
    regBlock(new BlockHeatCollectorFirewall(), "heat_collector_firewall");
    regTileEntity(TileHeatCollectorFirewall.class);
    regBlock(new BlockGeoThermalSmelter(), "geo_thermal_smelter");
    regTileEntity(TileGeoThermalSmelter.class);
    regBlock(new BlockChimney(), "chimney");
    regBlock(new BlockHeatPipe(), "heat_pipe");
    regBlock(new BlockHeatDistributor(), "heat_distributor");
    regTileEntity(TileHeatDistributor.class);

    regBlock(new BlockMotherboard(), "mother_board");
    regBlock(new BlockCentralProcessor(), "central_processor");
    regTileEntity(TileCentralProcessor.class);
    regBlock(new BlockDataGroupDefiner(), "data_group_definer");
    regTileEntity(TileDataGroupDefiner.class);
    regBlock(new BlockSecurityEncoder(), "security_encoder");
    regTileEntity(TileSecurityEncoder.class);
    regBlock(new BlockDataGroupChipset(), "data_group_chipset");
    regTileEntity(TileDataGroupChipset.class);
    regBlock(new BlockDataIntegrityChipset(), "data_integrity_chipset");
    regTileEntity(TileDataIntegrityChipset.class);
    regBlock(new BlockMainframeRecoveryChipset(), "mainframe_recovery_chipset");
    regTileEntity(TileMainframeRecoveryChipset.class);

    regBlock(new BlockNeithernetCable(), "neithernet_cable");
    regBlock(new BlockNeithernetPort(), "neithernet_port");
    regTileEntity(TileNeithernetPort.class);
    regBlock(new BlockDataDefinitionTerminal(), "data_definition_terminal");
    regTileEntity(TileDataDefinitionTerminal.class);
    regBlock(new BlockQueryBuilder(), "query_builder");
    regTileEntity(TileQueryBuilder.class);
    regBlock(new BlockBasicDiskStorage(), "basic_disk_storage");
    regTileEntity(TileBasicDiskStorage.class);
    regBlock(new BlockDataDefinitionStorage(), "data_definition_storage");
    regTileEntity(TileDataDefinitionStorage.class);
    regBlock(new BlockBasicGroupMappingStorage(), "basic_group_mapping_storage");
    regTileEntity(TileBasicGroupMappingStorage.class);
    regBlock(new BlockGroupMapper(), "group_mapper");
    regTileEntity(TileGroupMapper.class);

    // GameRegistry.registerBlock(new BlockNetworkMainframe(false),
    // "networkMainframeOff");
    // GameRegistry.registerBlock(new BlockNetworkMainframe(true),
    // "networkMainframeOn");
    // GameRegistry.registerBlock(new BlockCentralProcessor(), "centralProcessor");
    // GameRegistry.registerTileEntity(TileCentralProcessor.class,
    // "tileCentralProcessor");
    // GameRegistry.registerBlock(new BlockAuxiliaryComputingUnit(),
    // "auxiliaryComputingUnit");
    // GameRegistry.registerBlock(new BlockMainframeCasing(),
    // "blockMainframeCasing");
    // GameRegistry.registerBlock(new BlockMainframeCasingFramed(),
    // "blockMainframeCasingFramed");
  }

  private static void regBlock(Block b, String name) {
    b.setBlockName(name);
    b.setBlockTextureName(MNMMod.MODID + ":" + name);
    b.setCreativeTab(MNMCreativeTabs.instance);
    GameRegistry.registerBlock(b, name);
  }

  private static void regTileEntity(Class<? extends TileEntity> clazz) {
    String name = clazz.getSimpleName().replaceFirst("Tile", "tile");
    GameRegistry.registerTileEntity(clazz, name);
  }

}
