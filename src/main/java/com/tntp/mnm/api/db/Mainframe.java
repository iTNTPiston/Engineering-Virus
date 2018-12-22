package com.tntp.mnm.api.db;

import java.util.ArrayList;
import java.util.List;

import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileDataDefiner;
import com.tntp.mnm.tileentity.TileDataStorage;
import com.tntp.mnm.tileentity.TileNeithernetPort;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Mainframe {
  private static final int MAX_HORIZONTAL = 5, MAX_POB_RADIUS = 2;
  private World world;
  private TileCentralProcessor cpu;
  private List<Port<STileNeithernet>> neithernetPorts;
  /**
   * excluding routers and ports on this board
   */
  private List<STileNeithernet> allNnetTiles;

  private ArrayList<Object> structureList;

  private int nextDefId;
  private boolean scanedInTick;

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

  public void scan() {
    if (!scanedInTick) {
      scanedInTick = true;
      // clear structure list
      structureList.clear();
      // scan structure first (ports)
      neithernetPorts.clear();
      boolean[][] alreadyScanned = new boolean[MAX_HORIZONTAL * 2 + 1][MAX_HORIZONTAL * 2 + 1];
      // update the list of ports
      scanBoardAt(cpu.xCoord, cpu.yCoord, cpu.zCoord, alreadyScanned, MAX_HORIZONTAL, MAX_HORIZONTAL);

      allNnetTiles.clear();
      // scan for all nnet tiles by iterating ports
      for (Port<STileNeithernet> nnetport : neithernetPorts) {
        STileNeithernet portTile = nnetport.getTile();
        portTile.addFinalTilesTo(allNnetTiles);
      }
    }
  }

  private void scanBoardAt(int x, int y, int z, boolean[][] alreadyScanned, int i, int j) {
    if (alreadyScanned[i][j])
      return;
    alreadyScanned[i][j] = true;
    Block block = world.getBlock(x, y, z);
    boolean valid = false;
    if (block == MNMBlocks.mother_board) {
      valid = true;
      structureList.add(block);
    } else if (block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
      TileEntity tile = world.getTileEntity(x, y, z);
      valid = true;
      if (tile == cpu) {
        structureList.add(cpu.getBlockType());
      } else if (tile instanceof TileNeithernetPort) {
        Port<STileNeithernet> p = ((TileNeithernetPort) tile).getPort();
        p.setMainframe(this);
        neithernetPorts.add(p);
        structureList.add(tile.getBlockType());
      } else {
        valid = false;
      }
    }

    if (valid) {
      if (i > 0) {
        scanBoardAt(x - 1, y, z, alreadyScanned, i - 1, j);
      }
      if (i < alreadyScanned.length - 1) {
        scanBoardAt(x + 1, y, z, alreadyScanned, i + 1, j);
      }
      if (j > 0) {
        scanBoardAt(x, y, z - 1, alreadyScanned, i, j - 1);
      }
      if (j < alreadyScanned.length - 1) {
        scanBoardAt(x, y, z + 1, alreadyScanned, i, j + 1);
      }
    }
  }

  public void setNeedScan() {
    scanedInTick = false;
  }

  public void insertItemStack(ItemStack... stack) {
    // make sure the tile data is up-to-date
    scan();

    // get definition of all itesm
    int[] def = new int[stack.length];
    for (int i = 0; i < def.length; i++) {
      def[i] = defineItem(stack[i]);
    }

    // scan all neithernet tiles for data storage
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataStorage) {
        boolean done = true;
        for (int i = 0; i < def.length; i++) {
          if (def[i] < 0)
            continue;
          if (stack[i] == null)
            continue;
          int left = ((TileDataStorage) tile).putIn(def[i], stack[i].stackSize);
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
    // check
    scan();
    // define item
    ItemStack stack = getDefItemStack(id);
    if (stack == null)
      return null;
    int toBeTaken = qty;

    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataStorage) {
        toBeTaken = ((TileDataStorage) tile).takeAway(id, toBeTaken);
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
    scan();
    // scan data definers for definition
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefiner) {
        ItemStack stack = ((TileDataDefiner) tile).getItemDef(id);
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
    if (nextDefId < 0)// integer overflow safety
      return -1;
    if (stack == null)
      return -1;
    // check tile data
    scan();
    // scan for existing definition first
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefiner) {
        int id = ((TileDataDefiner) tile).getItemDefID(stack);
        if (id >= 0)
          return id;// found definition
      }
    }

    // if definition not found, define new
    int id = nextDefId;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefiner) {
        boolean defined = ((TileDataDefiner) tile).defineItem(stack, id);
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
