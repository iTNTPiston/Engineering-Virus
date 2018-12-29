package com.tntp.mnm.gui.diskkey;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.SGui;
import com.tntp.mnm.init.MNMResources;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDiskKey extends SGui {
  private static final ResourceLocation foreground = MNMResources.getResource("textures/guis/gui_disk_key_overlay.png");

  public GuiDiskKey(IInventory player, ITileDiskKeyable tile, int x, int y, int z) {
    super(new ContainerDiskKey(player, tile), tile.getInventoryName(), x, y, z);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 117);
  }

}
