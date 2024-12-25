package dev.mvc.grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface GradeDAOInter {
  
  /** 
   * 등급 추가
   * @param gradeVO
   * @return
   */
  public int create(GradeVO gradeVO);
  
  /**
   * 등급 조회
   * @param gradeno
   * @return
   */
  public GradeVO read(int gradeno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<GradeVO> list_all();
  
  /**
   * 등록된 등급 목록
   * @param gradeno
   * @return
   */
  public ArrayList<GradeVO> list_by_gradeno(int gradeno);
  
  /**
   * 등급 수정
   * @param gradeVO
   * @return
   */
  public int update(GradeVO gradeVO);
  
  /**
   * 등급 삭제
   * @param gradeno
   * @return
   */
  public int delete(int gradeno);
  
  /**
   * 등급 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<GradeVO> list_by_gradeno_search(HashMap<String, Object> hashMap);
  
  /**
   * 등급 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_gradeno_search(HashMap<String, Object> hashMap);
  
  /**
   * 등급 종류별 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<GradeVO> list_by_gradeno_search_paging(HashMap<String, Object> map);
  
}
