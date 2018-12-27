package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDataDefinitionRequest;
import com.tntp.mnm.network.MSGuiQueryBuilder;
import com.tntp.mnm.tileentity.TileQueryBuilder;
import com.tntp.mnm.util.LocalUtil;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class GuiContQueryBuilder extends GuiCont {

  public GuiContQueryBuilder(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_query_builder_overlay.png");
  }

  public TileQueryBuilder getTile() {
    return (TileQueryBuilder) ((ContainerCont) this.inventorySlots).getTile();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int x, int y) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, x, y);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int color = RenderUtil.argb(255, 0, 181, 0);
    drawCenteredStringNoShadow(fontRendererObj, LocalUtil.localize("mnm.gui.query_builder.put"), 91 + guiLeft,
        96 + guiTop, color);
    drawCenteredStringNoShadow(fontRendererObj, LocalUtil.localize("mnm.gui.query_builder.take"), 140 + guiLeft,
        96 + guiTop, color);
    // draw scroll bar
    int current = getTile().getCurrentScrollIndex();
    int total = getTile().getCacheRowTotal();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    if (total > 3) {
      float scrollBarPos = Math.max(0, current * 44f / (total - 3));
      this.drawTexturedModalRect(guiLeft + 161, (int) (guiTop + 44 + scrollBarPos), xSize, 0, 6, 12);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    // draw overriden stack size
    for (int i = 12; i < 27; i++) {
      Slot s = (Slot) this.inventorySlots.inventorySlots.get(i);
      this.drawOverridenDisplaySize(s);
    }
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mx -= guiLeft;
    my -= guiTop;

    if (withInRect(mx, my, 9, 20, 16, 16)) {
      this.drawHighlightRect(9, 20);
      tooltips.add(LocalUtil.localize("mnm.gui.query_builder.return"));
      tooltipX = mx;
      tooltipY = my;
    }
  }

  @Override
  protected void mouseClicked(int x, int y, int button) {
    super.mouseClicked(x, y, button);
    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 71, 94, 42, 11)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 0));
    } else if (withInRect(x, y, 9, 20, 16, 16)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 2));
    } else if (withInRect(x, y, 46, 20, 13, 16)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 8));
    } else if (withInRect(x, y, 151, 20, 13, 16)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 9));
    } else if (withInRect(x, y, 161, 38, 6, 6)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 10));
    } else if (withInRect(x, y, 161, 100, 6, 6)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 11));
    } else {
      for (int i = 0; i < 5; i++) {
        if (withInRect(x, y, 61 + i * 18, 20, 16, 16)) {
          this.playButtonSound();
          MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, i + 3));
          break;
        }
      }
    }
  }

  @Override
  protected void mouseWheeled(int x, int y, int wheel) {
    super.mouseWheeled(x, y, wheel);
    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 8, 19, 159, 18)) {
      // negative wheel means 9,otherwise 8
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, wheel < 0 ? 9 : 8));
    } else if (withInRect(x, y, 69, 38, 98, 68)) {
      // negative wheel means 11,otherwise 10
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, wheel < 0 ? 11 : 10));
    }
  }

}
