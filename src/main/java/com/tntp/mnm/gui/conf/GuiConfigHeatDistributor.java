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
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 117);
  }

  private class Button extends GuiButton {

    public Button(int id) {
      super(id, 34 + id * 18 + guiLeft, 31 + guiTop, 18, 18, "In");
    }

    public boolean isInput() {
      return "In".equals(displayString);
    }

    public void setInput(boolean in) {
      displayString = in ? "In" : "Out";
    }

  }

}
