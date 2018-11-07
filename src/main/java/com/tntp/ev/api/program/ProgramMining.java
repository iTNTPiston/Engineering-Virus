package com.tntp.ev.api.program;

import com.tntp.ev.api.IDisk;
import com.tntp.ev.api.IMemory;
import com.tntp.ev.api.IProgram;
import com.tntp.ev.init.EVItems;
import com.tntp.ev.util.RandomUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ProgramMining implements IProgram {

  @Override
  public int execCycle(IMemory mem, IDisk disk, IInventory inv) {
    int error = 0;
    double x = RandomUtil.RAND2.nextDouble();
    if (x < 0.002) {
      error++;
    } else if (x > 0.95) {
      if (!mem.canAccess(this)) {
        error--;
      } else {
        Object m = mem.access(this, "basicMining");
        if (m == null) {
          m = 1;
        } else if (m instanceof Integer) {
          m = (Integer) m + 1;
        } else {
          error += 64;
          return error;
        }
        if ((((Integer) m) & 7) == (RandomUtil.RANDS.nextInt() & 7)) {
          if (!disk.canAccess(this)) {
            error--;
          } else {
            boolean b = disk.writeItemStack(new ItemStack(EVItems.itemByteCoin));
            if (!b) {
              error += 16;
            }
            m = 0;
          }
        }
        mem.memWrite(this, "basicMining", m);
      }
    }
    return error;
  }

  @Override
  public int authority() {
    return 2;
  }

}
