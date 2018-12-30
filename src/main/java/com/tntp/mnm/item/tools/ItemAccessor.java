package com.tntp.mnm.item.tools;

import com.tntp.mnm.gui.GuiTabType;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.network.MCChatMsg;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.tileentity.STileData;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileMainframeRecoveryChipset;
import com.tntp.mnm.util.ClientUtil;
import com.tntp.mnm.util.LocalUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemAccessor extends SItemTool {

  public ItemAccessor() {
    super(GuiTabType.DATA_ACCESS);
  }

  public boolean onToolUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof TileMainframeRecoveryChipset) {
      if (world.isRemote) {
        ClientUtil.printChatMessage(LocalUtil.localize("mnm.message.mainframe_recovery_chipset.init"));
      } else {
        boolean successful = ((TileMainframeRecoveryChipset) tile).attemptToRecover();
        String mes;
        if (successful) {
          mes = "mnm.message.mainframe_recovery_chipset.successful";
        } else {
          mes = "mnm.message.mainframe_recovery_chipset.failed";
        }
        MNMNetwork.network.sendTo(new MCChatMsg(mes, false), (EntityPlayerMP) player);
      }
      return true;
    } else if (tile instanceof STileData) {
      if (!world.isRemote) {
        String MFID = ((STileData) tile).getMFID();
        MNMNetwork.network.sendTo(new MCChatMsg("mnm.message.mfid", false), (EntityPlayerMP) player);
        MNMNetwork.network.sendTo(new MCChatMsg(MFID, true), (EntityPlayerMP) player);
      }
      return true;
    } else if (tile instanceof TileCentralProcessor) {
      if (!world.isRemote) {
        String MFID = ((TileCentralProcessor) tile).getMainframe().mainframeRandomID;
        MNMNetwork.network.sendTo(new MCChatMsg("mnm.message.mfid", false), (EntityPlayerMP) player);
        MNMNetwork.network.sendTo(new MCChatMsg(MFID, true), (EntityPlayerMP) player);
      }
      return true;
    }
    return false;
  }

}
