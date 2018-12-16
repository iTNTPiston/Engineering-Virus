package com.tntp.mnm.api.db;

import java.util.List;

import com.tntp.mnm.tileentity.STile;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileDataDefiner;
import com.tntp.mnm.tileentity.TileDataStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Mainframe {
  private World world;
  private TileCentralProcessor cpu;
  private List<Port<STileNeithernet>> neithernetPorts;
  private int nextDefId;

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

  public void insertItemStack(ItemStack... stack) {
    int[] def = new int[stack.length];
    for (int i = 0; i < def.length; i++) {
      def[i] = defineItem(stack[i]);
    }
    for (Port<STileNeithernet> p : neithernetPorts) {
      STileNeithernet t = p.getTile();
      STileNeithernet end = t.getPipe().getEnd(world);
      if (end instanceof TileDataStorage) {
        boolean done = true;
        for (int i = 0; i < def.length; i++) {
          if (def[i] < 0)
            continue;
          if (stack[i] == null)
            continue;
          int left = ((TileDataStorage) end).putIn(def[i], stack[i].stackSize);
          if (left == 0) {
            stack[i] = null;
          } else {
            stack[i].stackSize = left;
            done = false;
          }
        }
        if (done)
          break;
      }
    }
  }

  public ItemStack takeItemStack(int id, int qty) {
    ItemStack stack = getDefItemStack(id);
    if (stack == null)
      return null;
    int toBeTaken = qty;
    for (Port<STileNeithernet> p : neithernetPorts) {
      STileNeithernet t = p.getTile();
      STileNeithernet end = t.getPipe().getEnd(world);
      if (end instanceof TileDataStorage) {
        toBeTaken = ((TileDataStorage) end).takeAway(id, toBeTaken);
        if (toBeTaken == 0) {
          break;// finished
        }
      }
    }
    int taken = qty - toBeTaken;
    stack.stackSize = taken;
    return stack;
  }

  public ItemStack getDefItemStack(int id) {
    if (id < 0)
      return null;
    for (Port<STileNeithernet> p : neithernetPorts) {
      STileNeithernet t = p.getTile();
      STileNeithernet end = t.getPipe().getEnd(world);
      if (end instanceof TileDataDefiner) {
        ItemStack stack = ((TileDataDefiner) end).getItemDef(id);
        if (stack != null)
          return stack;
      }
    }
    return null;
  }

  /**
   * Get the definition of the itemstack, if none, a new definition will generate
   * 
   * @param stack
   * @return the definition id of the stack, or -1 if space is full
   */
  public int defineItem(ItemStack stack) {
    if (stack == null)
      return -1;
    for (Port<STileNeithernet> p : neithernetPorts) {
      STileNeithernet t = p.getTile();
      STileNeithernet end = t.getPipe().getEnd(world);
      if (end instanceof TileDataDefiner) {
        int id = ((TileDataDefiner) end).getItemDefID(stack);
        if (id >= 0)
          return id;
      }
    }
    // define new
    int id = nextDefId;
    for (Port<STileNeithernet> p : neithernetPorts) {
      STileNeithernet t = p.getTile();
      STileNeithernet end = t.getPipe().getEnd(world);
      if (end instanceof TileDataDefiner) {
        boolean defined = ((TileDataDefiner) end).defineItem(stack, id);
        if (defined) {
          nextDefId++;
          return id;
        }
      }
    }
    // cannot define
    return -1;
  }

}
