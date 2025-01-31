package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveyProcInter {
  
  /**
   * 등록
   */
  public int create(SurveyVO surveyVO);
  
  /**
   * 전체 목록
   */
  public ArrayList<SurveyVO> list_all();
  
  /**
   * 목록 조회
   */
  public ArrayList<SurveyVO> list_by_surveyno(int memberno);
  
  /**
   * 설문조사 주제 검색
   */
  public ArrayList<SurveyVO> list_by_surveyno_search(HashMap<String, Object> map);
  
  /***
   * 설문조사 주제 검색 레코드 수
   */
  public int count_by_surveyno_search(HashMap<String, Object> map);
  
  /**
   * 검색 및 페이징
   */
  public ArrayList<SurveyVO> list_by_surveyno_search_paging(HashMap<String, Object> map);
  
  /** 
   * 페이징 박스
   */
  public String pagingBox(int memberno, int now_page, String is_continue, String list_file, int search_count, 
      int record_per_page, int page_per_block);

  
  /**
   * 조회
   */
  public SurveyVO read(int surveyno);
  
  /**
   * 글 수정
   */
  public int update_text(SurveyVO surveyVO);
  
  /**
   * 파일 수정
   */
  public int update_file(SurveyVO surveyVO);
  
  /**
   * 삭제
   */
  public int delete(int surveyno);
  
  /**
   * 자료수 산출
   */
  public int cntcount(int surveyno);
  
  /**
   * 추천 증가
   */
  public int increasegoodcnt(int surveyno);
  
  /**
   * 추천 감소
   */
  public int decreasegoodcnt(int surveyno);


}
