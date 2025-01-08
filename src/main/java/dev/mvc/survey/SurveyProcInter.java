package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveyProcInter {
  
  /**
   * 등록
   * @param surveyVO
   * @return
   */
  public int create(SurveyVO surveyVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<SurveyVO> list_all();
  
  /**
   * 목록 조회
   * @param memberno
   * @return
   */
  public ArrayList<SurveyVO> list_by_surveyno(int memberno);
  
  /**
   * 설문조사 주제 검색
   * @param hashMap
   * @return
   */
  public ArrayList<SurveyVO> list_by_surveyno_search(HashMap<String, Object> hashMap);
  
  /***
   * 설문조사 주제 검색 레코드 수
   * @param hashMap
   * @return
   */
  public int count_by_surveyno_search(HashMap<String, Object> hashMap);
  
  /**
   * 회원
   * 검색 및 페이징
   * @param hashMap
   * @return
   */
  public ArrayList<SurveyVO> list_by_surveyno_search_paging(HashMap<String, Object> hashMap);
  
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
  public String pagingBox(int memberno, int now_page, String is_continue, String list_file, int search_count, 
      int record_per_page, int page_per_block);

  
  /**
   * 조회
   * @param surveyno
   * @return
   */
  public SurveyVO read(int surveyno);
  
  /**
   * 글 수정
   * @param surveyVO
   * @return
   */
  public int update_text(SurveyVO surveyVO);
  
  /**
   * 파일 수정
   * @param surveyVO
   * @return
   */
  public int update_file(SurveyVO surveyVO);
  
  /**
   * 삭제
   * @param surveyno
   * @return
   */
  public int delete(int surveyno);
  
  /**
   * 자식 무시하고 삭제
   * @param surveyno
   * @return
   */
  public int delete_survey(int surveyno);
  
  /**
   * 자료수 산출
   * @param surveyno
   * @return
   */
  public int cntcount(int surveyno);
  
  /**
   * 추천 증가
   * @param surveyno
   * @return
   */
  public int increasegoodcnt(int surveyno);
  
  /**
   * 추천 감소
   * @param surveyno
   * @return
   */
  public int decreasegoodcnt(int surveyno);
  
  /**
   * 추천
   * @param surveyno
   * @return
   */
  public int good(int surveyno);


}
