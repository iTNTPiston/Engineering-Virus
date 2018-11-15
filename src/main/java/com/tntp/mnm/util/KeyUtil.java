package com.tntp.mnm.util;

import org.lwjgl.input.Keyboard;

public class KeyUtil {
  public static boolean isCtrlDown() {
    return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
  }
}
