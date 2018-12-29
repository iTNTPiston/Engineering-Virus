package com.tntp.mnm.gui.diskkey;

import com.tntp.mnm.gui.SContainer;
import com.tntp.mnm.init.MNMItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class ContainerDiskKey extends SContainer {
  private ITileDiskKeyable tile;

  public ContainerDiskKey(IInventory playerInventory, ITileDiskKeyable machine) {
    super(playerInventory, 0, machine);
    tile = machine;
  }

  @Override
  public void setupMachineSlots(IInventory machine) {
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return tile.isUseableByPlayer(player);
  }

  public void receiveActionFromClient(int action, EntityPlayerMP player) {
    ItemStack stack = player.inventory.getItemStack();
    if (!MNMItems.disk_key.isDiskKey(stack))
      return;
    if (action == 0) {// to disk key
      MNMItems.disk_key.onTransferToDiskKey(tile, stack);
    } else if (action == 1) {
      MNMItems.disk_key.onTransferFromDiskKey(tile, stack);
    }
    player.inventory.markDirty();
    // send itemstack to client
    player.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, player.inventory.getItemStack()));
  }

}
