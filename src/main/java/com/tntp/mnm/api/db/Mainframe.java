package com.tntp.mnm.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.tntp.mnm.init.MNMBlocks;
import com.tntp.mnm.init.MNMItems;
import com.tntp.mnm.item.ItemDataGroupChip;
import com.tntp.mnm.tileentity.STileDataGroupMapping;
import com.tntp.mnm.tileentity.STileDataStorage;
import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.tileentity.STilePOB;
import com.tntp.mnm.tileentity.TileCentralProcessor;
import com.tntp.mnm.tileentity.TileDataDefinitionStorage;
import com.tntp.mnm.tileentity.TileDataGroupChipset;
import com.tntp.mnm.tileentity.TileNeithernetPort;
import com.tntp.mnm.util.ItemUtil;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
  private List<Port<STilePOB>> boardPorts;

  // private ArrayList<Object> structureList;

  private int maxID;
  private boolean scanedInTick;

  public Mainframe(TileCentralProcessor cpu) {
    this.cpu = cpu;
    neithernetPorts = new ArrayList<Port<STileNeithernet>>();
    allNnetTiles = new ArrayList<STileNeithernet>();
    boardPorts = new ArrayList<Port<STilePOB>>();
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
      // structureList.clear();
      // scan structure first (ports)
      for (Port<STileNeithernet> port : neithernetPorts) {
        port.setMainframe(null);
      }
      neithernetPorts.clear();
      for (Port<STilePOB> port : boardPorts) {
        port.setMainframe(null);
      }
      boardPorts.clear();
      boolean[][] alreadyScanned = new boolean[MAX_HORIZONTAL * 2 + 1][MAX_HORIZONTAL * 2 + 1];
      // update the list of ports
      scanBoardAt(cpu.xCoord, cpu.yCoord, cpu.zCoord, alreadyScanned, MAX_HORIZONTAL, MAX_HORIZONTAL);

      allNnetTiles.clear();
      // scan for all nnet tiles by iterating ports
      for (Port<STileNeithernet> nnetport : neithernetPorts) {
        STileNeithernet portTile = nnetport.getTile();
        portTile.addFinalTilesTo(allNnetTiles);
      }

      checkDefinitions();
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
      // structureList.add(block);
    } else if (block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
      TileEntity tile = world.getTileEntity(x, y, z);
      valid = true;
      if (tile == cpu) {
        // structureList.add(cpu.getBlockType());
      } else if (tile instanceof TileNeithernetPort) {
        Port<STileNeithernet> p = ((TileNeithernetPort) tile).getPort();
        p.setMainframe(this);
        neithernetPorts.add(p);
        // structureList.add(tile.getBlockType());
      } else if (tile instanceof STilePOB) {
        Port<STilePOB> p = ((STilePOB) tile).getPort();
        p.setMainframe(this);
        boardPorts.add(p);
        // structureList.add(tile.getBlockType());
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
    System.out.println("Inserting ItemStacks");
    // get definition of all itesm
    int[] def = new int[stack.length];
    for (int i = 0; i < def.length; i++) {
      def[i] = defineItem(stack[i]);
      System.out.println("Defined " + def[i]);
    }

    // scan all neithernet tiles for data storage
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataStorage) {
        boolean done = true;
        for (int i = 0; i < def.length; i++) {
          if (def[i] < 0)
            continue;
          if (stack[i] == null)
            continue;
          int left = ((STileDataStorage) tile).putIn(def[i], stack[i].stackSize);
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
      if (tile instanceof STileDataStorage) {
        toBeTaken = ((STileDataStorage) tile).takeAway(id, toBeTaken);
        if (toBeTaken == 0) {
          break;// finished
        }
      }
    }
    int taken = qty - toBeTaken;
    stack.stackSize = taken;
    return stack;
  }

  /**
   * Get the stacks with the defIDs in the idSet, and fill the validIDs list with
   * those def IDs
   * 
   * @param idSet
   * @param validIDs
   * @return
   */
  public ItemStack[] getQuantityFor(Set<Integer> idSet, List<Integer> validIDs) {
    // check
    scan();

    HashMap<Integer, ItemStack> qtyMap = new HashMap<Integer, ItemStack>();
    // integrity check, define all ids
    for (Integer id : idSet) {
      ItemStack s = getDefItemStack(id);
      if (s != null) {
        s.stackSize = 0;
        qtyMap.put(id, s);
      }
    }
    // fill the stacksize
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataStorage) {
        for (Entry<Integer, ItemStack> e : qtyMap.entrySet()) {
          int qty = ((STileDataStorage) tile).findQuantityFor(e.getKey());
          e.getValue().stackSize += qty;
        }
      }
    }

    // to array
    ItemStack[] array = new ItemStack[qtyMap.size()];
    int i = 0;
    for (Entry<Integer, ItemStack> e : qtyMap.entrySet()) {
      ItemStack s = e.getValue();
      array[i] = s;
      NBTTagCompound tag = s.hasTagCompound() ? s.getTagCompound() : new NBTTagCompound();
      tag.setInteger("MNM|OverridenDisplaySize", s.stackSize);
      s.stackSize = 1;
      s.setTagCompound(tag);
      validIDs.add(e.getKey());
      i++;
    }
    return array;
  }

  public ItemStack getDefItemStack(int id) {
    if (id < 0)
      return null;
    scan();
    // scan data definers for definition
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefinitionStorage) {
        ItemStack stack = ((TileDataDefinitionStorage) tile).getItemDef(id);
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
    if (maxID == Integer.MAX_VALUE - 1000)// integer overflow safety
      return -1;
    if (stack == null)
      return -1;
    // check tile data
    scan();
    // scan for existing definition first
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefinitionStorage) {
        int id = ((TileDataDefinitionStorage) tile).getItemDefID(stack);
        if (id >= 0)
          return id;// found definition
      }
    }

    // if definition not found, define new
    int id = maxID + 1;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefinitionStorage) {
        boolean defined = ((TileDataDefinitionStorage) tile).defineItem(stack, id);
        if (defined) {
          maxID = id;
          return id;
        }
      }
    }

    // cannot define
    return -1;
  }

  public GroupSearchResult getGroup(String name) {
    scan();
    GroupSearchResult result = new GroupSearchResult(name);
    String prefix = name;
    List<ItemStack> allGroups = new ArrayList<ItemStack>();

    // search tile data group
    for (Port<STilePOB> port : boardPorts) {
      STilePOB tile = port.getTile();
      if (tile instanceof TileDataGroupChipset) {
        ItemStack thisGroup = ((TileDataGroupChipset) tile).addGroups(allGroups, prefix);
        if (thisGroup != null)
          result.setCurrentGroup(thisGroup);
      }
    }
    // add to result only those with exactly one more layer
    // but later add items in all groups to item list
    ItemDataGroupChip chip = MNMItems.data_group_chip;
    List<String> groupNames = new ArrayList<String>();
    for (ItemStack groupChip : allGroups) {
      String group = chip.getGroupName(groupChip);
      int dotPos = group.lastIndexOf('.');
      if (dotPos == prefix.length() || (prefix.length() == 0 && dotPos == -1)) {
        // must contain exactly one more layer than prefix
        result.getGroupChipList().add(groupChip);
      }
      groupNames.add(group);
    }

    // search for items in group using data group mapping
    Set<Integer> allIDs = new HashSet<Integer>();
    if (prefix.length() == 0) {
      // get everything
      allIDs.addAll(checkDefinitions().keySet());
    } else {
      for (STileNeithernet tile : allNnetTiles) {
        if (tile instanceof STileDataGroupMapping) {
          for (String group : groupNames)
            ((STileDataGroupMapping) tile).findMapping(allIDs, group);
        }
      }
    }
    List<Integer> groupItemDef = new ArrayList<Integer>();
    result.setGroupItems(getQuantityFor(allIDs, groupItemDef));
    // convert to array
    int[] d = new int[groupItemDef.size()];
    int i = 0;
    for (Integer n : groupItemDef) {
      d[i] = n;
      i++;
    }
    result.setGroupItemsDef(d);
    return result;
  }

  /**
   * Search all group chips that contain the group name, put them in a list
   * 
   * @param groupName
   * @param list
   */
  public void searchGroupChip(String groupName, LinkedList<ItemStack> list) {
    if (groupName == null || groupName.length() == 0)
      return;
    scan();
    groupName = groupName.toLowerCase();
    // search tile data group
    for (Port<STilePOB> port : boardPorts) {
      STilePOB tile = port.getTile();
      if (tile instanceof TileDataGroupChipset) {
        ((TileDataGroupChipset) tile).findGroupContains(list, groupName);
      }
    }
  }

  public boolean sendQueryToCPU(QueryExecuter query) {
    if (cpu != null)
      return cpu.addQuery(query);
    return false;
  }

  public HashMap<Integer, ItemStack> checkDefinitions() {
    HashMap<Integer, ItemStack> hashmap = new HashMap<Integer, ItemStack>();
    scan();
    // scan data definers for definition
    int maxID = -1;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefinitionStorage) {
        List<ItemDef> list = ((TileDataDefinitionStorage) tile).getDefinedItems();
        for (ItemDef d : list) {
          if (d.id > maxID) {
            maxID = d.id;
          }
          if (!hashmap.containsKey(d.id)) {
            hashmap.put(d.id, d.stack);
          } else {
            // integrity violated
          }
        }
      }
    }
    this.maxID = maxID;
    return hashmap;
  }

  public ItemStack[] getDefinitions() {
    HashMap<Integer, ItemStack> map = checkDefinitions();
    ItemStack[] stacks = new ItemStack[maxID + 1];
    for (Entry<Integer, ItemStack> e : map.entrySet()) {
      stacks[e.getKey()] = e.getValue();
    }
    return stacks;
  }

  /**
   * Add the stack to the group, even if the group doesn't exist. However if the
   * stack is not defined nothing will happen
   * 
   * @param group
   * @param stack
   */
  public void addToGroup(String group, ItemStack stack) {
    int def = defineItem(stack);
    if (def < 0)
      return;
    GroupItemMapping gim = new GroupItemMapping(group, def);
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataGroupMapping) {
        if (((STileDataGroupMapping) tile).addMapping(gim))
          return;// once successful, return
      }
    }
  }

  public void removeFromGroup(String group, ItemStack stack) {
    int def = defineItem(stack);
    if (def < 0)
      return;
    GroupItemMapping gim = new GroupItemMapping(group, def);
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataGroupMapping) {
        ((STileDataGroupMapping) tile).removeMapping(gim);
        // do not break here because the same mapping can exist across different drives
      }
    }
  }

  public void removeFromAllGroups(ItemStack stack) {
    int def = defineItem(stack);
    if (def < 0)
      return;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataGroupMapping) {
        ((STileDataGroupMapping) tile).removeAll(def);
        // do not break here because the same mapping can exist across different drives
      }
    }
  }

  public void writeToNBT(NBTTagCompound tag) {

  }

  public void readFromNBT(NBTTagCompound tag) {

  }

}
