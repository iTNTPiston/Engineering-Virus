package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.BlockUtil;
import com.tntp.mnm.util.UniversalUtil;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeRenderer extends WaveObjRenderer {
  private WaveObjRenderer extRender;

  public PipeRenderer(WavefrontObject obj, WavefrontObject ext, ResourceLocation texture) {
    super(obj, texture);
    extRender = new WaveObjRenderer(ext, texture);
  }

  public void renderExtensionFor(int... sides) {
    bindTexture();
    for (int s : sides) {
      GL11.glPushMatrix();
      rotateGLFor(s);
      extRender.render();
      GL11.glPopMatrix();
    }
  }

  public void tessellateExtensionFor(Tessellator tes, IIcon icon, int... sides) {
    for (int s : sides) {
      extRender.setRotationFor(s);
      extRender.tessellate(tes, icon);
    }
    extRender.clearRotation();
  }

  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    render();
    renderExtensionFor(2, 3);
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
      RenderBlocks renderer) {
    Tessellator tes = Tessellator.instance;
    tes.addTranslation(x + 0.5f, y + 0.5f, z + 0.5f);
    tes.setColorOpaque_F(1, 1, 1);
    tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
    IIcon icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : block.getIcon(0, 0);

    int[] sides = getSideCodeFromBlock(block, world, x, y, z);
    tessellate(tes, icon);
    tessellateExtensionFor(tes, icon, sides);
    tes.addTranslation(-x - 0.5f, -y - 0.5f, -z - 0.5f);
    return true;
  }

  public int[] getSideCodeFromBlock(Block block, IBlockAccess world, int x, int y, int z) {
    if (block == MNMBlocks.heatPipe) {
      int s = BlockUtil.pipeMetaToSide(world.getBlockMetadata(x, y, z));
      if (s != 0)
        return new int[] { s >> 4, s & 15 };
    }
    return UniversalUtil.EMPTY_INT_ARRAY;
  }

}
