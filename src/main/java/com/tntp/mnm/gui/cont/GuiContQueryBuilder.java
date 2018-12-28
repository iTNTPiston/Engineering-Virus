package com.tntp.mnm.gui.cont;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDataDefinitionRequest;
import com.tntp.mnm.network.MSGuiQueryBuilder;
import com.tntp.mnm.tileentity.TileQueryBuilder;
import com.tntp.mnm.util.ItemUtil;
import com.tntp.mnm.util.KeyUtil;
import com.tntp.mnm.util.LocalUtil;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

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
      this.drawSelectedSize(s.getStack(), s.xDisplayPosition, s.yDisplayPosition);
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
    } else if (withInRect(x, y, 117, 94, 42, 11)) {
      this.playButtonSound();
      MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, 1));
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
      boolean executed = false;
      for (int i = 0; i < 3 && !executed; i++) {
        for (int j = 0; j < 5 && !executed; j++) {
          if (withInRect(x, y, 71 + j * 18, 38 + i * 18, 16, 16)) {
            this.playButtonSound();
            int code = (button + 2) * 15 + i * 5 + j + 12;
            MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, code));
            executed = true;
          }
        }
      }
      if (!executed) {
        for (int i = 0; i < 5; i++) {
          if (withInRect(x, y, 61 + i * 18, 20, 16, 16)) {
            this.playButtonSound();
            MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, i + 3));
            break;
          }
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
      if (KeyUtil.isShiftDown()) {
        boolean executed = false;
        for (int i = 0; i < 3 && !executed; i++) {
          for (int j = 0; j < 5 && !executed; j++) {
            if (withInRect(x, y, 71 + j * 18, 38 + i * 18, 16, 16)) {
              this.playButtonSound();
              int code = (wheel < 0 ? 1 : 0) * 15 + i * 5 + j + 12;
              MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, code));
              executed = true;
            }
          }
        }
      } else {
        // negative wheel means 11,otherwise 10
        MNMNetwork.network.sendToServer(new MSGuiQueryBuilder(this.inventorySlots.windowId, wheel < 0 ? 11 : 10));
      }
    }
  }

  protected void drawSelectedSize(ItemStack stack, int x, int y) {
    if (stack == null || !stack.hasTagCompound())
      return;
    NBTTagCompound tag = stack.getTagCompound();
    if (!tag.hasKey("MNM|QueryBuilderSelected"))
      return;
    int selected = tag.getInteger("MNM|QueryBuilderSelected");
    if (selected <= 0)
      return;
    String size = "" + EnumChatFormatting.YELLOW + EnumChatFormatting.ITALIC + selected;
    this.zLevel = 200.0F;
    itemRender.zLevel = 200.0F;
    FontRenderer font = stack.getItem().getFontRenderer(stack);
    if (font == null)
      font = fontRendererObj;

    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_BLEND);
    font.drawStringWithShadow(size, x + 19 - 2 - font.getStringWidth(size), y, 16777215);
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_DEPTH_TEST);

    this.zLevel = 0.0F;
    itemRender.zLevel = 0.0F;

  }

}
