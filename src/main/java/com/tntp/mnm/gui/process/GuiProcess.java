package com.tntp.mnm.gui.process;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.SGui;
import com.tntp.mnm.init.MNMResources;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiProcess extends SGui {

  protected int progressX;
  protected int progressY;
  protected ResourceLocation foreground;

  public GuiProcess(ContainerProcess container, String title, int x, int y, int z, int pX, int pY,
      ResourceLocation foreground) {
    super(container, title, x, y, z);
    progressX = pX;
    progressY = pY;
    this.foreground = foreground;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);

    ContainerProcess container = (ContainerProcess) inventorySlots;
    double ratio = (double) container.getTile().getCurrentProgress() / container.getTile().getTotalProgress();
    this.drawTexturedModalRect(progressX, progressY, xSize, 0, (int) (ratio * 21), 17);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 117);
  }

}
