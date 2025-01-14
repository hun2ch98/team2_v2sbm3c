package dev.mvc.participants;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.surveygood.SurveygoodVO;

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
  public PartVO read(int pno) {
    PartVO partVO = this.partDAO.read(pno);
    return partVO;
  }
  
  @Override
  public PartVO readByitemmember(HashMap<String, Object> map) {
    PartVO partVO = this.partDAO.readByitemmember(map);
    return partVO;
  }

  @Override
  public ArrayList<ItemMemberPartVO> list_all_join() {
    ArrayList<ItemMemberPartVO> list = this.partDAO.list_all_join();
    return list;
  }



}
