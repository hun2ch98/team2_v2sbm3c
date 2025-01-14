package dev.mvc.participants;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.surveygood.SurveygoodVO;

public interface PartDAOInter {
  
  /**
   * 등록
   * @param partVO
   * @return
   */
  public int create(PartVO partVO);
  
  /**
   * 추천수 증가
   * @return
   */
  public int update_cnt(int itemno);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<PartVO> list_all();
  
  /**
   * 삭제
   * @param pno
   * @return
   */
  public int delete(int pno);
  
  /**
   * 조회
   * @param goodno
   * @return
   */
  public PartVO read(int pno);
  
  /**
   * surveyno, memberno로 조회
   * @param map
   * @return
   */
  public PartVO readByitemmember(HashMap<String, Object> map);
  
  /**
   * 3개 조인
   * @return
   */
  public ArrayList<ItemMemberPartVO> list_all_join();

}
