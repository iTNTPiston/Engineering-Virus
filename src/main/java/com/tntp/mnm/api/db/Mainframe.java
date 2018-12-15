package com.tntp.mnm.api.db;

import java.util.List;

import com.tntp.mnm.tileentity.STile;
import com.tntp.mnm.tileentity.TileCentralProcessor;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Mainframe {
  private World world;
  private TileCentralProcessor cpu;
  private List<Port<STile>> ports;

  public Mainframe(TileCentralProcessor cpu) {
    this.cpu = cpu;
  }

  public boolean isValid() {
    return cpu != null && cpu.isValidInWorld();
  }

  public void setWorld(World w) {
    world = w;
  }

  public World getWorld() {
    return world;
  }

  public void insertItemStack(ItemStack stack) {

  }
}
