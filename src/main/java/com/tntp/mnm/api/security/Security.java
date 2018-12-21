package com.tntp.mnm.api.security;

import com.tntp.mnm.tileentity.STile;
import com.tntp.mnm.util.SecurityUtil;

import net.minecraft.item.ItemStack;

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

}
