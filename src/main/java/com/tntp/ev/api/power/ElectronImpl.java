package com.tntp.ev.api.power;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class ElectronImpl implements IElectron {
  private int distance;
  private int energy;
  private List<ITileResistor> resistorPassed;

  public ElectronImpl() {
    this(0);
  }

  public ElectronImpl(int j) {
    energy = j;
    resistorPassed = new LinkedList<ITileResistor>();
  }

  @Override
  public int getCarriedEnergy() {
    return energy;
  }

  @Override
  public void setCarriedEnergy(int j) {
    energy = j;

  }

  @Override
  public void writeToNBT(NBTTagCompound nbt) {
    nbt.setInteger("eng", energy);
    nbt.setInteger("dist", distance);

  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    energy = nbt.getInteger("eng");
    distance = nbt.getInteger("dist");

  }

  @Override
  public void onTransfered(ITileWire from, ITileWire to) {
    distance++;
    if (energy > 0) {
      int eld = to.getEnergyLossDistance();
      if (eld <= distance && eld > 0) {
        distance -= eld;
        energy--;
      }
    }
  }

  @Override
  public List<ITileResistor> getResistorsPassed() {
    return resistorPassed;
  }

  @Override
  public void passResistor(ITileResistor resistor) {
    resistorPassed.add(resistor);

  }

}
