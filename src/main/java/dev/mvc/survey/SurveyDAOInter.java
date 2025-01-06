package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveyDAOInter {
  
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
   * 관리자
   * 검색 및 페이징
   * @param hashMap
   * @return
   */
  public ArrayList<SurveyVO> admin_list_surveyno_search_paging(HashMap<String, Object> hashMap);
  
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

}
