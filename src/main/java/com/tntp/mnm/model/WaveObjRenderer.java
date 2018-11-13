package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

@SideOnly(Side.CLIENT)
public class WaveObjRenderer {
  private WavefrontObject obj;
  private ResourceLocation texture;

  public WaveObjRenderer(WavefrontObject obj, ResourceLocation texture) {
    this.obj = obj;
    this.texture = texture;
  }

  public void render(float x, float y, float z) {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    GL11.glTranslatef(x + 0.5f, y + 0.5F, z + 0.5F);
    GL11.glScalef(1 / 16f, 1 / 16f, 1 / 16f);
    obj.renderAll();
  }
}
