package com.tntp.mnm.init;

import com.tntp.mnm.model.ItemRenderingHelper;
import com.tntp.mnm.model.MotherboardComponentRenderer;
import com.tntp.mnm.model.NeithernetCableRenderer;
import com.tntp.mnm.model.PipeRenderer;
import com.tntp.mnm.model.BlockRenderingHelper;
import com.tntp.mnm.model.WaveObjRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class MNMRender {
  public static void loadRenderers() {
    BlockRenderingHelper.id = RenderingRegistry.getNextAvailableRenderId();

    BlockRenderingHelper simple = new BlockRenderingHelper();
    int i;
    // i = simple.registerWaveObj(getWaveObjRenderer("MNM_ACU"));
    // simple.bindWaveObj(MNMBlocks.auxiliaryComputingUnit, 0, i);

    WavefrontObject heatPipe = getWaveObj("heat_pipe/main");
    WavefrontObject heatPipeExt = getWaveObj("heat_pipe/ext");
    ResourceLocation heatPipeTex = getBlockTexture("heat_pipe");
    PipeRenderer heatPipeRender = new PipeRenderer(heatPipe, heatPipeExt, heatPipeTex);
    i = simple.registerWaveObj(heatPipeRender);
    simple.bindWaveObj(MNMBlocks.heat_pipe, 0, i);

    WavefrontObject neitherCableCover = getWaveObj("neithernet_cable/cover");
    WavefrontObject neitherCableExt = getWaveObj("neithernet_cable/ext");
    WavefrontObject neitherCablePlug = getWaveObj("neithernet_cable/plug");
    ResourceLocation neitherCableTex = getBlockTexture("neithernet_cable");
    NeithernetCableRenderer neitherCableRender = new NeithernetCableRenderer(neitherCableCover, neitherCableExt,
        neitherCablePlug, neitherCableTex);
    i = simple.registerWaveObj(neitherCableRender);
    simple.bindWaveObj(MNMBlocks.neithernet_cable, 0, i);

    WavefrontObject motherboard = getWaveObj("mother_board/0");
    ResourceLocation motherboardTex = getBlockTexture("mother_board");
    WaveObjRenderer motherboardRender = new WaveObjRenderer(motherboard, motherboardTex);
    i = simple.registerWaveObj(motherboardRender);
    simple.bindWaveObj(MNMBlocks.mother_board, 0, i);

    WavefrontObject cpu = getWaveObj("central_processor/0");
    ResourceLocation cpuTex = getBlockTexture("central_processor");
    WaveObjRenderer cpuRender = new MotherboardComponentRenderer(motherboard, cpu, cpuTex);
    i = simple.registerWaveObj(cpuRender);
    simple.bindWaveObj(MNMBlocks.central_processor, 0, i);

    RenderingRegistry.registerBlockHandler(simple);

    ItemRenderingHelper itemRender = ItemRenderingHelper.instance;
    i = itemRender.registerWaveObj(getWaveObjRenderer("meter_stick/0"));
    itemRender.bindWaveObj(MNMItems.meter_stick, i);

    i = itemRender.registerWaveObj(getWaveObjRenderer("common_wrench/0"));
    itemRender.bindWaveObj(MNMItems.common_wrench, i);

    i = itemRender.registerWaveObj(getWaveObjRenderer("screw_driver/0"));
    itemRender.bindWaveObj(MNMItems.screw_driver, i);

    i = itemRender.registerWaveObj(getWaveObjRenderer("small_hammer/0"));
    itemRender.bindWaveObj(MNMItems.small_hammer, i);

  }

  public static WaveObjRenderer getWaveObjRenderer(String name) {
    return new WaveObjRenderer(getWaveObj(name), getTexture(name));
  }

  public static WavefrontObject getWaveObj(String name) {
    WavefrontObject obj = new WavefrontObject(MNMResources.getResource("models/" + name + ".obj"));
    for (Vertex v : obj.vertices) {
      v.x = v.x / 16f;
      v.y = v.y / 16f;
      v.z = v.z / 16f;
    }
    return obj;
  }

  public static ResourceLocation getTexture(String name) {
    return MNMResources.getResource("models/" + name + ".png");
  }

  public static ResourceLocation getBlockTexture(String name) {
    return MNMResources.getResource("textures/blocks/" + name + ".png");
  }
}
