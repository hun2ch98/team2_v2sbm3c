package dev.mvc.surveygood;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.surveygood.SurveygoodProc")
public class SurveygoodProc implements SurveygoodProcInter {
  @Autowired
  SurveygoodDAOInter surveygoodDAO;

  @Override
  public int create(SurveygoodVO surveygoodVO) {
    int cnt = this.surveygoodDAO.create(surveygoodVO);
    return cnt;
  }
  
  @Override
  public ArrayList<SurveygoodVO> list_all(){
    ArrayList<SurveygoodVO> list = this.surveygoodDAO.list_all();
    return list;
  }
  
  @Override
  public int delete(int goodno) {
    int cnt = this.surveygoodDAO.delete(goodno);
    return cnt;
  }

}
