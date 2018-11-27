package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.block.BlockHeatPipe;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.UniversalUtil;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeRenderer extends WaveObjRenderer {
  private WavefrontObject extension;

  public PipeRenderer(WavefrontObject obj, WavefrontObject ext, ResourceLocation texture) {
    super(obj, texture);
    extension = ext;
  }

  public void renderExtension() {
    extension.renderAll();
  }

  public void tessellateExtension(Tessellator tes, IIcon icon) {
    tes(tes, icon, extension);
  }

  public void renderExtensionFor(int... sides) {
    bindTexture();
    for (int s : sides) {
      GL11.glPushMatrix();
      rotateGLFor(s);
      renderExtension();
      GL11.glPopMatrix();
    }
  }

  public void tessellateExtensionFor(Tessellator tes, IIcon icon, int... sides) {
    for (int s : sides) {
      setRotationFor(s);
      tessellateExtension(tes, icon);
    }
    clearRotation();
  }

  private void setRotationFor(int side) {
    switch (side) {
    case 1:
      setRotation((float) Math.PI, ForgeDirection.EAST);
      break;
    case 2:
      setRotation((float) Math.PI / 2, ForgeDirection.EAST);
      break;
    case 3:
      setRotation((float) -Math.PI / 2, ForgeDirection.EAST);
      break;
    case 4:
      setRotation((float) -Math.PI / 2, ForgeDirection.SOUTH);
      break;
    case 5:
      setRotation((float) Math.PI / 2, ForgeDirection.SOUTH);
      break;

    }
  }

  private void rotateGLFor(int side) {
    switch (side) {
    case 1:// up,x-axis 180
      GL11.glRotatef(180, 1, 0, 0);
      break;
    case 2:// -z
      GL11.glRotatef(90, 1, 0, 0);
      break;
    case 3:// +z
      GL11.glRotatef(90, -1, 0, 0);
      break;
    case 4:// -x
      GL11.glRotatef(90, 0, 0, 1);
      break;
    case 5:// +x
      GL11.glRotatef(90, 0, 0, -1);
      break;
    }
  }

  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    // GL11.glEnable(GL11.GL_BLEND);
    // OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1,
    // 0);
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
      int s = BlockHeatPipe.metaToSide(world.getBlockMetadata(x, y, z));
      if (s != 0)
        return new int[] { s >> 4, s & 15 };
    }
    return UniversalUtil.EMPTY_INT_ARRAY;
  }

}
