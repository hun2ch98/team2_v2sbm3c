package dev.mvc.participants;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.participants.PartProc")
public class PartProc implements PartProcInter {
  
  @Autowired
  PartDAOInter partDAO;

  @Override
  public int create(PartVO partVO) {
    int cnt = this.partDAO.create(partVO);
    return cnt;
  }
  
  @Override
  public int update_cnt(int itemno) {
    int cnt = this.partDAO.update_cnt(itemno);
    return cnt;
  }

  @Override
  public ArrayList<PartVO> list_all() {
    ArrayList<PartVO> list = this.partDAO.list_all();
    return list;
  }

  @Override
  public int delete(int pno) {
    int cnt = this.partDAO.delete(pno);
    return cnt;
  }

  @Override
  public ArrayList<ItemMemberPartVO> list_all_join() {
    ArrayList<ItemMemberPartVO> list = this.partDAO.list_all_join();
    return list;
  }



}
