package com.tntp.ev.api.power;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Energy carrier
 * 
 * @author zhaoy
 *
 */
public interface IElectron {
  public int getCarriedEnergy();

  public void setCarriedEnergy(int j);

  public void writeToNBT(NBTTagCompound nbt);

  public void readFromNBT(NBTTagCompound nbt);

  public void onTransfered(ITileWire from, ITileWire to);

  /**
   * Used for dissipating excessive energy
   * 
   * @return
   */
  public List<ITileResistor> getResistorsPassed();

  public void passResistor(ITileResistor resistor);
}
