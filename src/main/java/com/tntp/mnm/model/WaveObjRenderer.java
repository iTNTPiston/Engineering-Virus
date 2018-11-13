package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;

@SideOnly(Side.CLIENT)
public class WaveObjRenderer {
  private WavefrontObject obj;
  private ResourceLocation texture;

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

  public void tessellate(Tessellator tes, IIcon icon, boolean texOverride) {
    for (GroupObject go : obj.groupObjects) {
      for (Face f : go.faces) {
        Vertex n = f.faceNormal;
        tes.setNormal(n.x, n.y, n.z);
        for (int i = 0; i < f.vertices.length; i++) {
          Vertex vert = f.vertices[i];
          TextureCoordinate t = f.textureCoordinates[i];
          tes.addVertexWithUV(vert.x, vert.y, vert.z, icon.getInterpolatedU(t.u * 16), icon.getInterpolatedV(t.v * 16));
        }
      }
    }
  }
}
