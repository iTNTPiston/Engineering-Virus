package com.tntp.mnm.gui.cont;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.network.MSGuiDataStorageRequest;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.util.RenderUtil;

import net.minecraft.inventory.IInventory;

/**
 * For disk storage viewing
 * 
 * @author iTNTPiston
 *
 */
public class GuiContData extends GuiCont {

  private int cachedTotalSpace = -1;

  public GuiContData(IInventory playerInventory, STileData machine, int x, int y, int z) {
    super(playerInventory, (ITileCont) machine, x, y, z);
    machine.setUsedSpaceCache(-1);
  }

  protected STileData getTile() {
    return (STileData) ((ContainerCont) this.inventorySlots).getTile();
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    super.drawGuiContainerForegroundLayer(mx, my);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int color = RenderUtil.arbg(255, 20, 20, 20);
    STileData t = getTile();
    int y = 42;
    int h = 10;
    if (t.getUsedSpaceCache() == -1) {
      MNMNetwork.network.sendToServer(new MSGuiDataStorageRequest(this.inventorySlots.windowId));
    }
    if (cachedTotalSpace == -1) {
      cachedTotalSpace = t.getTotalSpaceFromDisks();
    }
    fontRendererObj.drawString("<L>Capacity: " + t.getSizeInventory(), 7, y, color);
    fontRendererObj.drawString("<L>Used Space: " + t.getUsedSpaceCache() + " Minecraft Bytes", 7, y + h, color);
    fontRendererObj.drawString("<L>Total Space:" + cachedTotalSpace + " Minecraft Bytes", 7, y + 2 * h, color);
  }

}
