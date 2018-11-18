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
  private static final int MAX_TABS = 8;
  private String unlocalizedTitle;
  private GuiTabType[] tabs;
  private String[] tabGui;

  protected int tooltipX;
  protected int tooltipY;
  protected List<String> tooltips;

  public GuiMain(Container container, String title) {
    super(container);
    xSize = 176;
    ySize = 205;
    tabs = new GuiTabType[MAX_TABS];
    tabGui = new String[MAX_TABS];
    tooltips = new ArrayList<String>();
    unlocalizedTitle = title;
  }

  public void setTabAt(int i, GuiTabType tab, String gui) {
    tabs[i] = tab;
    tabGui[i] = gui;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.fontRendererObj.drawString(LocalUtil.localize(unlocalizedTitle), 8, 6, 0);
    for (int i = 0; i < MAX_TABS; i++) {
      if (tabs[i] != null) {
        this.mc.getTextureManager().bindTexture(background);
        tabs[i].drawTab(this, i);
      }
    }
    RenderHelper.enableGUIStandardItemLighting();
    for (int i = 0; i < MAX_TABS; i++) {
      if (tabs[i] != null && GuiTabType.isOnTab(i, mx - guiLeft, my - guiTop)) {
        tooltips.add(LocalUtil.localize(tabs[i].getUnlocalizedLabel()));
        tooltipX = mx - guiLeft;
        tooltipY = my - guiTop;
      }
    }

  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    tooltips.clear();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(background);
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 117);
    this.drawTexturedModalRect(guiLeft, guiTop + 117, 0, 117, xSize, ySize - 117);
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

  public boolean hideItemPanel(int x, int y, int w, int h) {
    int minX = guiLeft + 176;
    int minY = guiTop;
    for (int i = 0; i < MAX_TABS; i++) {
      if (tabs[i] == null)
        break;
      int tabX = minX + i / 4;
      int tabY = minY + (i & 3) * 28;
      if (withIn(tabX, 28, x, w) && withIn(tabY, 28, y, h))
        return true;
    }
    return false;
  }

  public boolean withIn(int c1, int w1, int c2, int w2) {
    return (c2 >= c1 && c2 <= c1 + w1) || (c2 + w2 >= c1 && c2 + w2 <= c1 + w1);
  }

  protected void mouseClicked(int x, int y, int button) {
    super.mouseClicked(x, y, button);
    for (int i = 0; i < MAX_TABS; i++) {
      if (tabs[i] == null)
        break;
      if (GuiTabType.isOnTab(i, x - guiLeft, y - guiTop)) {

      }
    }
  }

}
