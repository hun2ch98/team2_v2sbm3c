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
  
}

