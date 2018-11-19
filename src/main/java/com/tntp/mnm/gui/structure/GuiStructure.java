package com.tntp.mnm.gui.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.SGui;
import com.tntp.mnm.init.MNMResources;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GuiStructure extends SGui {
  private static final ResourceLocation foreground = MNMResources.getResource("textures/guis/guiStructure_overlay.png");

  private Structure[][][] structure;
  protected List<Structure> icons;
  private ItemStack stack;

  protected static class Structure {
    ItemStack iconStack;
    float r, g, b;
    String tooltip;
  }

  public GuiStructure(IInventory player, String title, int x, int y, int z) {
    super(new ContainerStructure(player), title, x, y, z);
    structure = new Structure[5][5][5];
    icons = new ArrayList<Structure>();
    setupStructure();
  }

  public abstract void setupStructure();

  protected void setMainStack(ItemStack s) {
    stack = s;
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
    this.mc.getTextureManager().bindTexture(foreground);
    this.drawTexturedModalRect(0, 0, 0, 0, xSize, 117);

    for (int x = 0; x < 5; x++) {
      for (int y = 0; y < 5; y++) {
        for (int z = 0; z < 5; z++) {
          if (structure[x][y][z] == null)
            continue;
          Structure s = structure[x][y][z];
          int drawX = 52 + y * 23 + x * 4;
          int drawY = 25 + z * 4;
          GL11.glColor4f(s.r, s.g, s.b, 1);
          GL11.glDisable(GL11.GL_TEXTURE_2D);
          this.drawTexturedModalRect(drawX, drawY, 194, 0, 3, 3);
          GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
      }
    }
    int drawX = 7;
    int drawY = 53;
    List<String> emptyList = Collections.emptyList();

    for (Structure s : icons) {

      GL11.glColor4f(s.r, s.g, s.b, 1);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      this.drawTexturedModalRect(drawX, drawY, 176, 18, 18, 18);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glColor4f(1, 1, 1, 1);
      this.drawTexturedModalRect(drawX, drawY, 176, 0, 18, 18);
      drawX += 18;
    }

    drawX = 7;
    for (Structure s : icons) {
      this.drawItemStack(s.iconStack, drawX + 1, drawY + 1, mx, my,
          s.tooltip == null ? emptyList : Arrays.asList(s.tooltip));
      drawX += 18;
    }

    this.drawItemStack(stack, 19, 26, mx, my, getMainTooltip(emptyList));
    RenderHelper.enableGUIStandardItemLighting();

  }

  protected List<String> getMainTooltip(List<String> emptyList) {
    return emptyList;
  }

}
