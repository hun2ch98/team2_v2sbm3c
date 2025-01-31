package dev.mvc.participants;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.surveygood.SurveygoodVO;

public interface PartDAOInter {
  
  /**
   * 등록
   */
  public int create(PartVO partVO);
  
  /**
   * 추천수 증가
   */
  public int update_cnt(int itemno);
  
  /**
   * 특정 개수 산출
   */
  public int updateCnt(int itemno);
  
  /**
   * 목록
   */
  public ArrayList<PartVO> list_all();
  
  /**
   * 삭제
   */
  public int delete(int pno);
  
  /**
   * 조회
   */
  public PartVO read(int pno);
  
  /**
   * surveyno, memberno로 조회
   */
  public PartVO readByitemmember(HashMap<String, Object> map);
  
  /**
   * 3개 조인
   */
  public ArrayList<ItemMemberPartVO> list_all_join();
  
  /**
   * 검색 개수
   */
  public int count_search(HashMap<String, Object> Map);
  
  /**
   * 검색 + 페이징
   */
  public ArrayList<ItemMemberPartVO> list_search_paging(HashMap<String, Object> Map);

}
