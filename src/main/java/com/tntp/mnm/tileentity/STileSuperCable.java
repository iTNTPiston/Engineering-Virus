package com.tntp.ev.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;

import com.tntp.ev.api.power.ElectronTypes;
import com.tntp.ev.api.power.IElectron;
import com.tntp.ev.api.power.ITileWire;
import com.tntp.ev.util.ConstUtil;
import com.tntp.ev.util.RandomUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class STileSuperCable extends STileSuperWire {

  private static ArrayList<Integer> cacheAvailableSides = new ArrayList<Integer>(6);
  private LinkedList<IElectron> electrons;

  public STileSuperCable() {
    this(0, 0, 0);
  }

  public STileSuperCable(int cap, int crossSect, int loss) {
    super(cap, crossSect, loss);
    electrons = new LinkedList<IElectron>();
  }

  @Override
  public IElectron getElectron(int i) {
    return electrons.get(i);
  }

  @Override
  public double getPotential(int side) {
    if (getCapacity() == 0 || electrons.isEmpty())
      return 0;
    double j = 0;
    for (IElectron e : electrons) {
      if (e != null) {
        j += e.getCarriedEnergy();
      }
    }
    return j / getCapacity();

  }

  @Override
  public int[] getOutputSides() {
    return ConstUtil.D_ALL;
  }

  @Override
  public int[] getInputSides() {
    return ConstUtil.D_ALL;
  }

  @Override
  public boolean isOutputSide(int i) {
    return true;
  }

  @Override
  public boolean isInputSide(int i) {
    return true;
  }

  @Override
  public IElectron getFirstElectron() {
    return electrons.getFirst();
  }

  @Override
  public IElectron getLastElectron() {
    return electrons.getLast();
  }

  @Override
  public boolean hasSpaceForMoreElectron() {
    return electrons.size() < getCapacity();
  }

  @Override
  public IElectron sendElectron() {
    IElectron e = electrons.removeFirst();
    markDirty();
    return e;
  }

  @Override
  public void receiveElectron(IElectron toBeRecept, int side) {
    super.receiveElectron(toBeRecept, side);
    electrons.addLast(toBeRecept);
    markDirty();
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    NBTTagList elec = new NBTTagList();
    int i = 0;
    for (IElectron e : electrons) {
      NBTTagCompound com = new NBTTagCompound();
      com.setShort("i", (short) i);
      ElectronTypes.writeElectronToNBT(e, com);
      elec.appendTag(com);
    }
    tag.setTag("electrons", elec);

  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    IElectron[] arr = new IElectron[getCapacity()];
    electrons = new LinkedList<IElectron>();
    NBTTagList list = tag.getTagList("electrons", 10);
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound com = list.getCompoundTagAt(i);
      int id = com.getShort("i");
      IElectron e = ElectronTypes.createAndLoadElectronFromNBT(com);
      arr[id] = e;
    }
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] != null) {
        electrons.add(arr[i]);
      }
    }
  }

  @Override
  public int decideOutputSide() {
    cacheAvailableSides.clear();
    int[] outputSides = getOutputSides();
    for (int o = 0; o < outputSides.length; o++) {
      int i = outputSides[o];
      ITileWire wire = this.getAdjacentWire(i);
      if (!isInputSideInThisTick(i)) {
        if (wire != null) {
          if (wire.isInputSide(i ^ 1) && wire.hasSpaceForMoreElectron()) {
            if (wire.getPotential(i ^ 1) < getPotential(i)) {
              cacheAvailableSides.add(i);
            }
          }
        }
      }
    }
    if (cacheAvailableSides.isEmpty())
      return -1;
    int rand = RandomUtil.RAND.nextInt(cacheAvailableSides.size());
    return cacheAvailableSides.get(rand);
  }

  @Override
  public boolean canSendElectron() {
    return !electrons.isEmpty();
  }

}
