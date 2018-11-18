package com.tntp.mnm.network;

import com.tntp.mnm.core.MNMMod;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class MSPlayerGui extends MAInt4<MSPlayerGui> {

  public MSPlayerGui(int gui, int x, int y, int z) {
    super(gui, x, y, z);
  }

  @Override
  public IMessage onMessage(MSPlayerGui message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    player.openGui(MNMMod.MODID, getI1(), player.getEntityWorld(), getI2(), getI3(), getI4());
    return null;
  }

}
