package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.participants.ItemMemberPartVO;

public interface SurveygoodProcInter {
  
  /**
   * 등록
   */
  public int create(SurveygoodVO surveygoodVO);
  
  /**
   * 목록
   */
  public ArrayList<SurveygoodVO> list_all();
  
  /**
   * 삭제
   */
  public int delete(int goodno);
  
  /**
   * 특정 개수 산출
   */
  public int heartCnt(HashMap<String, Object> map);
  
  /**
   * 조회
   */
  public SurveygoodVO read(int goodno);
  
  /**
   * surveyno, memberno로 조회
   */
  public SurveygoodVO readBysurveymember(HashMap<String, Object> map);
  
  /**
   * 목록
   */
  public ArrayList<SurveySurveygoodMemberVO> list_all_join();
  
  /**
   * 검색 개수
   */
  public int count_search(HashMap<String, Object> Map);
  
  /**
   * 검색 + 페이징
   */
  public ArrayList<ItemMemberPartVO> list_search_paging(HashMap<String, Object> Map);
  
  /** 
   * 페이징 박스
   */
  public String pagingBox(int goodno, int now_page, String word, String list_file, int search_count, 
      int record_per_page, int page_per_block);



}
