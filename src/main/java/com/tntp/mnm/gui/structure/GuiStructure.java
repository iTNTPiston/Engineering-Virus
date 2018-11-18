package com.tntp.mnm.gui.structure;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.GuiMain;
import com.tntp.mnm.gui.container.ContainerStructure;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class GuiStructure extends GuiMain {

  private Structure[][][] structure;

  private static class Structure {
    ItemStack iconStack;
    float r, g, b;
    String tooltip;
  }

  public GuiStructure(IInventory player, String title, int x, int y, int z) {
    super(new ContainerStructure(player), title);
    structure = new Structure[x][y][z];
    setupStructure();
  }

  public void setupStructure() {

  }

  protected void setStructureAt(Structure s, int x, int y, int z) {
    structure[x][y][z] = s;
  }

  protected Structure newStructure(ItemStack icon, float r, float g, float b, String tooltip) {
    Structure s = new Structure();
    s.iconStack = icon;
    s.r = r;
    s.g = g;
    s.b = b;
    s.tooltip = tooltip;
    return s;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }

}
