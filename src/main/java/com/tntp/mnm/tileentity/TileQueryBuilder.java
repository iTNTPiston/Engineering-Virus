package com.tntp.mnm.tileentity;

import com.tntp.mnm.api.db.GroupSearchResult;
import com.tntp.mnm.api.db.Mainframe;
import com.tntp.mnm.api.db.MainframeTPQuery;
import com.tntp.mnm.api.db.QueryExecuter;

import net.minecraft.item.ItemStack;

public class TileQueryBuilder extends STileNeithernetInventory {

  private String currentGroupName;
  private int currentFirstGroupIndex;
  private int currentScrollIndex;

  private ItemStack[] cachedGroupList;
  private ItemStack[] cachedItemGroup;

  public TileQueryBuilder() {
    super(12);// 0-8 inventory, 9,10,11 chip,12-26 display
  }

  public void scroll(int direction) {

  }

  public void selectGroup(String str) {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      GroupSearchResult result = mf.getGroup(str);
      cachedGroupList = new ItemStack[result.getGroupChipList().size()];
      result.getGroupChipList().toArray(cachedGroupList);
      cachedItemGroup = new ItemStack[result.getGroupItems().size()];
      result.getGroupItems().toArray(cachedItemGroup);
      currentGroupName = str;
    } else {
      cachedGroupList = null;
      cachedItemGroup = null;
      currentGroupName = "";
    }
  }

  public void executePut() {
    Mainframe mf = connectToMainframe();
    if (mf != null) {
      MainframeTPQuery query = new MainframeTPQuery();
      query.addTP(-1, 0);
      QueryExecuter qe = new QueryExecuter(query, this, 0, 8);
      mf.sendQueryToCPU(qe);
    }
  }

}
