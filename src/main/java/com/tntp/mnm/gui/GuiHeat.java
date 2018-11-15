package com.tntp.mnm.gui;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.api.ek.IHeatNode;
import com.tntp.mnm.gui.container.ContainerHeat;
import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiHeat extends GuiMain {
  private static final ResourceLocation foreground = MNMResources.getResource("textures/guis/guiHeat_overlay.png");

  IHeatNode heatNode;

  public GuiHeat(IInventory player, IHeatNode tile) {
    super(new ContainerHeat(player, tile));
    heatNode = tile;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    this.fontRendererObj.drawString(heatNode.getInventoryName(), 36, 8, 0);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.format.heat"), 36, 40, 0);
    this.fontRendererObj
        .drawString(LocalUtil.localize("mnm.gui.format.heat_value", heatNode.getEK(), heatNode.getMaxEK()), 36, 55, 0);

  }

  protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
    super.drawGuiContainerBackgroundLayer(f, mx, my);
    GL11.glColor4f(1f, 1f, 1f, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    drawTexturedModalRect(guiLeft + 34, guiTop + 28, 0, 0, 107, 10);
    double ratio = heatNode.getEK() / (double) heatNode.getMaxEK();
    int w = (int) (105 * ratio);
    drawTexturedModalRect(guiLeft + 35, guiTop + 29, 0, 10, w, 8);
  }

}
