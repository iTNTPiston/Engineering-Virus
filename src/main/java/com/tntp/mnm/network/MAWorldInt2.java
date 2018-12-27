package com.tntp.mnm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public abstract class MAWorldInt2<M extends MAWorldInt1<M>> extends MAWorldInt1<M> {
  private int int2;

  public MAWorldInt2(int dim, int x, int y, int z, int i1, int i2) {
    super(dim, x, y, z, i1);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    int2 = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeInt(int2);
  }

  public int getInt2() {
    return int2;
  }

  public int getI2() {
    return int2;
  }

}
