package com.tntp.mnm.init;

import com.tntp.mnm.model.SimpleObjRenderer;
import com.tntp.mnm.model.WaveObjRenderer;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class MNMRender {
  public static void loadRenderers() {
    SimpleObjRenderer simple = new SimpleObjRenderer();
    WaveObjRenderer cp = getWaveObjRenderer("MNM_CP");
    int i = simple.registerWaveObj(cp);
    simple.bindWaveObj(MNMBlocks.blockCentralProcessor, 0, i);
  }

  public static WaveObjRenderer getWaveObjRenderer(String name) {
    return new WaveObjRenderer(getWaveObj(name), getTexture(name));
  }

  public static WavefrontObject getWaveObj(String name) {
    return new WavefrontObject(MNMResources.getResource("models/" + name + ".obj"));
  }

  public static ResourceLocation getTexture(String name) {
    return MNMResources.getResource("models/" + name + ".png");
  }
}
