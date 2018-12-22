package com.tntp.mnm.api.security;

import com.tntp.mnm.tileentity.STile;
import com.tntp.mnm.util.SecurityUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A class used for tile security
 * 
 * @author iTNTPiston
 *
 */
public class Security {
  private STile owner;
  private boolean secured;
  private int securityCode;

  public Security(STile tile) {
    owner = tile;
  }

  public boolean securityCheck(ItemStack using) {
    if (!isSecured())
      return true;
    return SecurityUtil.matches(using, securityCode);
  }

  public boolean isSecured() {
    return secured;
  }

  public void resetSecurity() {
    secured = false;
    owner.markDirty();
  }

  public void setSecurityCode(int code) {
    if (!secured) {
      securityCode = code;
      secured = true;
    }
  }

  public void writeToNBT(NBTTagCompound tag) {
    tag.setBoolean("security_secured", secured);
    tag.setInteger("security_code", securityCode);
  }

  public void readFromNBT(NBTTagCompound tag) {
    secured = tag.getBoolean("security_secured");
    securityCode = tag.getInteger("securty_code");
  }

}
