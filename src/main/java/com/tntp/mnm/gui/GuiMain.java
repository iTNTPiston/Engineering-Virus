package com.tntp.mnm.gui;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.core.MNMMod;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMain extends GuiContainer {
  private static final ResourceLocation background = new ResourceLocation(MNMMod.MODID, "textures/guis/guiMain.png");

  private GuiTabType[] tabs;
  private String[] tabGui;

  public GuiMain(Container container) {
    super(container);
    xSize = 176;
    ySize = 172;
    tabs = new GuiTabType[6];
    tabGui = new String[6];
  }

  public void setTabAt(int i, GuiTabType tab, String gui) {
    tabs[i] = tab;
    tabGui[i] = gui;
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

}
