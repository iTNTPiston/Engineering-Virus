package com.tntp.mnm.event;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.inventory.Container;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ClientEvent {
  @SubscribeEvent
  public void handleGuiDataDefinitionTerminalTooltip(ItemTooltipEvent e) {
    Container c = e.entityPlayer.openContainer;
    if (c instanceof ContainerCont) {
      ITileCont t = ((ContainerCont) c).getTile();
      if (t instanceof TileDataDefinitionTerminal) {
        int i = ((TileDataDefinitionTerminal) t).getDefinedIDForClient(e.itemStack);
        if (i != -1) {
          e.toolTip.add("<L>Definition: " + i);
        }
      }
    }
  }
}
