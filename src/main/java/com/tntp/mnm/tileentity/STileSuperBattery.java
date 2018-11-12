package com.tntp.mnm.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tntp.mnm.api.power.IElectron;
import com.tntp.mnm.api.power.ITileBattery;
import com.tntp.mnm.api.power.ITileResistor;
import com.tntp.mnm.api.power.ITileWire;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class STileSuperBattery extends STileSuperWire implements ITileBattery {
  private int[] positive;
  private int[] negative;
  private int maxEnergy;
  private int energy;
  private int emf;
  private int firstElectronEnergy;

  public STileSuperBattery(int crossSection, int emf, int maxEnergy, int currentEnergy, int posSide, int negSide) {
    super(0, crossSection, -1);
    this.emf = emf;
    this.maxEnergy = maxEnergy;
    setCurrentEnergy(currentEnergy);
    positive = new int[] { posSide };
    negative = new int[] { negSide };
  }

  @Override
  public IElectron getFirstElectron() {
    return null;
  }

  @Override
  public IElectron getLastElectron() {
    return null;
  }

  @Override
  public IElectron getElectron(int i) {
    return null;
  }

  @Override
  public boolean hasSpaceForMoreElectron() {
    return firstElectronEnergy == 0;
  }

  @Override
  public int decideOutputSide() {
    int i = positive[0];
    ITileWire wire = this.getAdjacentWire(i);
    if (wire != null) {
      if (wire.isInputSide(i ^ 1) && wire.hasSpaceForMoreElectron()) {
        if (wire.getPotential(i ^ 1) <= getPotential(i)) {
          return positive[0];
        }
      }
    }
    return -1;
  }

  @Override
  public IElectron sendElectron() {
    energy -= emf;
    markDirty();
    return produceElectron();
  }

  @Override
  public void receiveElectron(IElectron toBeRecept, int side) {
    destroyElectron(toBeRecept);
    markDirty();
  }

  public double getPotential(int side) {
    if (isOutputSide(side)) {
      return getEMF();
    } else {
      return 0;
    }

  }

  @Override
  public int[] getOutputSides() {
    return positive;
  }

  @Override
  public int[] getInputSides() {
    return negative;
  }

  @Override
  public boolean isOutputSide(int i) {
    return i == positive[0];
  }

  @Override
  public boolean isInputSide(int i) {
    return i == negative[0];
  }

  @Override
  public int getMaxEnergy() {
    return maxEnergy;
  }

  @Override
  public int getCurrentEnergy() {
    return energy;
  }

  @Override
  public void setCurrentEnergy(int e) {
    energy = Math.min(e, getMaxEnergy());
    markDirty();
  }

  public int getEMF() {
    return emf;
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setInteger("pos", positive[0]);
    tag.setInteger("neg", negative[0]);
    tag.setInteger("maxEng", maxEnergy);
    tag.setInteger("eng", energy);
    tag.setInteger("emf", emf);
    tag.setInteger("FEE", firstElectronEnergy);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    positive = new int[] { tag.getInteger("pos") };
    negative = new int[] { tag.getInteger("neg") };
    maxEnergy = tag.getInteger("maxEng");
    energy = tag.getInteger("eng");
    emf = tag.getInteger("emf");
    firstElectronEnergy = tag.getInteger("FEE");
  }

  @Override
  public void destroyElectron(IElectron e) {
    if (e.getCarriedEnergy() > 0) {
      List<ITileResistor> passed = e.getResistorsPassed();
      if (passed == null || passed.isEmpty()) {
        firstElectronEnergy += e.getCarriedEnergy();
      } else {
        int[] dissipate = new int[passed.size()];
        for (int i = 0; i < dissipate.length; i++) {
          dissipate[i] = e.getCarriedEnergy() / dissipate.length;
        }
        int remainder = e.getCarriedEnergy() - dissipate[0] * dissipate.length;
        ArrayList<Integer> adding = new ArrayList<Integer>();
        for (int i = 0; i < dissipate.length; i++) {
          adding.add(i);
        }
        Collections.shuffle(adding);
        for (int i = 0; i < remainder; i++) {
          dissipate[adding.get(i)]++;
        }
        int ri = 0;
        for (ITileResistor r : passed) {
          r.dissipate(dissipate[ri]);
          ri++;
        }
      }
    }
  }

  public int produceEMF() {
    int effectiveEMF = emf + firstElectronEnergy;
    firstElectronEnergy = 0;
    markDirty();
    return effectiveEMF;
  }

  public boolean canSendElectron() {
    return energy > emf;
  }

  public void setPositiveSide(int side) {
    positive[0] = side;
    markDirty();
  }

  public void setNegativeSide(int side) {
    negative[0] = side;
    markDirty();
  }

}
