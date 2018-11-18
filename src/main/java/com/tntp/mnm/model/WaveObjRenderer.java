package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

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
  protected ForgeDirection rotationAxis;

  public WaveObjRenderer(WavefrontObject obj, ResourceLocation texture) {
    this.obj = obj;
    this.texture = texture;
  }

  public void render() {
    bindTexture();
    obj.renderAll();
  }

  public void bindTexture() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
  }

  public void tessellate(Tessellator tes, IIcon icon) {
    tes(tes, icon, obj);
  }

  public void setRotation(float rad, ForgeDirection axis) {
    rotation = rad;
    rotationAxis = axis;
  }

  public void clearRotation() {
    rotationAxis = null;
  }

  protected void tes(Tessellator tes, IIcon icon, WavefrontObject obj) {
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

  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    render();
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
      RenderBlocks renderer) {
    Tessellator tes = Tessellator.instance;
    tes.addTranslation(x + 0.5f, y + 0.5f, z + 0.5f);
    tes.setColorOpaque_F(1, 1, 1);
    tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
    IIcon icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : block.getIcon(0, 0);
    tessellate(tes, icon);
    tes.addTranslation(-x - 0.5f, -y - 0.5f, -z - 0.5f);
    return true;
  }
}
