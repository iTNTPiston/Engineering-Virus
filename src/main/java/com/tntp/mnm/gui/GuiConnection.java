package com.tntp.mnm.gui.heat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.SGui;
import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiConnection extends SGui {
  protected ResourceLocation foreground = MNMResources.getResource("textures/guis/gui_connection_overlay.png");

  public GuiConnection(ContainerConnection container, int x, int y, int z) {
    super(container, "mnm.gui.title.connection", x, y, z);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 117);
  }

  // @Override
  // protected void drawGuiContainerForegroundLayer(int mx, int my) {
  // super.drawGuiContainerForegroundLayer(mx, my);
  // GL11.glColor4f(1f, 1f, 1f, 1.0F);
  // List<String> empty = Collections.emptyList();
  // drawItemStack(endLeft, 28, 40, mx, my, empty);
  // drawItemStack(middle, 60, 40, mx, my, empty);
  // drawItemStack(endRight, 100, 40, mx, my, empty);
  // RenderHelper.enableGUIStandardItemLighting();
  // }

}
