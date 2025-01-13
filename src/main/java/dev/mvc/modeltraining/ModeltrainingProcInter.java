package dev.mvc.modeltraining;

import java.util.ArrayList;
import java.util.HashMap;

public interface ModeltrainingProcInter {
  /**
   * 모델 학습 이력 추가
   * @param ModeltrainingVO
   * @return
   */
  public int create(ModeltrainingVO modeltrainingVO);
  
  /**
   * 모델 학습 이력 조회
   * @param trainingno
   * @return
   */
  public ModeltrainingVO read(int trainingno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<ModeltrainingVO> list_all();
  
  /**
   * 모델 학습 이력 등록된 목록
   * @param trainingno
   * @return
   */
  public ArrayList<ModeltrainingVO> list_by_trainingno(int trainingno);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param wordno 등급 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  public String pagingBox(int now_page, String name, String notes, String status, int search_count, int record_per_page, int page_per_block);
  
  
  /**
   * 모델 학습 이력 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<ModeltrainingVO> list_by_trainingno_search(HashMap<String, Object> hashMap);
  
  /**
   * 모델 학습 이력 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_trainingno_search(HashMap<String, Object> hashMap);
  
  /**
   * 모델 학습 이력 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<ModeltrainingVO> list_by_trainingno_search_paging(HashMap<String, Object> map);
  
  /**
   * 모델 학습 이력 내용 수정
   * @param ModeltrainingVO
   * @return
   */
  public int update_text(ModeltrainingVO modeltrainingVO);
  
  /**
   * 모델 학습 이력 삭제
   * @param trainingno
   * @return
   */
  public int delete(int trainingno);
	  
}
