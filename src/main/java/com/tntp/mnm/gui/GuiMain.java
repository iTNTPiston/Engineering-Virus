package com.tntp.mnm.gui;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.core.MNMMod;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiMain extends GuiContainer {
  private static final ResourceLocation background = new ResourceLocation(MNMMod.MODID, "textures/guis/guiMain.png");

  private GuiTabType[] tabs;

  public GuiMain() {
    super(new ContainerTest());
    xSize = 176;
    ySize = 172;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    this.mc.getTextureManager().bindTexture(background);
    for (int i = 0; i < 6; i++) {
      if (tabs[i] != null) {
        tabs[i].drawTab(this, i, guiLeft, guiTop);
      }
    }
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.drawTexturedModalRect(guiLeft + 28, guiTop, 28, 0, xSize - 56, 84);
    this.drawTexturedModalRect(guiLeft, guiTop + 84, 0, 84, xSize, ySize - 84);

  }

}
