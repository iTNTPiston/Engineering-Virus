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

  public void tessellateExtensionFor(RenderBlocks renderer, Tessellator tes, Block block, int x, int y, int z,
      IIcon icon, int meta, int... sides) {
    for (int s : sides) {
      extRender.setRotationFor(s);
      extRender.tessellate(renderer, tes, block, x, y, z, icon, meta);
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
    int meta = world.getBlockMetadata(x, y, z);
    IIcon icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture
        : getBlockIcon(world, x, y, z, block);

    int[] sides = getSideCodeFromBlock(block, world, x, y, z);
    tessellate(renderer, tes, block, x, y, z, icon, meta);
    tessellateExtensionFor(renderer, tes, block, x, y, z, icon, meta, sides);
    tes.addTranslation(-x - 0.5f, -y - 0.5f, -z - 0.5f);
    return true;
  }

  public int[] getSideCodeFromBlock(Block block, IBlockAccess world, int x, int y, int z) {
    if (block == MNMBlocks.heat_pipe) {
      int s = BlockUtil.pipeMetaToSide(world.getBlockMetadata(x, y, z));
      if (s != 0)
        return new int[] { s >> 4, s & 15 };
    }
    return UniversalUtil.EMPTY_INT_ARRAY;
  }

}
