package com.tntp.mnm.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

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
import com.tntp.mnm.util.RandomUtil;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class Mainframe {
  private static final int MAX_HORIZONTAL = 5, MAX_POB_RADIUS = 2;
  private static final int ITEM_PER_BYTE = 128;
  private static final int BYTE_PER_MAPPING = 2;
  private static final int BYTE_PER_DEFINITION = 4;
  public final String mainframeRandomID;
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

  private boolean integrityProblem;

  /**
   * Stores both definition and stacksize. key is the definition, value is the
   * stack with a stacksize.
   */
  private HashMap<Integer, ItemStack> itemStorage;
  private int definitionTotalSpace;
  private int storageUsedSpace;
  private int storageTotalSpace;
  private HashMap<String, Set<Integer>> groupMapping;
  private int mappingUsedSpace;
  private int mappingTotalSpace;

  public Mainframe(TileCentralProcessor cpu, String mainframeRandomID) {
    this.cpu = cpu;
    neithernetPorts = new ArrayList<Port<STileNeithernet>>();
    allNnetTiles = new ArrayList<STileNeithernet>();
    boardPorts = new ArrayList<Port<STilePOB>>();
    itemStorage = new HashMap<Integer, ItemStack>();
    groupMapping = new HashMap<String, Set<Integer>>();
    this.mainframeRandomID = mainframeRandomID;
  }

  public boolean isValid() {
    return cpu != null && cpu.isValidInWorld() && world != null;
  }

  public void setWorld(World w) {
    world = w;
  }

  public World getWorld() {
    return world;
  }

  public void markDirty() {
    if (isValid())
      cpu.markDirty();
  }

  public void scan() {
    if (!scanedInTick) {
      if (isValid()) {
        if (!cpu.isDebugModeOn()) {
          integrityProblem = false;
        }
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
        checkStorage();
        checkGroupMapping();
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
      // structureList.add(block);
    } else if (block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
      TileEntity tile = world.getTileEntity(x, y, z);
      valid = true;
      if (tile instanceof TileCentralProcessor) {
        if (tile != cpu) {
          // cannot have multiple cpus
          integrityProblem = true;
          startDebugging();
          return;
        }
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
    if (integrityProblem) {
      return;
    }
    // get definition of all itesm
    int[] def = new int[stack.length];
    for (int i = 0; i < def.length; i++) {
      def[i] = defineItem(stack[i], true);
    }

    if (integrityProblem) {// catch integrity problem caused by definition
      return;
    }

    for (int i = 0; i < def.length; i++) {
      if (def[i] < 0 || stack[i] == null || stack[i].stackSize <= 0)
        continue;
      // check space needed
      ItemStack storedStack = itemStorage.get(def[i]);
      int spaceBefore = spaceNeeded(storedStack.stackSize);
      int qtyAfter = storedStack.stackSize + stack[i].stackSize;
      int spaceAfter = spaceNeeded(qtyAfter);
      int spaceAdded = spaceAfter - spaceBefore;
      if (storageUsedSpace + spaceAdded <= storageTotalSpace) {
        // if there is space
        storedStack.stackSize = qtyAfter;
        storageUsedSpace += spaceAdded;
        stack[i] = null;
        markDirty();
      }
    }

//    // scan all neithernet tiles for data storage
//    for (STileNeithernet tile : allNnetTiles) {
//      if (tile instanceof STileDataStorage) {
//        boolean done = true;
//        for (int i = 0; i < def.length; i++) {
//          if (def[i] < 0)
//            continue;
//          if (stack[i] == null)
//            continue;
//          int left = ((STileDataStorage) tile).putIn(this, def[i], stack[i].stackSize);
//          if (left == 0) {
//            stack[i] = null;
//          } else {
//            stack[i].stackSize = left;
//            done = false;
//          }
//        }
//        if (done)
//          break;
//      }
//    }
//    markDirty();
  }

  public ItemStack takeItemStack(int id, int qty) {
    if (qty == 0)
      return null;
    // check
    scan();
    if (integrityProblem)
      return null;
    // define item
//    ItemStack stack = getDefItemStack(id);
//    if (stack == null)
//      return null;
    // get storage directly
    ItemStack stored = itemStorage.get(id);
    if (stored == null)
      return null;// definition not found
    if (stored.stackSize < qty) {
      qty = stored.stackSize;// if not enough, take whatever it has
    }
    int spaceBefore = spaceNeeded(stored.stackSize);
    ItemStack taken = stored.splitStack(qty);
    int spaceAfter = spaceNeeded(stored.stackSize);
    int spaceDecrease = spaceAfter - spaceBefore;// this should have a negative sign. after < before
    storageUsedSpace += spaceDecrease;
    markDirty();
    return taken;

//    // take
//
//    int toBeTaken = qty;
//
//    for (STileNeithernet tile : allNnetTiles) {
//      if (tile instanceof STileDataStorage) {
//        toBeTaken = ((STileDataStorage) tile).takeAway(this, id, toBeTaken);
//        if (toBeTaken == 0) {
//          break;// finished
//        }
//      }
//    }
//    int taken = qty - toBeTaken;
//    stack.stackSize = taken;
//    markDirty();
//    return stack;
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
    if (integrityProblem)
      return new ItemStack[0];
    ArrayList<ItemStack> allValidQty = new ArrayList<ItemStack>(idSet.size());

    // HashMap<Integer, ItemStack> qtyMap = new HashMap<Integer, ItemStack>();
    // integrity check, define all ids
    for (Integer id : idSet) {
      ItemStack stored = itemStorage.get(id);
      if (stored != null) {
        allValidQty.add(stored.copy());
        validIDs.add(id);
      }
    }
    // fill the stacksize
//    for (STileNeithernet tile : allNnetTiles) {
//      if (tile instanceof STileDataStorage) {
//        for (Entry<Integer, ItemStack> e : qtyMap.entrySet()) {
//          int qty = ((STileDataStorage) tile).findQuantityFor(this, e.getKey());
//          e.getValue().stackSize += qty;
//        }
//      }
//    }

    // to array
    ItemStack[] array = new ItemStack[allValidQty.size()];
    int i = 0;
    for (ItemStack e : allValidQty) {
      array[i] = e;
      NBTTagCompound tag = e.hasTagCompound() ? e.getTagCompound() : new NBTTagCompound();
      tag.setInteger("MNM|OverridenDisplaySize", e.stackSize);
      e.stackSize = 1;
      e.setTagCompound(tag);
      i++;
    }
    return array;
  }

  public ItemStack getDefItemStack(int id) {
    if (id < 0)
      return null;
    scan();
    if (integrityProblem)
      return null;
    ItemStack copy = ItemStack.copyItemStack(itemStorage.get(id));
    // set the definition copy's stacksize to 1.
    // If no def is found, copy will be null
    if (copy != null)
      copy.stackSize = 1;
    return copy;
  }

  /**
   * Get the definition of the itemstack, if none and defineNew is true, a new
   * definition will generate
   * 
   * @param stack
   * @return the definition id of the stack, or -1 if space is full
   */
  public int defineItem(ItemStack stack, boolean defineNew) {
    if (maxID == Integer.MAX_VALUE - 1000)// integer overflow safety
      return -1;
    if (stack == null)
      return -1;
    // check tile data
    scan();
    if (integrityProblem)
      return -1;
    // scan for existing definition first
    int foundID = -1;
    for (Entry<Integer, ItemStack> def : itemStorage.entrySet()) {
      if (ItemUtil.areItemAndTagEqual(def.getValue(), stack)) {
        // found a matching stack
        if (foundID == -1) {
          foundID = def.getKey();// found definition
        } else {
          // there are two def id with the same itemstack
          startDebugging();// integrity violated
          integrityProblem = true;
          return -1;
        }
      }

    }
    if (foundID != -1) {
      return foundID;
    }
    if (defineNew) {

      // if definition not found, define new
      int id = defineNew(stack, maxID + 1);
      if (id > maxID) {
        // if the item is successfully defined, the id must be maxID+1
        maxID = id;
        return id;
      }
    }

    // cannot define
    return -1;
  }

  /**
   * Define new item with the specified ID
   * 
   * @param stack
   * @param newID
   * @return newID if definition is created, or -1 if not
   */
  private int defineNew(ItemStack stack, int newID) {
    if (stack == null)
      return -1;
    if (newID < 0)
      return -1;
    scan();
    if (integrityProblem)
      return -1;
    if (itemStorage.size() * 4 + 4 <= definitionTotalSpace) {
      ItemStack s = stack.copy();
      s.stackSize = 0;
      itemStorage.put(newID, s);
      markDirty();
      return newID;
    }
    return -1;
  }

  public GroupSearchResult getGroup(String name) {
    scan();
    if (integrityProblem)
      return null;
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
    Set<Integer> allIDsInGroup = new HashSet<Integer>();
    if (prefix.length() == 0) {
      // get everything
      allIDsInGroup.addAll(checkDefinitions().keySet());
    } else {
      for (String group : groupNames) {
        Set<Integer> mapping = groupMapping.get(group);
        if (mapping != null) {
          allIDsInGroup.addAll(mapping);
        }
      }

//      for (STileNeithernet tile : allNnetTiles) {
//        if (tile instanceof STileDataGroupMapping) {
//          for (String group : groupNames)
//            ((STileDataGroupMapping) tile).findMapping(this, allIDsInGroup, group);
//        }
//      }
    }
    List<Integer> groupItemDef = new ArrayList<Integer>();
    result.setGroupItems(getQuantityFor(allIDsInGroup, groupItemDef));
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
    if (integrityProblem)
      return;
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
    if (integrityProblem)
      return false;
    if (isValid())
      return cpu.addQuery(query);
    return false;
  }

  /**
   * Update the storage space info and lose item if there is not enough space
   */
  public void checkStorage() {
    scan();
    if (integrityProblem)
      return;
    // re-calculate used space
    storageUsedSpace = 0;
    for (ItemStack stack : itemStorage.values()) {
      if (stack != null)
        storageUsedSpace += spaceNeeded(stack.stackSize);
    }

    // re-calculate total space
    int spaceNeeded = storageUsedSpace;
    storageTotalSpace = 0;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataStorage) {
        STileDataStorage t = (STileDataStorage) tile;
        if (t.access(this)) {
          int total = t.getTotalSpaceFromDisks();
          storageTotalSpace += total;
          if (total >= spaceNeeded) {
            t.setUsedSpace(this, spaceNeeded);
            spaceNeeded = 0;
          } else {
            t.setUsedSpace(this, total);
            spaceNeeded -= total;
          }
        }
      }
    }

    // check if used < total
    if (spaceNeeded > 0) {
      randomlyLoseItems();
    }
  }

  /**
   * 
   * @param stackSize
   * @return how much space is needed for the stacksize
   */
  private int spaceNeeded(int stackSize) {
    return (int) Math.ceil(stackSize / (double) ITEM_PER_BYTE);
  }

  /**
   * How many items can be put into the given space
   * 
   * @param dataspace
   * @return
   */
  private int itemCapacity(int dataspace) {
    if (dataspace < 0)
      return 0;
    return dataspace * ITEM_PER_BYTE;
  }

  /**
   * called when usedspace > totalspace, will lose 1 byte of storage
   */
  private void randomlyLoseItems() {
    int random = RandomUtil.RAND.nextInt(itemStorage.size());
    for (ItemStack s : itemStorage.values()) {
      if (random <= 0) {
        if (s.stackSize <= ITEM_PER_BYTE) {
          s.stackSize = 0;
        } else {
          s.stackSize -= ITEM_PER_BYTE;
        }
        markDirty();
        break;
      } else {
        random--;
      }
    }
  }

  /**
   * 
   * @return an ordered map (TreeMap) containing all definitions that exist and
   *         their itemstacks
   */
  public TreeMap<Integer, ItemStack> checkDefinitions() {
    TreeMap<Integer, ItemStack> treemap = new TreeMap<Integer, ItemStack>();
    scan();
    // scan data definers for definition
    int maxID = -1;

    for (Entry<Integer, ItemStack> def : itemStorage.entrySet()) {
      int id = def.getKey();
      if (id > maxID) {
        maxID = id;
      }
      treemap.put(id, def.getValue());
    }
    definitionTotalSpace = 0;
    int space = itemStorage.size() * BYTE_PER_DEFINITION;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof TileDataDefinitionStorage) {
        TileDataDefinitionStorage t = (TileDataDefinitionStorage) tile;
        if (t.access(this)) {
          int total = t.getTotalSpaceFromDisks();
          definitionTotalSpace += total;
          if (total >= space) {
            t.setUsedSpace(this, space);
            space = 0;
          } else {
            t.setUsedSpace(this, total);
            space -= total;
          }
        }
      }
    }
    this.maxID = maxID;
    if (space > 0) {
      startDebugging();
      integrityProblem = true;
    }

    return treemap;
  }

  public ItemStack[] getDefinitions() {
    Map<Integer, ItemStack> map = checkDefinitions();
    ItemStack[] stacks = new ItemStack[maxID + 1];
    for (Entry<Integer, ItemStack> e : map.entrySet()) {
      ItemStack defStack = e.getValue().copy();
      defStack.stackSize = 1;
      stacks[e.getKey()] = defStack;
    }
    return stacks;
  }

  public void checkGroupMapping() {
    scan();
    // re-calculate used space
    mappingUsedSpace = 0;
    for (Set<Integer> mapping : groupMapping.values()) {
      mappingUsedSpace += mapping.size() * BYTE_PER_MAPPING;
    }

    // re-calculate total space
    int spaceNeeded = mappingUsedSpace;
    mappingTotalSpace = 0;
    for (STileNeithernet tile : allNnetTiles) {
      if (tile instanceof STileDataGroupMapping) {
        STileDataGroupMapping t = (STileDataGroupMapping) tile;
        if (t.access(this)) {
          int total = t.getTotalSpaceFromDisks();
          mappingTotalSpace += total;
          if (total >= spaceNeeded) {
            t.setUsedSpace(this, spaceNeeded);
            spaceNeeded = 0;
          } else {
            t.setUsedSpace(this, total);
            spaceNeeded -= total;
          }
        }
      }
    }

    if (spaceNeeded > 0) {
      startDebugging();
      integrityProblem = true;
    }

  }

  /**
   * Add the stack to the group, even if the group doesn't exist. However if the
   * stack is not defined nothing will happen
   * 
   * @param group
   * @param stack
   */
  public void addToGroup(String group, ItemStack stack) {
    if (integrityProblem)
      return;
    int def = defineItem(stack, false);
    if (def < 0)
      return;
    addToGroup(group, def);
//    GroupItemMapping gim = new GroupItemMapping(group, def);
//    for (STileNeithernet tile : allNnetTiles) {
//      if (tile instanceof STileDataGroupMapping) {
//        if (((STileDataGroupMapping) tile).addMapping(this, gim))
//          return;// once successful, return
//      }
//    }
  }

  private void addToGroup(String group, int id) {
    if (mappingUsedSpace + BYTE_PER_MAPPING <= mappingTotalSpace) {
      Set<Integer> mapping = groupMapping.get(group);
      if (mapping == null) {
        mapping = new HashSet<Integer>();
        groupMapping.put(group, mapping);
      }
      mapping.add(id);
      mappingUsedSpace += mappingTotalSpace;
      markDirty();
    }
  }

  public void removeFromGroup(String group, ItemStack stack) {
    if (integrityProblem)
      return;
    int def = defineItem(stack, false);
    if (def < 0)
      return;
    removeFromGroup(group, def);
//    GroupItemMapping gim = new GroupItemMapping(group, def);
//    for (STileNeithernet tile : allNnetTiles) {
//      if (tile instanceof STileDataGroupMapping) {
//        ((STileDataGroupMapping) tile).removeMapping(this, gim);
//        // do not break here because the same mapping can exist across different drives
//      }
//    }
  }

  private void removeFromGroup(String group, int id) {
    scan();
    if (integrityProblem)
      return;
    Set<Integer> mapping = groupMapping.get(group);
    if (mapping != null)
      mapping.remove(id);
    markDirty();
  }

  public void removeFromAllGroups(ItemStack stack) {
    if (integrityProblem)
      return;
    int def = defineItem(stack, false);
    if (def < 0)
      return;
    removeFromAllGroups(def);
//    for (STileNeithernet tile : allNnetTiles) {
//      if (tile instanceof STileDataGroupMapping) {
//        ((STileDataGroupMapping) tile).removeAll(this, def);
//        // do not break here because the same mapping can exist across different drives
//      }
//    }
  }

  private void removeFromAllGroups(int id) {
    scan();
    if (integrityProblem)
      return;
    for (Set<Integer> mapping : groupMapping.values()) {
      for (Iterator<Integer> iter = mapping.iterator(); iter.hasNext();) {
        Integer i = iter.next();
        if (i == id) {
          iter.remove();
        }
      }
    }
    markDirty();
  }

  public void writeToNBT(NBTTagCompound tag) {
    tag.setBoolean("integrity_problem", integrityProblem);
    NBTTagList list = new NBTTagList();
    for (Entry<Integer, ItemStack> def : itemStorage.entrySet()) {
      NBTTagCompound com = new NBTTagCompound();
      if (def.getValue() != null)
        def.getValue().writeToNBT(com);
      com.setInteger("MNM|ItemDefID", def.getKey());
      list.appendTag(com);
    }
    tag.setTag("stored_items", list);
    NBTTagList mapping = new NBTTagList();
    for (Entry<String, Set<Integer>> m : groupMapping.entrySet()) {
      Set<Integer> mappingSet = m.getValue();
      if (!mappingSet.isEmpty()) {
        NBTTagCompound com = new NBTTagCompound();
        com.setString("group_name", m.getKey());
        int[] arr = new int[mappingSet.size()];
        int i = 0;
        for (Integer def : mappingSet) {
          arr[i] = def;
          i++;
        }
        com.setIntArray("mapped_ids", arr);
        mapping.appendTag(com);
      }
    }
    tag.setTag("group_mapping", mapping);
  }

  public void readFromNBT(NBTTagCompound tag) {
    integrityProblem = tag.getBoolean("integrity_problem");
    NBTTagList list = tag.getTagList("stored_items", NBT.TAG_COMPOUND);
    itemStorage.clear();
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound com = list.getCompoundTagAt(i);
      ItemStack stack = ItemStack.loadItemStackFromNBT(com);
      int id = com.getInteger("MNM|ItemDefID");
      itemStorage.put(id, stack);
    }

    NBTTagList mapping = tag.getTagList("group_mapping", NBT.TAG_COMPOUND);
    groupMapping.clear();
    for (int i = 0; i < mapping.tagCount(); i++) {
      NBTTagCompound com = mapping.getCompoundTagAt(i);
      String group = com.getString("group_name");
      int[] def = com.getIntArray("mapped_ids");
      Set<Integer> mappingSet = new HashSet<Integer>();
      for (int id : def) {
        mappingSet.add(id);
      }
      groupMapping.put(group, mappingSet);
    }
  }

  /**
   * flag: 1 - check null defs 2 - remove empty definitions, 4 - trim definitions
   * (remove undefined space)
   * contents<br>
   * 1 is always executed first, then 2, then 4
   * 
   * @param flag
   */
  public void organizeDefinitions(int flag) {
    if (!isReadyToDebug())
      return;
    scan();
    if (integrityProblem)
      return;
    if ((flag & 1) == 1) {
      // remove null definitions
      // Item should not have null definition, this is a safety action
      for (Iterator<Entry<Integer, ItemStack>> iter = itemStorage.entrySet().iterator(); iter.hasNext();) {
        Entry<Integer, ItemStack> e = iter.next();
        if (e.getValue() == null)
          iter.remove();
      }
    }

    if ((flag & 2) == 2) {
      // remove definitions that is not currently used.
      // aka not stored, not mapped, not in any MQL or MCS

//      HashSet<Integer> allStoredItemsDef = new HashSet<Integer>();
//      for (STileNeithernet tile : allNnetTiles) {
//        if (tile instanceof STileDataStorage) {
//          HashMap<Integer, Integer> map = ((STileDataStorage) tile).getData(this);
//          allStoredItemsDef.addAll(map.keySet());
//        }
//      }
      for (Iterator<Integer> iter = itemStorage.keySet().iterator(); iter.hasNext();) {
        Integer id = iter.next();
        if (!isDefinitionUsed(id))
          iter.remove();
      }
    }

    if ((flag & 4) == 4) {
      // remove unstored definition
      // this will also remove all mappings, reset all MQL and MCS with that
      // definition
      for (Iterator<Entry<Integer, ItemStack>> iter = itemStorage.entrySet().iterator(); iter.hasNext();) {
        Entry<Integer, ItemStack> e = iter.next();
        int id = e.getKey();
        ItemStack stack = e.getValue();
        if (stack != null && stack.stackSize <= 0) {
          iter.remove();
          propagateUndefiningItem(id);
        }

      }
    }

    if ((flag & 8) == 8) {
      // trim definitions
      // remove unused gaps. for example if 1 2 4 are defined and 3 is not (maybe
      // removed), it will redefine 4 to be 3
      TreeMap<Integer, ItemStack> allDefs = checkDefinitions();
      Queue<Integer> emptyIndices = new LinkedList<Integer>();
      // array[i] is the old index where i is the new index
      int[] reorganized = new int[allDefs.size()];
      Iterator<Integer> defIter = allDefs.navigableKeySet().iterator();
      int nextDef = -1;
      for (int i = 0; i < reorganized.length && defIter.hasNext(); i++) {
        nextDef = defIter.next();
        while (i < nextDef) {// skip over empty indices
          emptyIndices.offer(i);
          i++;
        }
        reorganized[i] = i;
      }
      // reorganize the rest
      while (defIter.hasNext()) {
        nextDef = defIter.next();
        if (!emptyIndices.isEmpty()) {
          int nextEmpty = emptyIndices.poll();
          reorganized[nextEmpty] = nextDef;
        }
      }

      for (int newIndex = 0; newIndex < reorganized.length; newIndex++) {
        int oldIndex = reorganized[newIndex];
        if (oldIndex != newIndex) {
          ItemStack stack = allDefs.get(oldIndex);
          if (stack != null) {// If stack is null, it means the definition is contained in a disk key. We must
                              // preserve the position
            redefineItem(oldIndex, newIndex, stack);
          }
        }
      }

    }

  }

  /**
   * Determine if the id is currently being referred to by anything in the MF
   * 
   * @param id
   * @return true if there is something stored, or there is a mapping with that id
   */
  private boolean isDefinitionUsed(int id) {
    // check storage
    ItemStack stored = itemStorage.get(id);
    if (stored != null && stored.stackSize > 0)
      return true;

    // check mapping
    for (Set<Integer> mapping : groupMapping.values()) {
      if (mapping.contains(id))
        return true;
    }
    return false;
  }

  /**
   * Remove everything that refers to this id. Does not check storage
   * 
   * @param id
   */
  private void propagateUndefiningItem(int id) {
    // check mapping
    for (Set<Integer> mapping : groupMapping.values()) {
      mapping.remove(id);
    }
  }

  private int redefineItem(int oldID, int newID, ItemStack stack) {
    // later this could cause MQL and MCS (Mainframe Crafting Script) to change
    scan();
    if (integrityProblem)
      return -1;
    // force remove everything with newID
    // Although, if the system is properly used, nothing should happen.
    propagateUndefiningItem(newID);
    // undefine
    ItemStack stored = itemStorage.remove(oldID);

    // define new
    int id = defineNew(stack, newID);
    if (id != -1) {
      // change mapping
      for (Set<Integer> mapping : groupMapping.values()) {
        if (mapping.contains(oldID)) {
          mapping.remove(oldID);
          mapping.add(id);
        }
      }
//      for (STileNeithernet tile : allNnetTiles) {
//        if (tile instanceof STileDataGroupMapping) {
//          ((STileDataGroupMapping) tile).modifyMapping(this, oldID, id);
//        }
//      }
      // change storage
      itemStorage.get(id).stackSize = stored.stackSize;
    }
    markDirty();
    return id;
  }

  /**
   * Organize mapping. Flag 1 will remove empty groups, Flag 2 will remove
   * repeated
   * 
   * @param flag
   */
  public void organizeMapping(int flag) {
    scan();
    if (integrityProblem)
      return;
    if ((flag & 1) == 1) {
      groupMapping.remove("");
    }
    if ((flag & 2) == 2) {
      // remove repeated mapping in super groups
      HashSet<String> operatedGroups = new HashSet<String>();
      for (Entry<String, Set<Integer>> mapping : groupMapping.entrySet()) {
        removeRepeatedMappingRecursion(operatedGroups, mapping.getKey(), mapping.getValue());
      }
    }
    markDirty();
  }

  private void removeRepeatedMappingRecursion(HashSet<String> operated, String group, Set<Integer> subContained) {
    if (operated.contains(group))
      return;
    operated.add(group);
    int dotPos = group.lastIndexOf('.');
    if (dotPos == -1)
      return;
    String superGroup = group.substring(0, dotPos);
    Set<Integer> superMapping = groupMapping.get(superGroup);
    if (superMapping != null) {
      superMapping.removeAll(subContained);
      subContained.addAll(superMapping);
    }
    removeRepeatedMappingRecursion(operated, superGroup, subContained);
  }

  /**
   * Signal the CPU to prepare debug mode. No debugging calls should be invoked
   * until isReadyToDebug() returns true
   */
  public void startDebugging() {
    if (isValid()) {
      cpu.setDebugMode(true);
      for (STileNeithernet tile : allNnetTiles) {
        tile.rescanCD = 0;
      }
    }
    markDirty();
  }

  public boolean isBootingDebug() {
    return isValid() && cpu.isDebugModeOn() && !isReadyToDebug();
  }

  public boolean isReadyToDebug() {
    return isValid() && cpu.isDebugModeReady();
  }

  /**
   * Signal the CPU to turn debug mode off
   */
  public void finishDebugging() {
    if (isValid()) {
      cpu.setDebugMode(false);
    }
    markDirty();
  }

  public int getStatus() {
    if (integrityProblem)
      return 2;
    if (isBootingDebug())
      return 2;
    if (isReadyToDebug())
      return 3;
    return 1;
  }

  public boolean canHarvest() {
    scan();
    if (integrityProblem)
      return false;
    if (!neithernetPorts.isEmpty())
      return false;
    if (!allNnetTiles.isEmpty())
      return false;
    if (!boardPorts.isEmpty())
      return false;
    if (!itemStorage.isEmpty())
      return false;
    return true;
  }

}
