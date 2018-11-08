package com.tntp.ev.tileentity;

import com.tntp.ev.api.power.IElectron;
import com.tntp.ev.api.power.ITileWire;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class STileSuperWire extends TileEntity implements ITileWire {
  /**
   * How many electrons can it hold
   */
  private int capacity;
  /**
   * How many electrons can be sent at a time
   */
  private int crossSect;
  /**
   * How long can an electron go without losing energy
   */
  private int loss;
  private boolean[] receiveSide;

  public STileSuperWire(int cap, int crossSection, int lossDistance) {
    capacity = cap;
    crossSect = crossSection;
    loss = lossDistance;
    receiveSide = new boolean[6];
  }

  @Override
  public int getEnergyLossDistance() {
    return loss;
  }

  @Override
  public int getCapacity() {
    return capacity;
  }

  @Override
  public int getCrossSection() {
    return crossSect;
  }

  public boolean isInputSideInThisTick(int side) {
    return receiveSide[side];
  }

  @Override
  public void receiveElectron(IElectron e, int side) {
    receiveSide[side] = true;
    markDirty();
  }

  public void updateEntity() {
    if (this.worldObj == null)
      return;
    if (!this.worldObj.isRemote) {
      // ready to send electron
      for (int i = 0; i < crossSect; i++) {
        if (canSendElectron()) {
          int outputSide = decideOutputSide();
          if (outputSide == -1)
            break;

          ITileWire wire = getAdjacentWire(outputSide);

          IElectron e = sendElectron();
          markDirty();
          if (e != null) {
            wire.receiveElectron(e, outputSide ^ 1);
            e.onTransfered(this, wire);
          }
        }
      }
      for (int i = 0; i < 6; i++)
        receiveSide[i] = false;
    }
  }

  public ITileWire getAdjacentWire(int dir) {
    ForgeDirection fDir = ForgeDirection.VALID_DIRECTIONS[dir];
    int aX = xCoord + fDir.offsetX;
    int aY = yCoord + fDir.offsetY;
    int aZ = zCoord + fDir.offsetZ;
    TileEntity te = worldObj.getTileEntity(aX, aY, aZ);
    if (te instanceof ITileWire)
      return (ITileWire) te;
    else
      return null;
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setInteger("cap", capacity);
    tag.setInteger("cross", crossSect);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    capacity = tag.getInteger("cap");
    crossSect = tag.getInteger("cross");
  }

  public boolean canWireConnect(int side) {
    return isInputSide(side) || isOutputSide(side);
  }

}
