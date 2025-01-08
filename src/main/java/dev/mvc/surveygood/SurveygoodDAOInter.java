package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveygoodDAOInter {
  
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
  
  /**
   * 특정 개수 산출
   * @param map
   * @return
   */
  public int heartCnt(HashMap<String, Object> map);
  
  /**
   * 조회
   * @param goodno
   * @return
   */
  public SurveygoodVO read(int goodno);
  
  /**
   * surveyno, memberno로 조회
   * @param map
   * @return
   */
  public SurveygoodVO readBysurveymember(HashMap<String, Object> map);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<SurveySurveygoodMemberVO> list_all_join();

}
