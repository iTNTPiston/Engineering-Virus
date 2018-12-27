package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDataGroupDefine;
import com.tntp.mnm.network.MSGuiGroupMapper;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileGroupMapper;
import com.tntp.mnm.util.LocalUtil;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.IInventory;

public class GuiContGroupMapper extends GuiCont {
  private GuiButton buttonSearch;
  private GuiButton buttonAdd;
  private GuiButton buttonRemove;
  private GuiButton buttonRemoveAll;
  private GuiTextField textField;

  public GuiContGroupMapper(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_group_mapper_overlay.png");
    ((TileGroupMapper) machine).setItemDefCache(-1);
  }

  protected TileGroupMapper getTile() {
    return (TileGroupMapper) ((ContainerCont) this.inventorySlots).getTile();
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    TileGroupMapper tile = getTile();
    boolean hasItem = tile.getStackInSlot(1) != null && tile.getItemDefCache() != -1;
    boolean hasGroup = tile.getStackInSlot(2) != null;
    buttonAdd.enabled = hasItem && hasGroup;
    buttonRemove.enabled = buttonAdd.enabled;
    buttonRemoveAll.enabled = hasItem;
  }

  @Override
  public void initGui() {
    super.initGui();
    buttonSearch = new GuiButton(0, guiLeft + 125, guiTop + 53, 40, 20,
        LocalUtil.localize("mnm.gui.group_mapper.search"));
    this.buttonList.add(buttonSearch);

    buttonAdd = new GuiButton(0, guiLeft + 7, guiTop + 90, 50, 20, LocalUtil.localize("mnm.gui.group_mapper.add"));
    this.buttonList.add(buttonAdd);

    buttonRemove = new GuiButton(0, guiLeft + 63, guiTop + 90, 50, 20,
        LocalUtil.localize("mnm.gui.group_mapper.remove"));
    this.buttonList.add(buttonRemove);

    buttonRemoveAll = new GuiButton(0, guiLeft + 119, guiTop + 90, 50, 20,
        LocalUtil.localize("mnm.gui.group_mapper.remove_all"));
    this.buttonList.add(buttonRemoveAll);

    this.textField = new GuiTextField(this.fontRendererObj, guiLeft + 72, guiTop + 36, 94, 12);
    this.textField.setTextColor(RenderUtil.argb(255, 0, 181, 0));
    this.textField.setDisabledTextColour(-1);
    this.textField.setEnableBackgroundDrawing(false);
    this.textField.setMaxStringLength(64);

  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    int color = RenderUtil.argb(255, 0, 181, 0);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.group_mapper.group"), 70, 20, color);
    int def = getTile().getItemDefCache();
    if (def != -1) {
      this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.group_mapper.definition"), 70, 57, color);
      this.fontRendererObj.drawString(String.valueOf(def), 70, 67, color);
    }
    GL11.glTranslatef(-guiLeft, -guiTop, 0);
    textField.drawTextBox();
    GL11.glTranslatef(guiLeft, guiTop, 0);
    super.drawGuiContainerForegroundLayer(mx, my);
  }

  @Override
  protected void mouseClicked(int x, int y, int p_73864_3_) {
    super.mouseClicked(x, y, p_73864_3_);
    textField.mouseClicked(x, y, p_73864_3_);
  }

  @Override
  protected void keyTyped(char p_73869_1_, int p_73869_2_) {
    if (!textField.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
      super.keyTyped(p_73869_1_, p_73869_2_);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (button == this.buttonSearch) {
      String groupName = textField.getText();
      MSGuiGroupMapper mes = new MSGuiGroupMapper(this.inventorySlots.windowId, 0, groupName);
      MNMNetwork.network.sendToServer(mes);
    } else if (button == this.buttonAdd) {
      MSGuiGroupMapper mes = new MSGuiGroupMapper(this.inventorySlots.windowId, 1, "");
      MNMNetwork.network.sendToServer(mes);
    } else if (button == this.buttonRemove) {
      MSGuiGroupMapper mes = new MSGuiGroupMapper(this.inventorySlots.windowId, 2, "");
      MNMNetwork.network.sendToServer(mes);
    } else if (button == this.buttonRemoveAll) {
      MSGuiGroupMapper mes = new MSGuiGroupMapper(this.inventorySlots.windowId, 3, "");
      MNMNetwork.network.sendToServer(mes);
    }
  }
}
