package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.util.DirUtil;
import com.tntp.mnm.util.RenderUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class WaveObjRenderer {
  protected WavefrontObject obj;
  protected ResourceLocation texture;

  protected float rotation;
  protected int rotationAxis;
  private boolean metaRotation;

  public WaveObjRenderer(WavefrontObject obj, ResourceLocation texture) {
    this.obj = obj;
    this.texture = texture;
    rotationAxis = -1;
  }

  public void render() {
    bindTexture();
    obj.renderAll();
  }

  public void bindTexture() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
  }

  public void tessellate(Tessellator tes, IIcon icon) {
    Vertex vRot = null;
    for (GroupObject go : obj.groupObjects) {
      for (Face f : go.faces) {
        Vertex n = f.faceNormal;
        vRot = RenderUtil.rotate(n, rotation, rotationAxis, vRot);
        tes.setNormal(vRot.x, vRot.y, vRot.z);
        for (int i = 0; i < f.vertices.length; i++) {
          Vertex vert = f.vertices[i];
          vRot = RenderUtil.rotate(vert, rotation, rotationAxis, vRot);
          TextureCoordinate t = f.textureCoordinates[i];
          tes.addVertexWithUV(vRot.x, vRot.y, vRot.z, icon.getInterpolatedU(t.u * 16), icon.getInterpolatedV(t.v * 16));
        }
      }
    }
  }

  public void setRotation(float rad, int axis) {
    rotation = rad;
    rotationAxis = axis;
  }

  public void clearRotation() {
    rotationAxis = -1;
    rotation = 0;
  }

  public void setRotationFor(int side) {
    switch (side) {
    case 1:
      setRotation((float) Math.PI, DirUtil.EAST_PX);
      break;
    case 2:
      setRotation((float) Math.PI / 2, DirUtil.EAST_PX);
      break;
    case 3:
      setRotation((float) -Math.PI / 2, DirUtil.EAST_PX);
      break;
    case 4:
      setRotation((float) -Math.PI / 2, DirUtil.SOUTH_PZ);
      break;
    case 5:
      setRotation((float) Math.PI / 2, DirUtil.SOUTH_PZ);
      break;

    }
  }

  public void rotateGLFor(int side) {
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

  public void setRotationOnlyYFor(int side) {
    switch (side) {
    case 2:
      setRotation((float) Math.PI / 2, DirUtil.UP_PY);
      break;
    case 3:
      setRotation((float) -Math.PI / 2, DirUtil.UP_PY);
      break;
    case 4:
      setRotation((float) Math.PI, DirUtil.UP_PY);
      break;
    case 5:
      setRotation(0, DirUtil.UP_PY);
      break;

    }
  }

  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    render();
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
      RenderBlocks renderer) {
    Tessellator tes = Tessellator.instance;
    tes.addTranslation(x + 0.5f, y + 0.5f, z + 0.5f);
    tes.setColorOpaque_F(1, 1, 1);
    tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
    int meta = world.getBlockMetadata(x, y, z);
    IIcon icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : block.getIcon(0, meta);
    GL11.glPushMatrix();
    if (metaRotation) {
      setRotationOnlyYFor(meta);
    }
    tessellate(tes, icon);
    clearRotation();
    GL11.glPopMatrix();
    tes.addTranslation(-x - 0.5f, -y - 0.5f, -z - 0.5f);
    return true;
  }

  public void enableMetaRotation() {
    metaRotation = true;
  }
}
