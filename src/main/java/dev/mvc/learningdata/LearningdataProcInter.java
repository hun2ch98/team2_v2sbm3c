package dev.mvc.learningdata;

import java.util.ArrayList;
import java.util.HashMap;

public interface LearningdataProcInter {
	
  /**
   * 학습 데이터 추가
   * @param LearningdataVO
   * @return
   */
  public int create(LearningdataVO learningdataVO);
  
  /**
   * 학습 데이터 조회
   * @param datano
   * @return
   */
  public LearningdataVO read(int datano);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<LearningdataVO> list_all();
  
  /**
   * 학습 데이터 등록된 목록
   * @param datano
   * @return
   */
  public ArrayList<LearningdataVO> list_by_datano(int datano);
  
  /**
   * 학습 데이터 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<LearningdataVO> list_by_datano_search(HashMap<String, Object> hashMap);
  
  /**
   * 학습 데이터 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_datano_search(HashMap<String, Object> hashMap);
  
  /**
   * 학습 데이터 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<LearningdataVO> list_by_datano_search_paging(HashMap<String, Object> map);
  
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
  public String pagingBox(int now_page, String ethical, String ques, int search_count, int record_per_page, int page_per_block);

  /**
   * 학습 데이터 내용 수정
   * @param LearningdataVO
   * @return
   */
  public int update_text(LearningdataVO learningdataVO);
  
  /**
   * 학습 데이터 삭제
   * @param datano
   * @return
   */
  public int delete(int datano);
  
  /**
   * 모든 학습 데이터 목록을 반환
   * @return
   */
  ArrayList<LearningdataVO> findAll();
}

