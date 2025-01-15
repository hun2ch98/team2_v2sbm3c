package dev.mvc.modeltraining;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.modeltraining.ModeltrainingVO;

public interface ModeltrainingDAOInter {
	
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
