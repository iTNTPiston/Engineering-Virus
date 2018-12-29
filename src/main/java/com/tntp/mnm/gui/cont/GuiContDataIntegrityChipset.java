package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileDataIntegrityChipset;

import net.minecraft.inventory.IInventory;

public class GuiContDataIntegrityChipset extends GuiCont {
  private boolean buttonsEnable;
  private int flag;
  private int category;

  public GuiContDataIntegrityChipset(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_data_integrity_chipset_overlay.png");
  }

  public TileDataIntegrityChipset getTile() {
    return (TileDataIntegrityChipset) ((ContainerCont) this.inventorySlots).getTile();
  }

  @Override
  public void updateScreen() {
    buttonsEnable = getTile().isMFInDebugMode();
    if (!buttonsEnable) {
      category = -1;
      flag = 0;
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foreground);
    if (!buttonsEnable) {
      this.drawTexturedModalRect(guiLeft + 12, guiTop + 19, xSize + 18, 0, 18, 18);
      this.drawTexturedModalRect(guiLeft + 48, guiTop + 19, xSize + 18, 0, 18, 18);

    } else {

    }
  }

  public void click(int button) {
    if (!buttonsEnable)
      return;
    if (button >= 2 && button <= 5) {
      if (category != 0) {
        flag = 0;
        category = 0;
      }
    }
    switch (button) {
    case 2:
      flag = flag ^ 1;
      break;
    case 3:
      flag = flag ^ 2;
      break;
    case 4:
      flag = flag ^ 4;
      break;
    case 5:
      flag = flag ^ 8;
      break;
    }
  }

}
