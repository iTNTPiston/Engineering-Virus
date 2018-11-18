package com.tntp.mnm.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

  public GuiHeatPipe(ContainerHeatPipe container, int x, int y, int z) {
    super(container, "mnm.gui.title.heat_pipe", x, y, z);
    endLeft = container.getNode1();
    endRight = container.getNode2();
    middle = new ItemStack(MNMBlocks.blockHeatPipe);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1f, 1f, 1f, 1.0F);
    List<String> empty = Collections.emptyList();
    drawItemStack(endLeft, 28, 40, mx, my, empty);
    drawItemStack(middle, 60, 40, mx, my, empty);
    drawItemStack(endRight, 100, 40, mx, my, empty);
    RenderHelper.enableGUIStandardItemLighting();
  }

}
