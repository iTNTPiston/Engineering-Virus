package com.tntp.mnm.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class SimpleObjRenderer implements ISimpleBlockRenderingHandler {
  private ArrayList<WaveObjRenderer> list = new ArrayList<WaveObjRenderer>();
  private HashMap<String, HashMap<Integer, Integer>> blockToModel = new HashMap<String, HashMap<Integer, Integer>>();

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

  public boolean renderBlock(Block block, int x, int y, int z, int metadata, int modelId, RenderBlocks renderer) {
    GL11.glPushMatrix();
    WaveObjRenderer obj;
    if (modelId < 0) {
      String key = block.getUnlocalizedName();
      HashMap<Integer, Integer> metaMap = blockToModel.get(key);
      if (metaMap == null)
        return false;
      Integer m = metaMap.get(metadata);
      if (m == null) {
        modelId = metaMap.get(0);
      } else {
        modelId = metadata;
      }
    }
    obj = list.get(modelId);

    obj.render(x, y, z);
    GL11.glPopMatrix();
    return true;
  }

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    renderBlock(block, 0, 0, 0, metadata, modelId, renderer);

  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
      RenderBlocks renderer) {
    return renderBlock(block, x, y, z, world.getBlockMetadata(x, y, z), modelId, renderer);
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  @Override
  public int getRenderId() {
    return 1;
  }

}
