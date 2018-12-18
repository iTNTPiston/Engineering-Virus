package com.tntp.mnm.init;

import com.tntp.mnm.model.ItemRenderingHelper;
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

    int i = simple.registerWaveObj(getWaveObjRenderer("MNM_CP"));
    // simple.bindWaveObj(MNMBlocks.centralProcessor, 0, i);

    // i = simple.registerWaveObj(getWaveObjRenderer("MNM_ACU"));
    // simple.bindWaveObj(MNMBlocks.auxiliaryComputingUnit, 0, i);

    WavefrontObject heatPipe = getWaveObj("MNM_HP");
    WavefrontObject heatPipeExt = getWaveObj("MNM_HP_EXT");
    ResourceLocation heatPipeTex = getTexture("MNM_HP");
    PipeRenderer heatPipeRender = new PipeRenderer(heatPipe, heatPipeExt, heatPipeTex);
    i = simple.registerWaveObj(heatPipeRender);
    simple.bindWaveObj(MNMBlocks.heat_pipe, 0, i);

    WavefrontObject neitherCableCover = getWaveObj("MNM_NN_COV");
    WavefrontObject neitherCableExt = getWaveObj("MNM_NN_EXT");
    WavefrontObject neitherCablePlug = getWaveObj("MNM_NN_HEAD");
    ResourceLocation neitherCableTex = getTexture("MNM_NN");
    NeithernetCableRenderer neitherCableRender = new NeithernetCableRenderer(neitherCableCover, neitherCableExt,
        neitherCablePlug, neitherCableTex);
    i = simple.registerWaveObj(neitherCableRender);
    simple.bindWaveObj(MNMBlocks.neithernet_cable, 0, i);

    RenderingRegistry.registerBlockHandler(simple);

    ItemRenderingHelper itemRender = ItemRenderingHelper.instance;
    i = itemRender.registerWaveObj(getWaveObjRenderer("MNM_MS"));
    itemRender.bindWaveObj(MNMItems.meterStick, i);

    i = itemRender.registerWaveObj(getWaveObjRenderer("MNM_CW"));
    itemRender.bindWaveObj(MNMItems.commonWrench, i);

    i = itemRender.registerWaveObj(getWaveObjRenderer("MNM_SD"));
    itemRender.bindWaveObj(MNMItems.screwDriver, i);

    i = itemRender.registerWaveObj(getWaveObjRenderer("MNM_SH"));
    itemRender.bindWaveObj(MNMItems.smallHammer, i);

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
