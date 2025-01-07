package dev.mvc.surveygood;

import java.util.ArrayList;

public interface SurveygoodProcInter {
  
  /**
   * 등록
   * @param surveygoodVO
   * @return
   */
  public int create(SurveygoodVO surveygoodVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<SurveygoodVO> list_all();
  
  /**
   * 삭제
   * @param goodno
   * @return
   */
  public int delete(int goodno);

}
