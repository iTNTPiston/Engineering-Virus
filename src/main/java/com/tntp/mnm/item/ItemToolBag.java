package com.tntp.mnm.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.tntp.mnm.core.MNMMod;
import com.tntp.mnm.util.KeyUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class ItemToolBag extends SItem {
  private int limit;
  private IIcon left;
  private IIcon right;

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null && tag.hasKey("MNM|ToolBag")) {
      NBTTagCompound toolBag = tag.getCompoundTag("MNM|ToolBag");
      int lim = toolBag.getInteger("limit");
      int size = toolBag.getTagList("tools", NBT.TAG_COMPOUND).tagCount();
      tooltip.add(size + " / " + lim);
    }
  }

  public ItemToolBag(String regName, int lim) {
    super(regName);
    limit = lim;
    this.setMaxStackSize(1);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister reg) {
    super.registerIcons(reg);
    left = reg.registerIcon(MNMMod.MODID + ":itemArrowLeft");
    right = reg.registerIcon(MNMMod.MODID + ":itemArrowRight");

  }

  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }

  public IIcon getIcon(ItemStack stack, int pass) {
    if (pass == 0)
      return this.itemIcon;
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null)
      return this.itemIcon;
    boolean lefts = tag.getCompoundTag("MNM|ToolBag").getBoolean("side");
    return lefts ? left : right;
  }

  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (stack.stackSize > 1)
      return stack;
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound();
    NBTTagCompound toolBag;
    if (tag.hasKey("MNM|ToolBag")) {
      toolBag = tag.getCompoundTag("MNM|ToolBag");
    } else {
      toolBag = defaultTag((ItemToolBag) stack.getItem());
    }
    int index = player.inventory.currentItem;
    boolean left = toolBag.getBoolean("side");
    index = ((left ? index - 1 : index + 1) + 9) % 9;
    ItemStack activeStack = player.inventory.getStackInSlot(index);
    boolean p = putIn(toolBag, activeStack);
    if (p) {
      player.inventory.setInventorySlotContents(index, null);
      activeStack = null;
    }
    if (player.isSneaking()) {
      if (KeyUtil.isCtrlDown()) {
        switchSide(toolBag);
      }
    } else {
      if (activeStack == null) {
        activeStack = takeOut(toolBag);
        if (activeStack != null)
          activeStack.animationsToGo = 5;
        player.inventory.setInventorySlotContents(index, activeStack);
      }
    }
    tag.setTag("MNM|ToolBag", toolBag);
    stack.setTagCompound(tag);
    stack.animationsToGo = 5;
    player.inventory.markDirty();

    return stack;
  }

  // take out
  public static ItemStack takeOut(NBTTagCompound toolBag) {
    NBTTagList tools = toolBag.getTagList("tools", NBT.TAG_COMPOUND);
    if (tools.tagCount() == 0)
      return null;
    NBTTagCompound tool = (NBTTagCompound) tools.removeTag(0);
    ItemStack stack = ItemStack.loadItemStackFromNBT(tool);
    toolBag.setTag("tools", tools);
    return stack;
  }

  public static boolean putIn(NBTTagCompound toolBag, ItemStack stack) {
    if (stack == null)
      return false;
    if (stack.getItem() instanceof ItemToolBag)
      return false;
    int limit = toolBag.getInteger("limit");
    NBTTagList tools = toolBag.getTagList("tools", NBT.TAG_COMPOUND);
    if (tools.tagCount() == limit)
      return false;
    NBTTagCompound newTag = new NBTTagCompound();
    stack.writeToNBT(newTag);
    tools.appendTag(newTag);
    toolBag.setTag("tools", tools);
    return true;
  }

  public static void switchSide(NBTTagCompound toolBag) {
    boolean left = toolBag.getBoolean("side");
    toolBag.setBoolean("side", !left);
  }

  public static NBTTagCompound defaultTag(ItemToolBag item) {
    NBTTagCompound toolBag = new NBTTagCompound();
    toolBag.setInteger("limit", item.limit);
    toolBag.setBoolean("side", true);
    return toolBag;
  }

}
