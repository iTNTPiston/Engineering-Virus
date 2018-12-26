package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
import com.tntp.mnm.item.ItemDataGroupChip;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSDataGroupDefine;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.IInventory;

public class GuiContDataGroupDefiner extends GuiCont {

  private GuiButton button;
  private GuiTextField textField;

  public GuiContDataGroupDefiner(IInventory playerInventory, ITileCont machine, int x, int y, int z) {
    super(playerInventory, machine, x, y, z);
    this.foreground = MNMResources.getResource("textures/guis/gui_data_group_definer_overlay.png");
  }

  @Override
  public void initGui() {
    super.initGui();
    button = new GuiButton(0, guiLeft + 38, guiTop + 95, 100, 18,
        LocalUtil.localize("mnm.gui.data_group_definer.define"));
    this.textField = new GuiTextField(this.fontRendererObj, guiLeft + 24, guiTop + 80, 128, 12);
    this.textField.setTextColor(-1);
    this.textField.setDisabledTextColour(-1);
    this.textField.setEnableBackgroundDrawing(true);
    this.textField.setMaxStringLength(64);
    this.buttonList.add(button);
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    String text = this.textField.getText();
    button.enabled = ItemDataGroupChip.isGroupNameValid(text);
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (button == this.button) {
      String groupName = textField.getText();
      if (groupName.length() > 0) {
        MSDataGroupDefine mes = new MSDataGroupDefine(this.inventorySlots.windowId, groupName);
        MNMNetwork.network.sendToServer(mes);
      }
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.data_group_definer.icon"), 29, 22, 0);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.data_group_definer.chip"), 29, 52, 0);
    this.fontRendererObj.drawString(LocalUtil.localize("mnm.gui.data_group_definer.name"), 8, 65, 0);
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

}
