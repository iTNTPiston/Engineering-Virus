package com.tntp.mnm.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class MotherboardComponentRenderer extends WaveObjRenderer {
  private WaveObjRenderer componentRender;

  public MotherboardComponentRenderer(WavefrontObject board, WavefrontObject component, ResourceLocation texture) {
    super(board, texture);
    componentRender = new WaveObjRenderer(component, texture);
  }

  public void render() {
    super.render();
    componentRender.render();
  }

  public void tessellate(Tessellator tes, IIcon icon) {
    super.tessellate(tes, icon);
    componentRender.tessellate(tes, icon);
  }

}
