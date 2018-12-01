package com.tntp.mnm.gui.conf;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.gui.SGui;
import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSHeatDistConf;
import com.tntp.mnm.tileentity.TileHeatDistributor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiConfigHeatDistributor extends SGui {
  private static final ResourceLocation foreground = MNMResources.getResource("textures/guis/guiHeatDist_overlay.png");
  private TileHeatDistributor tile;

  public GuiConfigHeatDistributor(IInventory player, TileHeatDistributor tile, String title, int x, int y, int z) {
    super(new ContainerConfigHeatDistributor(player, tile), title, x, y, z);
    this.tile = tile;
  }

  @Override
  public void initGui() {
    super.initGui();
    for (int i = 0; i < 6; i++) {
      this.buttonList.add(new Button(i));
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    int id = button.id;
    MSHeatDistConf mes = new MSHeatDistConf(this.inventorySlots.windowId, id, !((Button) button).isInput());
    MNMNetwork.network.sendToServer(mes);
  }

  public void updateScreen() {
    for (int i = 0; i < 6; i++) {
      ((Button) this.buttonList.get(i)).setInput(tile.isSinkSide(i));
    }
    super.updateScreen();
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
  }

  private class Button extends GuiButton {

    public Button(int id) {
      super(id, 35 + id * 18, 32, 18, 18, "In");
    }

    public boolean isInput() {
      return "In".equals(displayString);
    }

    public void setInput(boolean in) {
      displayString = in ? "In" : "Out";
    }

  }

}
