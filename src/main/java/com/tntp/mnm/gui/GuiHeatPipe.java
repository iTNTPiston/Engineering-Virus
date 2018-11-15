package com.tntp.mnm.gui;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.container.ContainerHeatPipe;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class GuiHeatPipe extends GuiMain {
  private ItemStack endLeft;
  private ItemStack middle;
  private ItemStack endRight;

  public GuiHeatPipe(ContainerHeatPipe container) {
    super(container);
    endLeft = container.getNode1();
    endRight = container.getNode2();
    middle = new ItemStack(MNMBlocks.blockHeatPipe);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1f, 1f, 1f, 1.0F);
    drawItemStack(endLeft, 28, 40, mx, my);
    drawItemStack(middle, 60, 40, mx, my);
    drawItemStack(endRight, 100, 40, mx, my);
    GL11.glColor4f(1f, 1f, 1f, 1.0F);
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.title.heat_pipe"), 36, 8, 0);
  }

  protected void drawItemStack(ItemStack stack, int x, int y, int mx, int my) {
    if (stack != null) {
      RenderHelper.enableGUIStandardItemLighting();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), stack, x, y);
      itemRender.renderItemOverlayIntoGUI(fontRendererObj, this.mc.getTextureManager(), stack, x, y);
      GL11.glEnable(GL11.GL_BLEND);
      RenderHelper.disableStandardItemLighting();
      if (mx >= x + guiLeft && mx <= x + guiLeft + 16 && my >= y + guiTop && my <= y + guiTop + 16) {
        drawHighlightRect(x, y);
        tooltips.add(stack.getDisplayName());
        tooltipX = mx - guiLeft;
        tooltipY = my - guiTop;
      }
    }
  }

  protected void drawHighlightRect(int x, int y) {
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glColorMask(true, true, true, false);
    this.drawGradientRect(x, y, x + 16, y + 16, -2130706433, -2130706433);
    GL11.glColorMask(true, true, true, true);
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
  }
}
