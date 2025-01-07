package dev.mvc.surveygood;

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

}
