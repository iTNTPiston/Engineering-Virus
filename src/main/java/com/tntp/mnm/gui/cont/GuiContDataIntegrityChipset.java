package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDataIntegrityChipset;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileDataIntegrityChipset;
import com.tntp.mnm.util.LocalUtil;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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
      this.drawTexturedModalRect(guiLeft + 48, guiTop + 19, xSize + 18, 0, 18, 18);
      for (int i = 0; i < 3; i++) {
        this.drawTexturedModalRect(guiLeft + 12 + i * 18, guiTop + 51, xSize + 18, 0, 18, 18);
      }
    } else {
      if (flag == 0 || category == -1) {
        this.drawTexturedModalRect(guiLeft + 48, guiTop + 19, xSize + 18, 0, 18, 18);
      }
      if (category == 0) {
        for (int i = 1, j = 0; j < 3; i *= 2, j++) {
          if ((flag & i) == i)
            this.drawTexturedModalRect(guiLeft + 12 + j * 18, guiTop + 51, xSize, 18, 18, 18);
        }
      }
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    int color = RenderUtil.argb(255, 20, 20, 20);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.data_integrity_chipset.definition"), 13, 39, color);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.data_integrity_chipset.items"), 107, 39, 0xFF141414);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.data_integrity_chipset.mapping"), 13, 71, 0xFF141414);
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    // draw overriden stack size
    ItemStack accessor = new ItemStack(MNMItems.accessor);
    ItemStack key = new ItemStack(MNMItems.disk_key);
    ItemStack disk = new ItemStack(MNMItems.disk_64mb);
    mx -= guiLeft;
    my -= guiTop;
    itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, mc.getTextureManager(), accessor, 13, 20);
    itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, mc.getTextureManager(), accessor, 49, 20);
    itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, mc.getTextureManager(), accessor, 13, 52);
    itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, mc.getTextureManager(), key, 31, 52);
    itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, mc.getTextureManager(), disk, 49, 52);

    boolean tooltiped = false;
    tooltiped = tooltipHelper(tooltiped, mx, my, "mnm.gui.data_integrity_chipset.debug", 13, 20);
    tooltiped = tooltipHelper(tooltiped, mx, my, "mnm.gui.data_integrity_chipset.execute", 49, 20);
    tooltiped = tooltipHelperList(tooltiped, mx, my, "mnm.gui.data_integrity_chipset.definition.check_null_", 13, 52);
    tooltiped = tooltipHelperList(tooltiped, mx, my, "mnm.gui.data_integrity_chipset.definition.remove_empty_", 31, 52);
    tooltiped = tooltipHelperList(tooltiped, mx, my, "mnm.gui.data_integrity_chipset.definition.trim_", 49, 52);

  }

  private boolean tooltipHelper(boolean executed, int mx, int my, String unlocalized, int x, int y) {
    if (executed)
      return true;
    if (withInRect(mx, my, x, y, 16, 16)) {
      this.drawHighlightRect(x, y);
      tooltips.add(LocalUtil.localize(unlocalized));
      tooltipX = mx;
      tooltipY = my;
      return true;
    }
    return false;
  }

  private boolean tooltipHelperList(boolean executed, int mx, int my, String unlocalized, int x, int y) {
    if (executed)
      return true;
    if (withInRect(mx, my, x, y, 16, 16)) {
      this.drawHighlightRect(x, y);
      tooltips.addAll(LocalUtil.localizeList(unlocalized));
      tooltipX = mx;
      tooltipY = my;
      return true;
    }
    return false;
  }

  @Override
  protected void mouseClicked(int x, int y, int button) {
    super.mouseClicked(x, y, button);
    x -= guiLeft;
    y -= guiTop;
    if (withInRect(x, y, 13, 20, 16, 16)) {
      // send 0
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiDataIntegrityChipset(this.inventorySlots.windowId, 0, 0));
    } else if (withInRect(x, y, 49, 20, 16, 16)) {
      // send 1 for execute
      this.playButtonSound();
      if (flag != 0 && category != -1)
        MNMNetwork.network
            .sendToServer(new MSGuiDataIntegrityChipset(this.inventorySlots.windowId, category + 1, flag));
      flag = 0;
      category = -1;
    } else {
      boolean executed = false;
      for (int i = 0; i < 3 && !executed; i++) {
        if (withInRect(x, y, 13 + i * 18, 52, 16, 16)) {
          click(2 + i);
          executed = true;
        }
      }
    }
  }

  public void click(int button) {
    if (!buttonsEnable)
      return;
    if (button >= 2 && button <= 4) {
      if (category != 0) {
        flag = 0;
        category = 0;
      }
    }
    this.playButtonSound();
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
    }
    // reset category if all buttons are cleared
    if (flag == 0) {
      category = -1;
    }
  }

}
