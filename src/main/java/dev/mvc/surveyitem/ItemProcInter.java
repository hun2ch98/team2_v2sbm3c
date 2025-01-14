package dev.mvc.surveyitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.mvc.participants.PartVO;

public interface ItemProcInter {
  
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
  public ArrayList<ItemVO> list_member(int surveyno);
  
  /**
   * 관리자
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
   * 등록
   * @param partVO
   * @return
   */
  public int create(PartVO partVO);
  
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
  public ArrayList<ItemVO> list_search_paging(int surveyno, String word, int now_page, int record_per_page);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int surveyno, int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  
  /**
   * 특정 카테고리에 속한 레코드 갯수 산출
   * @param surveyno
   * @return
   */
  public int count_survey(int itemno, int memberno);
  
  /**
   * 특정 레코드 삭제
   * @param surveyno
   * @return
   */
  public int delete_survey(int surveyno);
  
  /**
   * 설문 참여 결과 조회
   * @param itemno
   * @return
   */
  public int count_result(int itemno);


}
