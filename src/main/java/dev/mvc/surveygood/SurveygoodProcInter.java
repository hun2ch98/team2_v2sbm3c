package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.participants.ItemMemberPartVO;

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
  
  /**
   * 검색 개수
   * @param Map
   * @return
   */
  public int count_search(HashMap<String, Object> Map);
  
  /**
   * 검색 + 페이징
   * @param Map
   * @return
   */
  public ArrayList<ItemMemberPartVO> list_search_paging(HashMap<String, Object> Map);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param cateno 카테고리 번호
   * @param now_page 현재 페이지
   * @param topic 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  public String pagingBox(int goodno, int now_page, String word, String list_file, int search_count, 
      int record_per_page, int page_per_block);



}
