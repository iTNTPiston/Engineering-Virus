package com.tntp.mnm.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class SimpleObjRenderer implements ISimpleBlockRenderingHandler {
  private ArrayList<WaveObjRenderer> list = new ArrayList<WaveObjRenderer>();
  private HashMap<String, HashMap<Integer, Integer>> blockToModel = new HashMap<String, HashMap<Integer, Integer>>();
  public static int id;

  public int registerWaveObj(WaveObjRenderer obj) {
    int i = list.size();
    list.add(obj);
    return i;
  }

  public void bindWaveObj(Block b, int meta, int model) {
    String key = b.getUnlocalizedName();
    HashMap<Integer, Integer> metaMap = blockToModel.get(key);
    if (metaMap == null) {
      metaMap = new HashMap<Integer, Integer>();
      blockToModel.put(key, metaMap);
      metaMap.put(0, model);
    }
    metaMap.put(meta, model);

  }

  public WaveObjRenderer getRendererFor(Block block, int metadata, int modelId) {
    WaveObjRenderer obj;
    String key = block.getUnlocalizedName();
    HashMap<Integer, Integer> metaMap = blockToModel.get(key);
    if (metaMap == null)
      return null;
    Integer m = metaMap.get(metadata);
    if (m == null) {
      modelId = metaMap.get(0);
    } else {
      modelId = m;
    }
    obj = list.get(modelId);
    return obj;
  }

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    WaveObjRenderer obj = getRendererFor(block, metadata, modelId);
    if (obj != null) {
      obj.renderInventoryBlock(block, metadata, modelId, renderer);
    }
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
      RenderBlocks renderer) {
    WaveObjRenderer obj = getRendererFor(block, world.getBlockMetadata(x, y, z), modelId);
    return obj == null ? false : obj.renderWorldBlock(world, x, y, z, block, modelId, renderer);
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return id;
  }

}
