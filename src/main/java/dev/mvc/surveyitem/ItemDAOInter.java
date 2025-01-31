package dev.mvc.surveyitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.mvc.participants.PartVO;

public interface ItemDAOInter {
  
  /**
   * 등록
   */
  public int create(ItemVO itemVO);
  
  /**
   * 조회
   */
  public ItemVO read(int itemno);
  
  /**
   * 회원
   * 목록
   */
  public ArrayList<ItemVO> list_member(int surveyno);
  
  /**
   * 관리자
   * 목록
   */
  public ArrayList<ItemVO> list_all_com(int surveyno);
  
  /**
   * 수정
   */
  public int update(ItemVO itemVO);
  
  /**
   * 삭제
   */
  public int delete(int itemVO);
  
  /**
   * 회원 참여수
   */
  public int update_cnt(int itemno);
  
  /**
   * 설문참여 회원 등록
   */
  public int create(PartVO partVO);
  
  /**
   * 검색 목록
   */
  public ArrayList<ItemVO> list_search(int surveyno, String word);
  
  /**
   * 검색 갯수
   */
  public int count_by_search(Map<String, Object> map);
  
  /**
   * 검색 + 페이징 목록
   */
  public ArrayList<ItemVO> list_search_paging(Map<String, Object> map);
  
  /**
   * 특정 카테고리에 속한 레코드 갯수 산출
   */  
  public int count_survey(int surveyno);
  
  /**
   * 특정 레코드 삭제
   */
  public int delete_survey(int surveyno);
  
  /**
   * 설문 참여 결과 조회
   */
  public int count_result(int itemno);
  


}
