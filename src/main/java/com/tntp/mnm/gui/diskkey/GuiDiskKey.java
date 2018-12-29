package com.tntp.mnm.gui.diskkey;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.SGui;
import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDiskKey;
import com.tntp.mnm.util.LocalUtil;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiDiskKey extends SGui {
  private static final ResourceLocation foreground = MNMResources.getResource("textures/guis/gui_disk_key_overlay.png");

  public GuiDiskKey(IInventory player, ITileDiskKeyable tile, int x, int y, int z) {
    super(new ContainerDiskKey(player, tile), tile.getInventoryName(), x, y, z);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int x, int y) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, x, y);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 117);

    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 134, 30, 16, 16)) {
      this.drawHighlightRect(134 + guiLeft, 30 + guiTop);
    } else if (withInRect(x, y, 134, 70, 16, 16)) {
      this.drawHighlightRect(134 + guiLeft, 70 + guiTop);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    int color = RenderUtil.argb(255, 20, 20, 20);
    int h = 10;
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.disk_key.to_message"), 11, 29, color);
    this.fontRendererObj.drawString(EnumChatFormatting.RED + LocalUtil.localize("mnm.gui.disk_key.warning"), 11, 29 + h,
        color);
    this.fontRendererObj.drawString(EnumChatFormatting.RED + LocalUtil.localize("mnm.gui.disk_key.warning_message"), 11,
        29 + h * 2, color);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.disk_key.from_message"), 11, 69, color);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.disk_key.instruction"), 11, 69 + h * 3, color);

    super.drawGuiContainerForegroundLayer(mx, my);
  }

  @Override
  protected void mouseClicked(int x, int y, int p_73864_3_) {
    super.mouseClicked(x, y, p_73864_3_);
    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 134, 30, 16, 16)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiDiskKey(this.inventorySlots.windowId, 0));
    } else if (withInRect(x, y, 134, 70, 16, 16)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiDiskKey(this.inventorySlots.windowId, 1));
    }
  }

}
