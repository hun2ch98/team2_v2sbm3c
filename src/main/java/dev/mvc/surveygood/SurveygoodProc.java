package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

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

  @Override
  public int heartCnt(HashMap<String, Object> map) {
    int cnt = this.surveygoodDAO.heartCnt(map);
    return cnt;
  }

  @Override
  public SurveygoodVO read(int goodno) {
    SurveygoodVO surveygoodVO = this.surveygoodDAO.read(goodno);
    return surveygoodVO;
  }
  
  @Override
  public SurveygoodVO read(HashMap<String, Object> map) {
    SurveygoodVO surveygoodVO = this.surveygoodDAO.read(map);
    return surveygoodVO;
  }
  
}
