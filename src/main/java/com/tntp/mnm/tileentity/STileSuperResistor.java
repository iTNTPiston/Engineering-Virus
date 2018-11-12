package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.power.IElectron;
import com.tntp.mnm.api.power.ITileResistor;

import net.minecraft.nbt.NBTTagCompound;

public abstract class STileSuperResistor extends STileSuperCable implements ITileResistor {

  private int power;
  private int stored;
  private int heat;
  private int sHeat;
  private int energyDissipated;
  private boolean working;

  public STileSuperResistor(int cap, int crossSection, int power, int sHeat) {
    super(cap, crossSection, -1);
    this.power = power;
    this.sHeat = sHeat;
    heat = 0;
    stored = 0;
    energyDissipated = 0;
  }

  @Override
  public IElectron sendElectron() {
    IElectron send = super.sendElectron();
    send.passResistor(this);
    return send;
  }

  @Override
  public int getPower() {
    return power;
  }

  @Override
  public int getHeat() {
    return heat;
  }

  /**
   * how much heat can be raised by 1j
   */
  @Override
  public int getSpecificHeat() {
    return sHeat;
  }

  @Override
  public void dissipate(int energy) {
    stored += energy;
    System.out.println("charge");
    System.out.println("E=" + stored);
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setInteger("p", power);
    tag.setInteger("storedP", stored);
    tag.setInteger("heat", heat);
    tag.setInteger("specific", sHeat);
    tag.setInteger("ed", energyDissipated);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    power = tag.getInteger("p");
    stored = tag.getInteger("storedP");
    heat = tag.getInteger("heat");
    sHeat = tag.getInteger("specific");
    energyDissipated = tag.getInteger("ed");
  }

  public void updateEntity() {
    if (worldObj != null && !worldObj.isRemote) {
      super.updateEntity();
      loseHeatToEnvironment();
      System.out.println("tick");
      System.out.println("E=" + stored);
      if (stored >= power) {
        if (!working) {
          aquirePower();
          working = true;
        }
        System.out.println("work");
        work();
        stored -= power;
        System.out.println("E=" + stored);
      } else {
        System.out.println("lose");
        if (working) {
          losePower();
          working = false;
        }

        System.out.println("E=" + stored);

      }
      int maxStored = power * 20;
      if (stored > maxStored) {
        System.out.println("dissipate");
        int excess = stored - maxStored;
        stored = maxStored;
        System.out.println("E=" + stored);
        energyDissipated += excess;
        if (energyDissipated >= sHeat) {
          heat++;
          energyDissipated -= sHeat;
        } else if (energyDissipated < 0) {
          if (heat <= 0) {
            energyDissipated = 0;
            heat = 0;
          } else {
            heat--;
            energyDissipated = sHeat + energyDissipated;
          }
        }
      }
    }
  }

  @Override
  public void loseHeatToEnvironment() {
    // TODO Auto-generated method stub

  }

  public boolean isWorking() {
    return working;
  }

}