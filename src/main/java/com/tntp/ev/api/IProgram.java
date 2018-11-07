package com.tntp.ev.api;

import net.minecraft.inventory.IInventory;

/**
 * Defines a program
 * 
 * @author iTNTPiston
 *
 */
public interface IProgram {
  /**
   * 
   * @param mem
   * @param disk
   * @param inv
   * @return errors that happened
   */
  public int execCycle(IMemory mem, IDisk disk, IInventory inv);

  public int authority();
}
