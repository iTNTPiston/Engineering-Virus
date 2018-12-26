package com.tntp.mnm.event;

import com.tntp.mnm.gui.cont.ContainerCont;
import com.tntp.mnm.gui.cont.ITileCont;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.tileentity.TileDataDefinitionTerminal;
import com.tntp.mnm.tileentity.TileQueryBuilder;
import com.tntp.mnm.util.LocalUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
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
          e.toolTip.add(LocalUtil.localize("mnm.tooltip.ddt.definition_arg_d", i));
        }
      } else if (t instanceof TileQueryBuilder) {
        String taggedName = MNMItems.data_group_chip.getTaggedNameFromIcon(e.itemStack);
        if (taggedName != null) {
          if (taggedName.length() == 0)
            taggedName = LocalUtil.localize("mnm.tooltip.query_builder.default_data_group");
          taggedName = LocalUtil.localize("mnm.tooltip.query_builder.group_arg_s", taggedName);
          e.toolTip.add(0, EnumChatFormatting.YELLOW + taggedName + EnumChatFormatting.RESET);
          e.toolTip.set(1, EnumChatFormatting.RESET + e.toolTip.get(1));
        }
      }
    }
  }
}
