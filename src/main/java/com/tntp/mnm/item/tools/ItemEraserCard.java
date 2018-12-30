package com.tntp.mnm.item.tools;

import com.tntp.mnm.api.security.Security;
import com.tntp.mnm.gui.cont.ITileSecuredCont;
import com.tntp.mnm.item.SItemTool;
import com.tntp.mnm.network.MCChatMsg;
import com.tntp.mnm.network.MNMNetwork;
import com.tntp.mnm.util.SecurityUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemEraserCard extends SItemTool implements ISecurityItem {
  @Override
  public boolean onToolUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
      float hitY, float hitZ) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof ITileSecuredCont) {
      if (!world.isRemote) {
        Security s = ((ITileSecuredCont) tile).getSecurity();
        if (s.securityCheck(stack)) {
          s.resetSecurity();
          MCChatMsg mes = new MCChatMsg("mnm.message.security.reset", false);
          MNMNetwork.network.sendTo(mes, (EntityPlayerMP) player);
        } else {
          MCChatMsg mes = new MCChatMsg("mnm.message.security.secured", false);
          MNMNetwork.network.sendTo(mes, (EntityPlayerMP) player);
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {
    return SecurityUtil.isEncoded(stack) ? EnumRarity.rare : EnumRarity.common;
  }
}
