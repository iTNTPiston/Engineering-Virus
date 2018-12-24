package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiQueryBuilder;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.inventory.IInventory;

public class GuiContQueryBuilder extends GuiCont {

  public GuiContQueryBuilder(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_query_builder_overlay.png");
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int color = RenderUtil.arbg(255, 0, 181, 0);
    drawCenteredStringNoShadow(fontRendererObj, "<L>PUT", 91, 96, color);
    drawCenteredStringNoShadow(fontRendererObj, "<L>TAKE", 140, 96, color);
  }

  @Override
  protected void mouseClicked(int x, int y, int button) {
    super.mouseClicked(x, y, button);
    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 71, 94, 42, 11)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 0));
    }
  }

}
