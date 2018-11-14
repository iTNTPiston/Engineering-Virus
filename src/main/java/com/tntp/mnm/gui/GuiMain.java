package com.tntp.mnm.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMain extends GuiContainer {
  private static final ResourceLocation background = MNMResources.getResource("textures/guis/guiMain.png");

  private GuiTabType[] tabs;
  private String[] tabGui;

  private int tooltipX;
  private int tooltipY;
  private List<String> tooltips;

  public GuiMain(Container container) {
    super(container);
    xSize = 176;
    ySize = 172;
    tabs = new GuiTabType[6];
    tabGui = new String[6];
    tooltips = new ArrayList<String>();
  }

  public void setTabAt(int i, GuiTabType tab, String gui) {
    tabs[i] = tab;
    tabGui[i] = gui;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    tooltips.clear();
    for (int i = 0; i < 6; i++) {
      if (tabs[i] != null && GuiTabType.isOnTab(i, mx - guiLeft, my - guiTop)) {
        tooltips.add(LocalUtil.localize(tabs[i].getUnlocalizedLabel()));
        tooltipX = mx;
        tooltipY = my;
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    for (int i = 0; i < 6; i++) {
      if (tabs[i] != null) {
        this.mc.getTextureManager().bindTexture(background);
        tabs[i].drawTab(this, i, guiLeft, guiTop);
      }
    }
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(background);
    this.drawTexturedModalRect(guiLeft + 28, guiTop, 28, 0, xSize - 56, 84);
    this.drawTexturedModalRect(guiLeft, guiTop + 84, 0, 84, xSize, ySize - 84);
  }

  public void drawScreen(int mx, int my, float p_73863_3_) {
    super.drawScreen(mx, my, p_73863_3_);
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    RenderHelper.disableStandardItemLighting();
    GL11.glPushMatrix();
    GL11.glTranslatef((float) guiLeft, (float) guiTop, 0.0F);
    drawGuiContainerTopLayer(mx, my);
    GL11.glPopMatrix();
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    RenderHelper.enableStandardItemLighting();
  }

  protected void drawGuiContainerTopLayer(int mx, int my) {
    if (!tooltips.isEmpty()) {
      drawHoveringText(tooltips, tooltipX, tooltipY, fontRendererObj);
    }
  }

}
