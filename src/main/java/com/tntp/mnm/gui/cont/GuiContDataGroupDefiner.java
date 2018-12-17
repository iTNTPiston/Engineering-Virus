package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.init.MNMResources;
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
    button = new GuiButton(0, 0, 0, 100, 100, LocalUtil.localize("send<LOCAL>"));
    this.textField = new GuiTextField(this.fontRendererObj, guiLeft + 8, guiTop + 57, 52, 9);
    this.textField.setTextColor(-1);
    this.textField.setDisabledTextColour(-1);
    this.textField.setEnableBackgroundDrawing(false);
    this.textField.setMaxStringLength(32);
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (button == this.button) {

    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    GL11.glPushMatrix();
    GL11.glTranslatef(-guiLeft, -guiTop, 0);
    textField.drawTextBox();
    GL11.glPopMatrix();
    super.drawGuiContainerForegroundLayer(mx, my);
  }

}
