package dev.mvc.surveyitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.surveyitem.ItemProc")
public class ItemProc implements ItemProcInter{
  
  @Autowired
  private ItemDAOInter itemDAO;
  
  public ItemProc() {
    System.out.println("-> ItemProc created.");
  }
  
  @Override
  public int create(ItemVO itemVO) {
    int cnt = this.itemDAO.create(itemVO);
    return cnt;
  }
  
  @Override
  public ItemVO read(int itemno) {
    ItemVO itemVO = this.itemDAO.read(itemno);
    return itemVO;
  }
  
  @Override
  public int update(ItemVO itemVO) {
    int cnt = this.itemDAO.update(itemVO);
    return cnt;
  }
  
  @Override
  public int delete(int itemno) {
    int cnt = this.itemDAO.delete(itemno);
    return cnt;
  }

}
