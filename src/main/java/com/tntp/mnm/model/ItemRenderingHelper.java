package com.tntp.mnm.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ItemRenderingHelper implements IItemRenderer {
  public static final ItemRenderingHelper instance = new ItemRenderingHelper();
  private ArrayList<WaveObjRenderer> list;
  private HashMap<String, Integer> itemToRenderer;

  private ItemRenderingHelper() {
    list = new ArrayList<WaveObjRenderer>();
    itemToRenderer = new HashMap<String, Integer>();
  }

  public int registerWaveObj(WaveObjRenderer obj) {
    int i = list.size();
    list.add(obj);
    return i;
  }

  public void bindWaveObj(Item i, int model) {
    String key = i.getUnlocalizedName();
    itemToRenderer.put(key, model);
    MinecraftForgeClient.registerItemRenderer(i, this);
  }

  public WaveObjRenderer getRenderer(ItemStack stack) {
    WaveObjRenderer obj = list.get(itemToRenderer.get(stack.getItem().getUnlocalizedName(stack)));
    return obj;
  }

  @Override
  public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    return true;
  }

  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
    return true;
  }

  @Override
  public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    switch (type) {
    case INVENTORY:
      renderInventoryItem(item, (RenderBlocks) data[0]);
      break;
    case ENTITY:
      renderEntityItem(item, (RenderBlocks) data[0], (EntityItem) data[1]);
      break;
    case EQUIPPED:
      renderThirdPersonItem(item, (RenderBlocks) data[0], (EntityLivingBase) data[1]);
    }
  }

  public void renderInventoryItem(ItemStack stack, RenderBlocks render) {
    WaveObjRenderer obj = getRenderer(stack);
    GL11.glPushMatrix();
    GL11.glRotatef(30, 0, 1, 0);
    GL11.glRotatef(45, 0, 0, 1);
    obj.render();
    GL11.glPopMatrix();
  }

  public void renderEntityItem(ItemStack stack, RenderBlocks render, EntityItem entity) {
    WaveObjRenderer obj = getRenderer(stack);
    GL11.glPushMatrix();
    GL11.glRotatef(90, 0, 0, 1);
    GL11.glTranslatef(1 / 4f, 0, 0);
    obj.render();
    GL11.glPopMatrix();
  }

  public void renderThirdPersonItem(ItemStack stack, RenderBlocks render, EntityLivingBase entity) {
    WaveObjRenderer obj = getRenderer(stack);
    GL11.glPushMatrix();
    GL11.glTranslatef(1f, 1 / 2f, 1f);
    GL11.glRotatef(45, 0, 1, 0);
    GL11.glRotatef(105, 1, 0, 0);
    GL11.glTranslatef(0, -0.7f, 0);
    obj.render();
    GL11.glPopMatrix();
  }

}
