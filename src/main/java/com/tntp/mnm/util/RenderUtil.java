package com.tntp.mnm.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderUtil {
  public static Vertex rotate(Vertex v, float rad, int axis, Vertex vObj) {
    if (axis < 0) {
      return v;
    }
    if (axis == DirUtil.DOWN_MY) {
      axis = DirUtil.UP_PY;
      rad = -rad;
    } else if (axis == DirUtil.NORTH_MZ) {
      axis = DirUtil.SOUTH_PZ;
      rad = -rad;
    } else if (axis == DirUtil.WEST_MX) {
      axis = DirUtil.EAST_PX;
      rad = -rad;
    }
    if (vObj == null) {
      vObj = new Vertex(0, 0, 0);
    }
    float cos = (float) MathHelper.cos(rad);
    float sin = (float) MathHelper.sin(rad);
    float x = v.x;
    float y = v.y;
    float z = v.z;
    if (axis == DirUtil.EAST_PX) {
      vObj.x = x;
      vObj.y = cos * y - sin * z;
      vObj.z = sin * y + cos * z;
    } else if (axis == DirUtil.UP_PY) {
      vObj.x = cos * x + sin * z;
      vObj.y = y;
      vObj.z = -sin * x + cos * z;
    } else if (axis == DirUtil.SOUTH_PZ) {
      vObj.x = cos * x - sin * y;
      vObj.y = sin * x + cos * y;
      vObj.z = z;
    }
    return vObj;
  }

  public static int argb(int a, int r, int g, int b) {
    return (a << 24) + (r << 16) + (g << 8) + b;
  }

  /**
   * Determine the orthogonal face (+-x,y,z) that this normal is closest to
   * 
   * @param normal
   * @return
   */
  public static int determineFace(Vertex normal) {
    float[] sp = toSphericalAngle(normal.x, normal.y, normal.z);
    if (sp[2] < Math.PI / 4) {
      return DirUtil.UP_PY;
    } else if (sp[2] < Math.PI * 3 / 4) {
      if (sp[1] < -Math.PI * 3 / 4) {
        return DirUtil.WEST_MX;
      } else if (sp[1] < -Math.PI / 4) {
        return DirUtil.SOUTH_PZ;
      } else if (sp[1] < Math.PI / 4) {
        return DirUtil.EAST_PX;
      } else if (sp[1] < Math.PI * 3 / 4) {
        return DirUtil.NORTH_MZ;
      } else {
        return DirUtil.WEST_MX;
      }
    } else {
      return DirUtil.DOWN_MY;
    }
  }

  public static float[] toSphericalAngle(float x, float y, float z) {
    float[] sp = new float[3];
    sp[0] = 1;
    sp[1] = (float) Math.atan2(z, x);
    sp[2] = (float) Math.atan2(MathHelper.sqrt_float(x * x + z * z), y);
    return sp;
  }

  /**
   * Determine, when rendering, the closest corner this vertex is to
   * 
   * @param vert
   * @return
   */
  public static int determineVertexCorner(Vertex vert, int normal) {
    int TL = 0, BL = 1, BR = 2, TR = 3;
    boolean posX = vert.x > 0;
    boolean posY = vert.y > 0;
    boolean posZ = vert.z > 0;
    switch (normal) {
    case DirUtil.DOWN_MY:
      // tl = -x +z
      if (posX) {
        if (posZ)
          return TR;
        else
          return BR;
      } else {
        if (posZ)
          return TL;
        else
          return BL;
      }
    case DirUtil.UP_PY:
      if (posX) {
        if (posZ)
          return TL;
        else
          return BL;
      } else {
        if (posZ)
          return TR;
        else
          return BR;
      }
    case DirUtil.NORTH_MZ:
      if (posX) {
        if (posY)
          return BL;// max x max y
        else
          return BR;// max x min y
      } else {
        if (posY)
          return TL;// min x max y
        else
          return TR;// min x min y
      }
    case DirUtil.SOUTH_PZ:
      if (posX) {
        if (posY)
          return TR;// max x max y
        else
          return BR;// max x min y
      } else {
        if (posY)
          return TL;// min x max y
        else
          return BL;// min x min y
      }
    case DirUtil.WEST_MX:
      if (posY) {
        if (posZ)
          return TL;// max y max z
        else
          return BL;// max y min z
      } else {
        if (posZ)
          return TR;// min y max z
        else
          return BR;// min y min z
      }
    case DirUtil.EAST_PX:
      if (posY) {
        if (posZ)
          return TR;// max y max z
        else
          return BR;// max y min z
      } else {
        if (posZ)
          return TL;// min y max z
        else
          return BL;// min y min z
      }
    }
    return 0;
  }

  public static boolean tessellateWithAmbientOcclusion(RenderBlocks renderBlock, Tessellator tessellator, Block block,
      int x, int y, int z, int normal, Vertex[] vertices, TextureCoordinate[] tCoords, float rotation, int rotationAxis,
      float metaRotate, IIcon icon) {
    Vertex vRot = null;
    Vertex vRot2 = null;
    int l = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z);

    if (Minecraft.isAmbientOcclusionEnabled()) {
      int colorMultiplier = block.colorMultiplier(renderBlock.blockAccess, x, y, z);
      float mRed = (float) (colorMultiplier >> 16 & 255) / 255.0F;
      float mGreen = (float) (colorMultiplier >> 8 & 255) / 255.0F;
      float mBlue = (float) (colorMultiplier & 255) / 255.0F;

      renderBlock.enableAO = true;

      float brightTL = 0.0F;
      float brightBL = 0.0F;
      float brightBR = 0.0F;
      float brightTR = 0.0F;
      boolean usingNormalTexture = true;

      tessellator.setBrightness(983055);

      if (renderBlock.hasOverrideBlockTexture()) {
        usingNormalTexture = false;
      }

      boolean flag2;
      boolean flag3;
      boolean flag4;
      boolean flag5;
      int i1;
      float f7;

      if (normal == DirUtil.DOWN_MY) {
        if (renderBlock.renderMinY <= 0.0D) {
          --y;
        }

        renderBlock.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z);
        renderBlock.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z - 1);
        renderBlock.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z + 1);
        renderBlock.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z);
        renderBlock.aoLightValueScratchXYNN = renderBlock.blockAccess.getBlock(x - 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZNN = renderBlock.blockAccess.getBlock(x, y, z - 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZNP = renderBlock.blockAccess.getBlock(x, y, z + 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXYPN = renderBlock.blockAccess.getBlock(x + 1, y, z)
            .getAmbientOcclusionLightValue();
        flag2 = renderBlock.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
        flag3 = renderBlock.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
        flag4 = renderBlock.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
        flag5 = renderBlock.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

        if (!flag5 && !flag3) {
          renderBlock.aoLightValueScratchXYZNNN = renderBlock.aoLightValueScratchXYNN;
          renderBlock.aoBrightnessXYZNNN = renderBlock.aoBrightnessXYNN;
        } else {
          renderBlock.aoLightValueScratchXYZNNN = renderBlock.blockAccess.getBlock(x - 1, y, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z - 1);
        }

        if (!flag4 && !flag3) {
          renderBlock.aoLightValueScratchXYZNNP = renderBlock.aoLightValueScratchXYNN;
          renderBlock.aoBrightnessXYZNNP = renderBlock.aoBrightnessXYNN;
        } else {
          renderBlock.aoLightValueScratchXYZNNP = renderBlock.blockAccess.getBlock(x - 1, y, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z + 1);
        }

        if (!flag5 && !flag2) {
          renderBlock.aoLightValueScratchXYZPNN = renderBlock.aoLightValueScratchXYPN;
          renderBlock.aoBrightnessXYZPNN = renderBlock.aoBrightnessXYPN;
        } else {
          renderBlock.aoLightValueScratchXYZPNN = renderBlock.blockAccess.getBlock(x + 1, y, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z - 1);
        }

        if (!flag4 && !flag2) {
          renderBlock.aoLightValueScratchXYZPNP = renderBlock.aoLightValueScratchXYPN;
          renderBlock.aoBrightnessXYZPNP = renderBlock.aoBrightnessXYPN;
        } else {
          renderBlock.aoLightValueScratchXYZPNP = renderBlock.blockAccess.getBlock(x + 1, y, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z + 1);
        }

        if (renderBlock.renderMinY <= 0.0D) {
          ++y;
        }

        i1 = l;

        if (renderBlock.renderMinY <= 0.0D || !renderBlock.blockAccess.getBlock(x, y - 1, z).isOpaqueCube()) {
          i1 = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z);
        }

        f7 = renderBlock.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        brightTL = (renderBlock.aoLightValueScratchXYZNNP + renderBlock.aoLightValueScratchXYNN
            + renderBlock.aoLightValueScratchYZNP + f7) / 4.0F;
        brightTR = (renderBlock.aoLightValueScratchYZNP + f7 + renderBlock.aoLightValueScratchXYZPNP
            + renderBlock.aoLightValueScratchXYPN) / 4.0F;
        brightBR = (f7 + renderBlock.aoLightValueScratchYZNN + renderBlock.aoLightValueScratchXYPN
            + renderBlock.aoLightValueScratchXYZPNN) / 4.0F;
        brightBL = (renderBlock.aoLightValueScratchXYNN + renderBlock.aoLightValueScratchXYZNNN + f7
            + renderBlock.aoLightValueScratchYZNN) / 4.0F;
        renderBlock.brightnessTopLeft = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYZNNP,
            renderBlock.aoBrightnessXYNN, renderBlock.aoBrightnessYZNP, i1);
        renderBlock.brightnessTopRight = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZNP,
            renderBlock.aoBrightnessXYZPNP, renderBlock.aoBrightnessXYPN, i1);
        renderBlock.brightnessBottomRight = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZNN,
            renderBlock.aoBrightnessXYPN, renderBlock.aoBrightnessXYZPNN, i1);
        renderBlock.brightnessBottomLeft = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYNN,
            renderBlock.aoBrightnessXYZNNN, renderBlock.aoBrightnessYZNN, i1);

        if (usingNormalTexture) {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = mRed
              * 0.5F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = mGreen
              * 0.5F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = mBlue
              * 0.5F;
        } else {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = 0.5F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = 0.5F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = 0.5F;
        }

        renderBlock.colorRedTopLeft *= brightTL;
        renderBlock.colorGreenTopLeft *= brightTL;
        renderBlock.colorBlueTopLeft *= brightTL;
        renderBlock.colorRedBottomLeft *= brightBL;
        renderBlock.colorGreenBottomLeft *= brightBL;
        renderBlock.colorBlueBottomLeft *= brightBL;
        renderBlock.colorRedBottomRight *= brightBR;
        renderBlock.colorGreenBottomRight *= brightBR;
        renderBlock.colorBlueBottomRight *= brightBR;
        renderBlock.colorRedTopRight *= brightTR;
        renderBlock.colorGreenTopRight *= brightTR;
        renderBlock.colorBlueTopRight *= brightTR;

        // tessellation is moved to the end, this is only calculating AO

      } else if (normal == DirUtil.UP_PY) {
        if (renderBlock.renderMaxY >= 1.0D) {
          ++y;
        }

        renderBlock.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z);
        renderBlock.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z);
        renderBlock.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z - 1);
        renderBlock.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z + 1);
        renderBlock.aoLightValueScratchXYNP = renderBlock.blockAccess.getBlock(x - 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXYPP = renderBlock.blockAccess.getBlock(x + 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZPN = renderBlock.blockAccess.getBlock(x, y, z - 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZPP = renderBlock.blockAccess.getBlock(x, y, z + 1)
            .getAmbientOcclusionLightValue();
        flag2 = renderBlock.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
        flag3 = renderBlock.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
        flag4 = renderBlock.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
        flag5 = renderBlock.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

        if (!flag5 && !flag3) {
          renderBlock.aoLightValueScratchXYZNPN = renderBlock.aoLightValueScratchXYNP;
          renderBlock.aoBrightnessXYZNPN = renderBlock.aoBrightnessXYNP;
        } else {
          renderBlock.aoLightValueScratchXYZNPN = renderBlock.blockAccess.getBlock(x - 1, y, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z - 1);
        }

        if (!flag5 && !flag2) {
          renderBlock.aoLightValueScratchXYZPPN = renderBlock.aoLightValueScratchXYPP;
          renderBlock.aoBrightnessXYZPPN = renderBlock.aoBrightnessXYPP;
        } else {
          renderBlock.aoLightValueScratchXYZPPN = renderBlock.blockAccess.getBlock(x + 1, y, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z - 1);
        }

        if (!flag4 && !flag3) {
          renderBlock.aoLightValueScratchXYZNPP = renderBlock.aoLightValueScratchXYNP;
          renderBlock.aoBrightnessXYZNPP = renderBlock.aoBrightnessXYNP;
        } else {
          renderBlock.aoLightValueScratchXYZNPP = renderBlock.blockAccess.getBlock(x - 1, y, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z + 1);
        }

        if (!flag4 && !flag2) {
          renderBlock.aoLightValueScratchXYZPPP = renderBlock.aoLightValueScratchXYPP;
          renderBlock.aoBrightnessXYZPPP = renderBlock.aoBrightnessXYPP;
        } else {
          renderBlock.aoLightValueScratchXYZPPP = renderBlock.blockAccess.getBlock(x + 1, y, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z + 1);
        }

        if (renderBlock.renderMaxY >= 1.0D) {
          --y;
        }

        i1 = l;

        if (renderBlock.renderMaxY >= 1.0D || !renderBlock.blockAccess.getBlock(x, y + 1, z).isOpaqueCube()) {
          i1 = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z);
        }

        f7 = renderBlock.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        brightTR = (renderBlock.aoLightValueScratchXYZNPP + renderBlock.aoLightValueScratchXYNP
            + renderBlock.aoLightValueScratchYZPP + f7) / 4.0F;
        brightTL = (renderBlock.aoLightValueScratchYZPP + f7 + renderBlock.aoLightValueScratchXYZPPP
            + renderBlock.aoLightValueScratchXYPP) / 4.0F;
        brightBL = (f7 + renderBlock.aoLightValueScratchYZPN + renderBlock.aoLightValueScratchXYPP
            + renderBlock.aoLightValueScratchXYZPPN) / 4.0F;
        brightBR = (renderBlock.aoLightValueScratchXYNP + renderBlock.aoLightValueScratchXYZNPN + f7
            + renderBlock.aoLightValueScratchYZPN) / 4.0F;
        renderBlock.brightnessTopRight = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYZNPP,
            renderBlock.aoBrightnessXYNP, renderBlock.aoBrightnessYZPP, i1);
        renderBlock.brightnessTopLeft = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZPP,
            renderBlock.aoBrightnessXYZPPP, renderBlock.aoBrightnessXYPP, i1);
        renderBlock.brightnessBottomLeft = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZPN,
            renderBlock.aoBrightnessXYPP, renderBlock.aoBrightnessXYZPPN, i1);
        renderBlock.brightnessBottomRight = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYNP,
            renderBlock.aoBrightnessXYZNPN, renderBlock.aoBrightnessYZPN, i1);
        renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = mRed;
        renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = mGreen;
        renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = mBlue;
        renderBlock.colorRedTopLeft *= brightTL;
        renderBlock.colorGreenTopLeft *= brightTL;
        renderBlock.colorBlueTopLeft *= brightTL;
        renderBlock.colorRedBottomLeft *= brightBL;
        renderBlock.colorGreenBottomLeft *= brightBL;
        renderBlock.colorBlueBottomLeft *= brightBL;
        renderBlock.colorRedBottomRight *= brightBR;
        renderBlock.colorGreenBottomRight *= brightBR;
        renderBlock.colorBlueBottomRight *= brightBR;
        renderBlock.colorRedTopRight *= brightTR;
        renderBlock.colorGreenTopRight *= brightTR;
        renderBlock.colorBlueTopRight *= brightTR;

      }

      float f8;
      float f9;
      float f10;
      float f11;
      int j1;
      int k1;
      int l1;
      int i2;

      if (normal == DirUtil.NORTH_MZ) {
        if (renderBlock.renderMinZ <= 0.0D) {
          --z;
        }

        renderBlock.aoLightValueScratchXZNN = renderBlock.blockAccess.getBlock(x - 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZNN = renderBlock.blockAccess.getBlock(x, y - 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZPN = renderBlock.blockAccess.getBlock(x, y + 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXZPN = renderBlock.blockAccess.getBlock(x + 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z);
        renderBlock.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z);
        renderBlock.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z);
        renderBlock.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z);
        flag2 = renderBlock.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
        flag3 = renderBlock.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
        flag4 = renderBlock.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
        flag5 = renderBlock.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

        if (!flag3 && !flag5) {
          renderBlock.aoLightValueScratchXYZNNN = renderBlock.aoLightValueScratchXZNN;
          renderBlock.aoBrightnessXYZNNN = renderBlock.aoBrightnessXZNN;
        } else {
          renderBlock.aoLightValueScratchXYZNNN = renderBlock.blockAccess.getBlock(x - 1, y - 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y - 1, z);
        }

        if (!flag3 && !flag4) {
          renderBlock.aoLightValueScratchXYZNPN = renderBlock.aoLightValueScratchXZNN;
          renderBlock.aoBrightnessXYZNPN = renderBlock.aoBrightnessXZNN;
        } else {
          renderBlock.aoLightValueScratchXYZNPN = renderBlock.blockAccess.getBlock(x - 1, y + 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y + 1, z);
        }

        if (!flag2 && !flag5) {
          renderBlock.aoLightValueScratchXYZPNN = renderBlock.aoLightValueScratchXZPN;
          renderBlock.aoBrightnessXYZPNN = renderBlock.aoBrightnessXZPN;
        } else {
          renderBlock.aoLightValueScratchXYZPNN = renderBlock.blockAccess.getBlock(x + 1, y - 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y - 1, z);
        }

        if (!flag2 && !flag4) {
          renderBlock.aoLightValueScratchXYZPPN = renderBlock.aoLightValueScratchXZPN;
          renderBlock.aoBrightnessXYZPPN = renderBlock.aoBrightnessXZPN;
        } else {
          renderBlock.aoLightValueScratchXYZPPN = renderBlock.blockAccess.getBlock(x + 1, y + 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y + 1, z);
        }

        if (renderBlock.renderMinZ <= 0.0D) {
          ++z;
        }

        i1 = l;

        if (renderBlock.renderMinZ <= 0.0D || !renderBlock.blockAccess.getBlock(x, y, z - 1).isOpaqueCube()) {
          i1 = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z - 1);
        }

        f7 = renderBlock.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        f8 = (renderBlock.aoLightValueScratchXZNN + renderBlock.aoLightValueScratchXYZNPN + f7
            + renderBlock.aoLightValueScratchYZPN) / 4.0F;
        f9 = (f7 + renderBlock.aoLightValueScratchYZPN + renderBlock.aoLightValueScratchXZPN
            + renderBlock.aoLightValueScratchXYZPPN) / 4.0F;
        f10 = (renderBlock.aoLightValueScratchYZNN + f7 + renderBlock.aoLightValueScratchXYZPNN
            + renderBlock.aoLightValueScratchXZPN) / 4.0F;
        f11 = (renderBlock.aoLightValueScratchXYZNNN + renderBlock.aoLightValueScratchXZNN
            + renderBlock.aoLightValueScratchYZNN + f7) / 4.0F;
        brightTL = (float) ((double) f8 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMinX)
            + (double) f9 * renderBlock.renderMaxY * renderBlock.renderMinX
            + (double) f10 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinX
            + (double) f11 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinX));
        brightBL = (float) ((double) f8 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxX)
            + (double) f9 * renderBlock.renderMaxY * renderBlock.renderMaxX
            + (double) f10 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxX
            + (double) f11 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxX));
        brightBR = (float) ((double) f8 * renderBlock.renderMinY * (1.0D - renderBlock.renderMaxX)
            + (double) f9 * renderBlock.renderMinY * renderBlock.renderMaxX
            + (double) f10 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxX
            + (double) f11 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxX));
        brightTR = (float) ((double) f8 * renderBlock.renderMinY * (1.0D - renderBlock.renderMinX)
            + (double) f9 * renderBlock.renderMinY * renderBlock.renderMinX
            + (double) f10 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMinX
            + (double) f11 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinX));
        j1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXZNN, renderBlock.aoBrightnessXYZNPN,
            renderBlock.aoBrightnessYZPN, i1);
        k1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZPN, renderBlock.aoBrightnessXZPN,
            renderBlock.aoBrightnessXYZPPN, i1);
        l1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZNN, renderBlock.aoBrightnessXYZPNN,
            renderBlock.aoBrightnessXZPN, i1);
        i2 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYZNNN, renderBlock.aoBrightnessXZNN,
            renderBlock.aoBrightnessYZNN, i1);
        renderBlock.brightnessTopLeft = renderBlock.mixAoBrightness(j1, k1, l1, i2,
            renderBlock.renderMaxY * (1.0D - renderBlock.renderMinX), renderBlock.renderMaxY * renderBlock.renderMinX,
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinX,
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinX));
        renderBlock.brightnessBottomLeft = renderBlock.mixAoBrightness(j1, k1, l1, i2,
            renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxX), renderBlock.renderMaxY * renderBlock.renderMaxX,
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxX,
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxX));
        renderBlock.brightnessBottomRight = renderBlock.mixAoBrightness(j1, k1, l1, i2,
            renderBlock.renderMinY * (1.0D - renderBlock.renderMaxX), renderBlock.renderMinY * renderBlock.renderMaxX,
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxX,
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxX));
        renderBlock.brightnessTopRight = renderBlock.mixAoBrightness(j1, k1, l1, i2,
            renderBlock.renderMinY * (1.0D - renderBlock.renderMinX), renderBlock.renderMinY * renderBlock.renderMinX,
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMinX,
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinX));

        if (usingNormalTexture) {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = mRed
              * 0.8F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = mGreen
              * 0.8F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = mBlue
              * 0.8F;
        } else {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = 0.8F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = 0.8F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = 0.8F;
        }

        renderBlock.colorRedTopLeft *= brightTL;
        renderBlock.colorGreenTopLeft *= brightTL;
        renderBlock.colorBlueTopLeft *= brightTL;
        renderBlock.colorRedBottomLeft *= brightBL;
        renderBlock.colorGreenBottomLeft *= brightBL;
        renderBlock.colorBlueBottomLeft *= brightBL;
        renderBlock.colorRedBottomRight *= brightBR;
        renderBlock.colorGreenBottomRight *= brightBR;
        renderBlock.colorBlueBottomRight *= brightBR;
        renderBlock.colorRedTopRight *= brightTR;
        renderBlock.colorGreenTopRight *= brightTR;
        renderBlock.colorBlueTopRight *= brightTR;

      } else if (normal == DirUtil.SOUTH_PZ) {
        if (renderBlock.renderMaxZ >= 1.0D) {
          ++z;
        }

        renderBlock.aoLightValueScratchXZNP = renderBlock.blockAccess.getBlock(x - 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXZPP = renderBlock.blockAccess.getBlock(x + 1, y, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZNP = renderBlock.blockAccess.getBlock(x, y - 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchYZPP = renderBlock.blockAccess.getBlock(x, y + 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z);
        renderBlock.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z);
        renderBlock.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z);
        renderBlock.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z);
        flag2 = renderBlock.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
        flag3 = renderBlock.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
        flag4 = renderBlock.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
        flag5 = renderBlock.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();

        if (!flag3 && !flag5) {
          renderBlock.aoLightValueScratchXYZNNP = renderBlock.aoLightValueScratchXZNP;
          renderBlock.aoBrightnessXYZNNP = renderBlock.aoBrightnessXZNP;
        } else {
          renderBlock.aoLightValueScratchXYZNNP = renderBlock.blockAccess.getBlock(x - 1, y - 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y - 1, z);
        }

        if (!flag3 && !flag4) {
          renderBlock.aoLightValueScratchXYZNPP = renderBlock.aoLightValueScratchXZNP;
          renderBlock.aoBrightnessXYZNPP = renderBlock.aoBrightnessXZNP;
        } else {
          renderBlock.aoLightValueScratchXYZNPP = renderBlock.blockAccess.getBlock(x - 1, y + 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y + 1, z);
        }

        if (!flag2 && !flag5) {
          renderBlock.aoLightValueScratchXYZPNP = renderBlock.aoLightValueScratchXZPP;
          renderBlock.aoBrightnessXYZPNP = renderBlock.aoBrightnessXZPP;
        } else {
          renderBlock.aoLightValueScratchXYZPNP = renderBlock.blockAccess.getBlock(x + 1, y - 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y - 1, z);
        }

        if (!flag2 && !flag4) {
          renderBlock.aoLightValueScratchXYZPPP = renderBlock.aoLightValueScratchXZPP;
          renderBlock.aoBrightnessXYZPPP = renderBlock.aoBrightnessXZPP;
        } else {
          renderBlock.aoLightValueScratchXYZPPP = renderBlock.blockAccess.getBlock(x + 1, y + 1, z)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y + 1, z);
        }

        if (renderBlock.renderMaxZ >= 1.0D) {
          --z;
        }

        i1 = l;

        if (renderBlock.renderMaxZ >= 1.0D || !renderBlock.blockAccess.getBlock(x, y, z + 1).isOpaqueCube()) {
          i1 = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z + 1);
        }

        f7 = renderBlock.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        f8 = (renderBlock.aoLightValueScratchXZNP + renderBlock.aoLightValueScratchXYZNPP + f7
            + renderBlock.aoLightValueScratchYZPP) / 4.0F;
        f9 = (f7 + renderBlock.aoLightValueScratchYZPP + renderBlock.aoLightValueScratchXZPP
            + renderBlock.aoLightValueScratchXYZPPP) / 4.0F;
        f10 = (renderBlock.aoLightValueScratchYZNP + f7 + renderBlock.aoLightValueScratchXYZPNP
            + renderBlock.aoLightValueScratchXZPP) / 4.0F;
        f11 = (renderBlock.aoLightValueScratchXYZNNP + renderBlock.aoLightValueScratchXZNP
            + renderBlock.aoLightValueScratchYZNP + f7) / 4.0F;
        brightTL = (float) ((double) f8 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMinX)
            + (double) f9 * renderBlock.renderMaxY * renderBlock.renderMinX
            + (double) f10 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinX
            + (double) f11 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinX));
        brightBL = (float) ((double) f8 * renderBlock.renderMinY * (1.0D - renderBlock.renderMinX)
            + (double) f9 * renderBlock.renderMinY * renderBlock.renderMinX
            + (double) f10 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMinX
            + (double) f11 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinX));
        brightBR = (float) ((double) f8 * renderBlock.renderMinY * (1.0D - renderBlock.renderMaxX)
            + (double) f9 * renderBlock.renderMinY * renderBlock.renderMaxX
            + (double) f10 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxX
            + (double) f11 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxX));
        brightTR = (float) ((double) f8 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxX)
            + (double) f9 * renderBlock.renderMaxY * renderBlock.renderMaxX
            + (double) f10 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxX
            + (double) f11 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxX));
        j1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXZNP, renderBlock.aoBrightnessXYZNPP,
            renderBlock.aoBrightnessYZPP, i1);
        k1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZPP, renderBlock.aoBrightnessXZPP,
            renderBlock.aoBrightnessXYZPPP, i1);
        l1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessYZNP, renderBlock.aoBrightnessXYZPNP,
            renderBlock.aoBrightnessXZPP, i1);
        i2 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYZNNP, renderBlock.aoBrightnessXZNP,
            renderBlock.aoBrightnessYZNP, i1);
        renderBlock.brightnessTopLeft = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            renderBlock.renderMaxY * (1.0D - renderBlock.renderMinX),
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinX),
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinX, renderBlock.renderMaxY * renderBlock.renderMinX);
        renderBlock.brightnessBottomLeft = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            renderBlock.renderMinY * (1.0D - renderBlock.renderMinX),
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinX),
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMinX, renderBlock.renderMinY * renderBlock.renderMinX);
        renderBlock.brightnessBottomRight = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            renderBlock.renderMinY * (1.0D - renderBlock.renderMaxX),
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxX),
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxX, renderBlock.renderMinY * renderBlock.renderMaxX);
        renderBlock.brightnessTopRight = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxX),
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxX),
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxX, renderBlock.renderMaxY * renderBlock.renderMaxX);

        if (usingNormalTexture) {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = mRed
              * 0.8F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = mGreen
              * 0.8F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = mBlue
              * 0.8F;
        } else {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = 0.8F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = 0.8F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = 0.8F;
        }

        renderBlock.colorRedTopLeft *= brightTL;
        renderBlock.colorGreenTopLeft *= brightTL;
        renderBlock.colorBlueTopLeft *= brightTL;
        renderBlock.colorRedBottomLeft *= brightBL;
        renderBlock.colorGreenBottomLeft *= brightBL;
        renderBlock.colorBlueBottomLeft *= brightBL;
        renderBlock.colorRedBottomRight *= brightBR;
        renderBlock.colorGreenBottomRight *= brightBR;
        renderBlock.colorBlueBottomRight *= brightBR;
        renderBlock.colorRedTopRight *= brightTR;
        renderBlock.colorGreenTopRight *= brightTR;
        renderBlock.colorBlueTopRight *= brightTR;

      } else if (normal == DirUtil.WEST_MX) {
        if (renderBlock.renderMinX <= 0.0D) {
          --x;
        }

        renderBlock.aoLightValueScratchXYNN = renderBlock.blockAccess.getBlock(x, y - 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXZNN = renderBlock.blockAccess.getBlock(x, y, z - 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXZNP = renderBlock.blockAccess.getBlock(x, y, z + 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXYNP = renderBlock.blockAccess.getBlock(x, y + 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z);
        renderBlock.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z - 1);
        renderBlock.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z + 1);
        renderBlock.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z);
        flag2 = renderBlock.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
        flag3 = renderBlock.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
        flag4 = renderBlock.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
        flag5 = renderBlock.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();

        if (!flag4 && !flag3) {
          renderBlock.aoLightValueScratchXYZNNN = renderBlock.aoLightValueScratchXZNN;
          renderBlock.aoBrightnessXYZNNN = renderBlock.aoBrightnessXZNN;
        } else {
          renderBlock.aoLightValueScratchXYZNNN = renderBlock.blockAccess.getBlock(x, y - 1, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z - 1);
        }

        if (!flag5 && !flag3) {
          renderBlock.aoLightValueScratchXYZNNP = renderBlock.aoLightValueScratchXZNP;
          renderBlock.aoBrightnessXYZNNP = renderBlock.aoBrightnessXZNP;
        } else {
          renderBlock.aoLightValueScratchXYZNNP = renderBlock.blockAccess.getBlock(x, y - 1, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z + 1);
        }

        if (!flag4 && !flag2) {
          renderBlock.aoLightValueScratchXYZNPN = renderBlock.aoLightValueScratchXZNN;
          renderBlock.aoBrightnessXYZNPN = renderBlock.aoBrightnessXZNN;
        } else {
          renderBlock.aoLightValueScratchXYZNPN = renderBlock.blockAccess.getBlock(x, y + 1, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z - 1);
        }

        if (!flag5 && !flag2) {
          renderBlock.aoLightValueScratchXYZNPP = renderBlock.aoLightValueScratchXZNP;
          renderBlock.aoBrightnessXYZNPP = renderBlock.aoBrightnessXZNP;
        } else {
          renderBlock.aoLightValueScratchXYZNPP = renderBlock.blockAccess.getBlock(x, y + 1, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z + 1);
        }

        if (renderBlock.renderMinX <= 0.0D) {
          ++x;
        }

        i1 = l;

        if (renderBlock.renderMinX <= 0.0D || !renderBlock.blockAccess.getBlock(x - 1, y, z).isOpaqueCube()) {
          i1 = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x - 1, y, z);
        }

        f7 = renderBlock.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        f8 = (renderBlock.aoLightValueScratchXYNN + renderBlock.aoLightValueScratchXYZNNP + f7
            + renderBlock.aoLightValueScratchXZNP) / 4.0F;
        f9 = (f7 + renderBlock.aoLightValueScratchXZNP + renderBlock.aoLightValueScratchXYNP
            + renderBlock.aoLightValueScratchXYZNPP) / 4.0F;
        f10 = (renderBlock.aoLightValueScratchXZNN + f7 + renderBlock.aoLightValueScratchXYZNPN
            + renderBlock.aoLightValueScratchXYNP) / 4.0F;
        f11 = (renderBlock.aoLightValueScratchXYZNNN + renderBlock.aoLightValueScratchXYNN
            + renderBlock.aoLightValueScratchXZNN + f7) / 4.0F;
        brightTL = (float) ((double) f9 * renderBlock.renderMaxY * renderBlock.renderMaxZ
            + (double) f10 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxZ)
            + (double) f11 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxZ)
            + (double) f8 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxZ);
        brightBL = (float) ((double) f9 * renderBlock.renderMaxY * renderBlock.renderMinZ
            + (double) f10 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMinZ)
            + (double) f11 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinZ)
            + (double) f8 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinZ);
        brightBR = (float) ((double) f9 * renderBlock.renderMinY * renderBlock.renderMinZ
            + (double) f10 * renderBlock.renderMinY * (1.0D - renderBlock.renderMinZ)
            + (double) f11 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinZ)
            + (double) f8 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMinZ);
        brightTR = (float) ((double) f9 * renderBlock.renderMinY * renderBlock.renderMaxZ
            + (double) f10 * renderBlock.renderMinY * (1.0D - renderBlock.renderMaxZ)
            + (double) f11 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxZ)
            + (double) f8 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxZ);
        j1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYNN, renderBlock.aoBrightnessXYZNNP,
            renderBlock.aoBrightnessXZNP, i1);
        k1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXZNP, renderBlock.aoBrightnessXYNP,
            renderBlock.aoBrightnessXYZNPP, i1);
        l1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXZNN, renderBlock.aoBrightnessXYZNPN,
            renderBlock.aoBrightnessXYNP, i1);
        i2 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYZNNN, renderBlock.aoBrightnessXYNN,
            renderBlock.aoBrightnessXZNN, i1);
        renderBlock.brightnessTopLeft = renderBlock.mixAoBrightness(k1, l1, i2, j1,
            renderBlock.renderMaxY * renderBlock.renderMaxZ, renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxZ),
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxZ),
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxZ);
        renderBlock.brightnessBottomLeft = renderBlock.mixAoBrightness(k1, l1, i2, j1,
            renderBlock.renderMaxY * renderBlock.renderMinZ, renderBlock.renderMaxY * (1.0D - renderBlock.renderMinZ),
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinZ),
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinZ);
        renderBlock.brightnessBottomRight = renderBlock.mixAoBrightness(k1, l1, i2, j1,
            renderBlock.renderMinY * renderBlock.renderMinZ, renderBlock.renderMinY * (1.0D - renderBlock.renderMinZ),
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinZ),
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMinZ);
        renderBlock.brightnessTopRight = renderBlock.mixAoBrightness(k1, l1, i2, j1,
            renderBlock.renderMinY * renderBlock.renderMaxZ, renderBlock.renderMinY * (1.0D - renderBlock.renderMaxZ),
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxZ),
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxZ);

        if (usingNormalTexture) {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = mRed
              * 0.6F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = mGreen
              * 0.6F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = mBlue
              * 0.6F;
        } else {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = 0.6F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = 0.6F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = 0.6F;
        }

        renderBlock.colorRedTopLeft *= brightTL;
        renderBlock.colorGreenTopLeft *= brightTL;
        renderBlock.colorBlueTopLeft *= brightTL;
        renderBlock.colorRedBottomLeft *= brightBL;
        renderBlock.colorGreenBottomLeft *= brightBL;
        renderBlock.colorBlueBottomLeft *= brightBL;
        renderBlock.colorRedBottomRight *= brightBR;
        renderBlock.colorGreenBottomRight *= brightBR;
        renderBlock.colorBlueBottomRight *= brightBR;
        renderBlock.colorRedTopRight *= brightTR;
        renderBlock.colorGreenTopRight *= brightTR;
        renderBlock.colorBlueTopRight *= brightTR;

      } else if (normal == DirUtil.EAST_PX) {
        if (renderBlock.renderMaxX >= 1.0D) {
          ++x;
        }

        renderBlock.aoLightValueScratchXYPN = renderBlock.blockAccess.getBlock(x, y - 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXZPN = renderBlock.blockAccess.getBlock(x, y, z - 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXZPP = renderBlock.blockAccess.getBlock(x, y, z + 1)
            .getAmbientOcclusionLightValue();
        renderBlock.aoLightValueScratchXYPP = renderBlock.blockAccess.getBlock(x, y + 1, z)
            .getAmbientOcclusionLightValue();
        renderBlock.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z);
        renderBlock.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z - 1);
        renderBlock.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y, z + 1);
        renderBlock.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z);
        flag2 = renderBlock.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
        flag3 = renderBlock.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
        flag4 = renderBlock.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
        flag5 = renderBlock.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();

        if (!flag3 && !flag5) {
          renderBlock.aoLightValueScratchXYZPNN = renderBlock.aoLightValueScratchXZPN;
          renderBlock.aoBrightnessXYZPNN = renderBlock.aoBrightnessXZPN;
        } else {
          renderBlock.aoLightValueScratchXYZPNN = renderBlock.blockAccess.getBlock(x, y - 1, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z - 1);
        }

        if (!flag3 && !flag4) {
          renderBlock.aoLightValueScratchXYZPNP = renderBlock.aoLightValueScratchXZPP;
          renderBlock.aoBrightnessXYZPNP = renderBlock.aoBrightnessXZPP;
        } else {
          renderBlock.aoLightValueScratchXYZPNP = renderBlock.blockAccess.getBlock(x, y - 1, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y - 1, z + 1);
        }

        if (!flag2 && !flag5) {
          renderBlock.aoLightValueScratchXYZPPN = renderBlock.aoLightValueScratchXZPN;
          renderBlock.aoBrightnessXYZPPN = renderBlock.aoBrightnessXZPN;
        } else {
          renderBlock.aoLightValueScratchXYZPPN = renderBlock.blockAccess.getBlock(x, y + 1, z - 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z - 1);
        }

        if (!flag2 && !flag4) {
          renderBlock.aoLightValueScratchXYZPPP = renderBlock.aoLightValueScratchXZPP;
          renderBlock.aoBrightnessXYZPPP = renderBlock.aoBrightnessXZPP;
        } else {
          renderBlock.aoLightValueScratchXYZPPP = renderBlock.blockAccess.getBlock(x, y + 1, z + 1)
              .getAmbientOcclusionLightValue();
          renderBlock.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x, y + 1, z + 1);
        }

        if (renderBlock.renderMaxX >= 1.0D) {
          --x;
        }

        i1 = l;

        if (renderBlock.renderMaxX >= 1.0D || !renderBlock.blockAccess.getBlock(x + 1, y, z).isOpaqueCube()) {
          i1 = block.getMixedBrightnessForBlock(renderBlock.blockAccess, x + 1, y, z);
        }

        f7 = renderBlock.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        f8 = (renderBlock.aoLightValueScratchXYPN + renderBlock.aoLightValueScratchXYZPNP + f7
            + renderBlock.aoLightValueScratchXZPP) / 4.0F;
        f9 = (renderBlock.aoLightValueScratchXYZPNN + renderBlock.aoLightValueScratchXYPN
            + renderBlock.aoLightValueScratchXZPN + f7) / 4.0F;
        f10 = (renderBlock.aoLightValueScratchXZPN + f7 + renderBlock.aoLightValueScratchXYZPPN
            + renderBlock.aoLightValueScratchXYPP) / 4.0F;
        f11 = (f7 + renderBlock.aoLightValueScratchXZPP + renderBlock.aoLightValueScratchXYPP
            + renderBlock.aoLightValueScratchXYZPPP) / 4.0F;
        brightTL = (float) ((double) f8 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxZ
            + (double) f9 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxZ)
            + (double) f10 * renderBlock.renderMinY * (1.0D - renderBlock.renderMaxZ)
            + (double) f11 * renderBlock.renderMinY * renderBlock.renderMaxZ);
        brightBL = (float) ((double) f8 * (1.0D - renderBlock.renderMinY) * renderBlock.renderMinZ
            + (double) f9 * (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinZ)
            + (double) f10 * renderBlock.renderMinY * (1.0D - renderBlock.renderMinZ)
            + (double) f11 * renderBlock.renderMinY * renderBlock.renderMinZ);
        brightBR = (float) ((double) f8 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinZ
            + (double) f9 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinZ)
            + (double) f10 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMinZ)
            + (double) f11 * renderBlock.renderMaxY * renderBlock.renderMinZ);
        brightTR = (float) ((double) f8 * (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxZ
            + (double) f9 * (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxZ)
            + (double) f10 * renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxZ)
            + (double) f11 * renderBlock.renderMaxY * renderBlock.renderMaxZ);
        j1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYPN, renderBlock.aoBrightnessXYZPNP,
            renderBlock.aoBrightnessXZPP, i1);
        k1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXZPP, renderBlock.aoBrightnessXYPP,
            renderBlock.aoBrightnessXYZPPP, i1);
        l1 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXZPN, renderBlock.aoBrightnessXYZPPN,
            renderBlock.aoBrightnessXYPP, i1);
        i2 = renderBlock.getAoBrightness(renderBlock.aoBrightnessXYZPNN, renderBlock.aoBrightnessXYPN,
            renderBlock.aoBrightnessXZPN, i1);
        renderBlock.brightnessTopLeft = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMaxZ,
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMaxZ),
            renderBlock.renderMinY * (1.0D - renderBlock.renderMaxZ), renderBlock.renderMinY * renderBlock.renderMaxZ);
        renderBlock.brightnessBottomLeft = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            (1.0D - renderBlock.renderMinY) * renderBlock.renderMinZ,
            (1.0D - renderBlock.renderMinY) * (1.0D - renderBlock.renderMinZ),
            renderBlock.renderMinY * (1.0D - renderBlock.renderMinZ), renderBlock.renderMinY * renderBlock.renderMinZ);
        renderBlock.brightnessBottomRight = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMinZ,
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMinZ),
            renderBlock.renderMaxY * (1.0D - renderBlock.renderMinZ), renderBlock.renderMaxY * renderBlock.renderMinZ);
        renderBlock.brightnessTopRight = renderBlock.mixAoBrightness(j1, i2, l1, k1,
            (1.0D - renderBlock.renderMaxY) * renderBlock.renderMaxZ,
            (1.0D - renderBlock.renderMaxY) * (1.0D - renderBlock.renderMaxZ),
            renderBlock.renderMaxY * (1.0D - renderBlock.renderMaxZ), renderBlock.renderMaxY * renderBlock.renderMaxZ);

        if (usingNormalTexture) {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = mRed
              * 0.6F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = mGreen
              * 0.6F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = mBlue
              * 0.6F;
        } else {
          renderBlock.colorRedTopLeft = renderBlock.colorRedBottomLeft = renderBlock.colorRedBottomRight = renderBlock.colorRedTopRight = 0.6F;
          renderBlock.colorGreenTopLeft = renderBlock.colorGreenBottomLeft = renderBlock.colorGreenBottomRight = renderBlock.colorGreenTopRight = 0.6F;
          renderBlock.colorBlueTopLeft = renderBlock.colorBlueBottomLeft = renderBlock.colorBlueBottomRight = renderBlock.colorBlueTopRight = 0.6F;
        }

        renderBlock.colorRedTopLeft *= brightTL;
        renderBlock.colorGreenTopLeft *= brightTL;
        renderBlock.colorBlueTopLeft *= brightTL;
        renderBlock.colorRedBottomLeft *= brightBL;
        renderBlock.colorGreenBottomLeft *= brightBL;
        renderBlock.colorBlueBottomLeft *= brightBL;
        renderBlock.colorRedBottomRight *= brightBR;
        renderBlock.colorGreenBottomRight *= brightBR;
        renderBlock.colorBlueBottomRight *= brightBR;
        renderBlock.colorRedTopRight *= brightTR;
        renderBlock.colorGreenTopRight *= brightTR;
        renderBlock.colorBlueTopRight *= brightTR;

      }
    }
    // Tessellate
    for (int i = 0; i < vertices.length; i++) {
      Vertex vert = vertices[i];
      vRot = rotate(vert, rotation, rotationAxis, vRot);
      vRot = rotate(vRot, metaRotate, DirUtil.UP_PY, vRot2);
      TextureCoordinate t = tCoords[i];

      int corner = determineVertexCorner(vRot, normal);
      double u = icon.getInterpolatedU(t.u * 16);
      double v = icon.getInterpolatedV(t.v * 16);
      if (Minecraft.isAmbientOcclusionEnabled()) {
        switch (corner) {
        case 0:
          tessellator.setColorOpaque_F(renderBlock.colorRedTopLeft, renderBlock.colorGreenTopLeft,
              renderBlock.colorBlueTopLeft);
          tessellator.setBrightness(renderBlock.brightnessTopLeft);
          break;
        case 1:
          tessellator.setColorOpaque_F(renderBlock.colorRedBottomLeft, renderBlock.colorGreenBottomLeft,
              renderBlock.colorBlueBottomLeft);
          tessellator.setBrightness(renderBlock.brightnessBottomLeft);
          break;
        case 2:
          tessellator.setColorOpaque_F(renderBlock.colorRedBottomRight, renderBlock.colorGreenBottomRight,
              renderBlock.colorBlueBottomRight);
          tessellator.setBrightness(renderBlock.brightnessBottomRight);
          break;
        case 3:
          tessellator.setColorOpaque_F(renderBlock.colorRedTopRight, renderBlock.colorGreenTopRight,
              renderBlock.colorBlueTopRight);
          tessellator.setBrightness(renderBlock.brightnessTopRight);

        }
      } else {
        tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.setBrightness(l);
      }

      tessellator.addVertexWithUV(vRot.x, vRot.y, vRot.z, u, v);
    }

    renderBlock.enableAO = false;
    return true;
  }
}
