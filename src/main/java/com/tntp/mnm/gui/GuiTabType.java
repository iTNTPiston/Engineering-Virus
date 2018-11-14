package com.tntp.mnm.gui;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public enum GuiTabType {
  HEAT(new ItemStack(MNMItems.itemWrench), "mnm.gui.label.heat", 1f, 0.7f, 0.5f);
  @SideOnly(Side.CLIENT)
  private static final RenderItem itemRender = new RenderItem();
  @SideOnly(Side.CLIENT)
  private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
  @SideOnly(Side.CLIENT)
  private static final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
  private ItemStack icon;
  private String label;
  private float r;
  private float g;
  private float b;

  GuiTabType(ItemStack icon, String label, float r, float g, float b) {
    this.icon = icon;
    this.label = label;
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public static boolean isOnTab(int tabLocation, int mx, int my) {
    int tabX = tabLocation > 2 ? 148 : 0;
    int tabY = (tabLocation % 3) * 28;
    return mx >= tabX && mx <= tabX + 28 && my >= tabY && my <= tabY + 28;
  }

  public String getUnlocalizedLabel() {
    return label;
  }

  @SideOnly(Side.CLIENT)
  public void setColorToTab() {
    GL11.glColor4f(r, g, b, 1);
  }

  @SideOnly(Side.CLIENT)
  public void drawTab(GuiContainer gui, int tabLocation, int left, int top) {

    GL11.glColor4f(r, g, b, 1.0F);
    int tabX = tabLocation > 2 ? 148 : 0;
    int tabY = (tabLocation % 3) * 28;
    gui.drawTexturedModalRect(left + tabX, top + tabY, 0, 0, 28, 28);
    GL11.glColor4f(1f, 1f, 1f, 1.0F);
    if (icon != null) {
      RenderHelper.enableGUIStandardItemLighting();
      itemRender.renderItemAndEffectIntoGUI(fontRenderer, textureManager, icon, 5 + left + tabX, 5 + top + tabY);
      itemRender.renderItemOverlayIntoGUI(fontRenderer, textureManager, icon, 5 + left + tabX, 5 + top + tabY);
      GL11.glEnable(GL11.GL_BLEND);
      RenderHelper.disableStandardItemLighting();
    }
  }

}
