package com.tntp.mnm.tileentity;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

/**
 * Network Mainframe controller
 * 
 * @author iTNTPiston
 *
 */
public class TileCentralProcessor extends TileEntity {
  private int computingPower;// power of the cpu
  private int xSize;
  private int ySize;
  private int zSize;
  private int rescanCD;
  private boolean formed;
  private int energy;
  private int maxEnergy;
  private int energyGen;
  private List<Integer> security;

}
