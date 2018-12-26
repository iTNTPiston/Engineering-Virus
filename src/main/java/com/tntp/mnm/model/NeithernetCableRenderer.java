package com.tntp.mnm.model;

import org.lwjgl.opengl.GL11;

import com.tntp.mnm.tileentity.STileNeithernet;
import com.tntp.mnm.util.BlockUtil;
import com.tntp.mnm.util.DirUtil;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class NeithernetCableRenderer extends WaveObjRenderer {
  private WaveObjRenderer extRender;
  private WaveObjRenderer plugRender;

  public NeithernetCableRenderer(WavefrontObject cover, WavefrontObject ext, WavefrontObject plug,
      ResourceLocation texture) {
    super(cover, texture);
    extRender = new WaveObjRenderer(ext, texture);
    plugRender = new WaveObjRenderer(plug, texture);
  }

  public void renderExtensionFor(int side) {
    GL11.glPushMatrix();
    rotateGLFor(side);
    extRender.render();
    GL11.glPopMatrix();
  }

  public void renderCoverFor(int side) {
    GL11.glPushMatrix();
    rotateGLFor(side);
    render();
    GL11.glPopMatrix();
  }

  public void tessellateExtensionFor(Tessellator tes, IIcon icon, int side, int meta) {
    extRender.setRotationFor(side);
    extRender.tessellate(tes, icon, meta);
    extRender.clearRotation();
  }

  public void tessellateCoverFor(Tessellator tes, IIcon icon, int side, int meta) {
    setRotationFor(side);
    tessellate(tes, icon, meta);
    clearRotation();
  }

  public void tessellatePlugFor(Tessellator tes, IIcon icon, int side, int meta) {
    setPlugRotationFor(side);
    plugRender.tessellate(tes, icon, meta);
    plugRender.clearRotation();
  }

  public void setPlugRotationFor(int side) {
    plugRender.setRotationOnlyYFor(side);
  }

  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    renderExtensionFor(2);
    renderExtensionFor(3);
    renderCoverFor(0);
    renderCoverFor(1);
    renderCoverFor(4);
    renderCoverFor(5);
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
      RenderBlocks renderer) {
    Tessellator tes = Tessellator.instance;
    tes.addTranslation(x + 0.5f, y + 0.5f, z + 0.5f);
    tes.setColorOpaque_F(1, 1, 1);
    tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
    IIcon icon = renderer.hasOverrideBlockTexture() ? renderer.overrideBlockTexture : block.getIcon(0, 0);

    int meta = world.getBlockMetadata(x, y, z);
    int s = BlockUtil.pipeMetaToSide(meta);
    int s1 = s >> 4;
    int s2 = s & 15;
    for (int d = 0; d < 6; d++) {
      if (s != 0 && (d == s1 || d == s2)) {
        int[] off = DirUtil.OFFSETS[d];
        int xx = x + off[0];
        int yy = y + off[1];
        int zz = z + off[2];
        TileEntity tile = world.getTileEntity(xx, yy, zz);
        boolean plug = false;
        if (tile instanceof STileNeithernet) {
          int portSide = ((STileNeithernet) tile).getPortSide();
          if (portSide == (d ^ 1))
            plug = true;
        }
        if (plug)
          tessellatePlugFor(tes, icon, d, meta);
        else
          tessellateExtensionFor(tes, icon, d, meta);
      } else {
        tessellateCoverFor(tes, icon, d, meta);
      }
    }
    tes.addTranslation(-x - 0.5f, -y - 0.5f, -z - 0.5f);
    return true;
  }

}
