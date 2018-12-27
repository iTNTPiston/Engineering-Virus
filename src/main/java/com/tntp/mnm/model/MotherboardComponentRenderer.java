package com.tntp.mnm.model;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
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

  @Override
  public void render() {
    super.render();
    componentRender.render();
  }

  @Override
  public void tessellate(RenderBlocks renderer, Tessellator tes, Block block, int x, int y, int z, IIcon icon,
      int meta) {
    super.tessellate(renderer, tes, block, x, y, z, icon, meta);
    componentRender.tessellate(renderer, tes, block, x, y, z, icon, meta);
  }

  @Override
  public void enableMetaRotation() {
    super.enableMetaRotation();
    componentRender.enableMetaRotation();
  }

}
