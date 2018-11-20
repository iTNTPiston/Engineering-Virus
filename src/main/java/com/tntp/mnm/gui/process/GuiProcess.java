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
    this.drawTexturedModalRect(0, 0, 0, 0, xSize, 117);

    ContainerProcess container = (ContainerProcess) inventorySlots;
    double ratio = (double) container.getTile().getCurrentProgress() / container.getTile().getTotalProgress();
    this.drawTexturedModalRect(progressX, progressY, xSize, 0, (int) (ratio * 21), 17);
  }

}
