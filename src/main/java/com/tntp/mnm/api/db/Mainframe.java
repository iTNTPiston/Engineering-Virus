package com.tntp.mnm.api.db;

import com.tntp.mnm.tileentity.TileCentralProcessor;

import net.minecraft.world.World;

public class Mainframe {
  private World world;
  private TileCentralProcessor cpu;

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
}
