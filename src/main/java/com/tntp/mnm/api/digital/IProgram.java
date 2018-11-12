package com.tntp.mnm.api.digital;

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

  /**
   * Authority of this program 0=nothing, 1=memory, 2=disk,4=inventory,
   * 8=processor, 16=computer
   * 
   * @return
   */
  public int authority();
}
