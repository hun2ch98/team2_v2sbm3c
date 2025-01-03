package dev.mvc.surveyitem;

import java.util.ArrayList;

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
  public ArrayList<ItemVO> list_all_com(int surveyno){
    ArrayList<ItemVO> list = this.itemDAO.list_all_com(surveyno);
    return list;
  }
  
  @Override
  public ArrayList<ItemVO> admin_list(int surveyno){
    ArrayList<ItemVO> list = this.itemDAO.admin_list(surveyno);
    return list;
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
  
  @Override
  public int update_cnt(int itemVO) {
    int cnt = this.itemDAO.update_cnt(itemVO);
    return cnt;
  }

}
