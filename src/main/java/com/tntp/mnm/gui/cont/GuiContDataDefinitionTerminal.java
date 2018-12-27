package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDataDefinitionRequest;
import com.tntp.mnm.network.MSGuiDataStorageRequest;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;
import com.tntp.mnm.util.LocalUtil;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.inventory.IInventory;

public class GuiContDataDefinitionTerminal extends GuiCont {

  public GuiContDataDefinitionTerminal(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_data_definition_terminal_overlay.png");
    ((TileDataDefinitionTerminal) machine).setDefinitionLengthCache(-1);
  }

  public TileDataDefinitionTerminal getTile() {
    return (TileDataDefinitionTerminal) ((ContainerCont) this.inventorySlots).getTile();
  }
//
//  @Override
//  protected void drawGuiContainerForegroundLayer(int mx, int my) {
//    super.drawGuiContainerForegroundLayer(mx, my);
//    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    
//
//  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int x, int y) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, x, y);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int color = RenderUtil.argb(255, 0, 181, 0);
    TileDataDefinitionTerminal t = getTile();
    if (t.getDefinitionLengthCache() == -1) {
      MNMNetwork.network.sendToServer(new MSGuiDataDefinitionRequest(this.inventorySlots.windowId, -1));
    }
    fontRendererObj.drawString(LocalUtil.localize("mnm.gui.ddt.info_arg_d_d_d", t.getDefinitionLengthCache(),
        t.getCurrentRow(), t.getCurrentRow() + 3), 12 + guiLeft, 93 + guiTop, color);
    // render scroll bar
    if (t.getTotalRow() > 4) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(foreground);
      float scrollBarPos = t.getCurrentRow() * 62f / (t.getTotalRow() - 4);
      this.drawTexturedModalRect(guiLeft + 161, (int) (guiTop + 26 + scrollBarPos), xSize, 0, 6, 12);
    }
  }

  /**
   * Called when the mouse is clicked.
   */
  @Override
  protected void mouseClicked(int x, int y, int button) {
    super.mouseClicked(x, y, button);
    x -= guiLeft;
    y -= guiTop;
    if (this.withInRect(x, y, 161, 20, 6, 6)) {
      this.playButtonSound();
      MNMNetwork.network
          .sendToServer(new MSGuiDataDefinitionRequest(this.inventorySlots.windowId, getTile().getCurrentRow() - 1));
    } else if (withInRect(x, y, 161, 100, 6, 6)) {
      this.playButtonSound();
      MNMNetwork.network
          .sendToServer(new MSGuiDataDefinitionRequest(this.inventorySlots.windowId, getTile().getCurrentRow() + 1));
    }
  }

  @Override
  protected void mouseWheeled(int x, int y, int wheel) {
    super.mouseWheeled(x, y, wheel);
    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 8, 19, 159, 87)) {
      // negative wheel means increase row
      MNMNetwork.network.sendToServer(
          new MSGuiDataDefinitionRequest(this.inventorySlots.windowId, getTile().getCurrentRow() - wheel));
    }
  }
}
