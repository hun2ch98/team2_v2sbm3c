package dev.mvc.surveyitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface ItemDAOInter {
  
  /**
   * 등록
   * @param itemVO
   * @return
   */
  public int create(ItemVO itemVO);
  
  /**
   * 조회
   * @param itemno
   * @return
   */
  public ItemVO read(int itemno);
  
  /**
   * 회원
   * 목록
   * @return
   */
  public ArrayList<ItemVO> list_all_com(int surveyno);
  
  /**
   * 수정
   * @param itemVO
   * @return
   */
  public int update(ItemVO itemVO);
  
  /**
   * 삭제
   * @param itemVO
   * @return
   */
  public int delete(int itemVO);
  
  /**
   * 회원 참여수
   * @param itemVO
   * @return
   */
  public int update_cnt(int itemno);
  
  /**
   * 검색 목록
   * @return
   */
  public ArrayList<ItemVO> list_search(int surveyno, String word);
  
  /**
   * 검색 갯수
   * @param word
   * @return
   */
  public int count_by_search(String word);
  
  /**
   * 검색 + 페이징 목록
   * @param map
   * @return
   */
  public ArrayList<ItemVO> list_search_paging(Map<String, Object> map);
  
  /**
   * 특정 카테고리에 속한 레코드 갯수 산출
   * @param surveyno
   * @return
   */
  public int count_survey(int surveyno);
  
  /**
   * 특정 레코드 삭제
   * @param surveyno
   * @return
   */
  public int delete_survey(int surveyno);


}
