package dev.mvc.score;

import java.util.ArrayList;
import java.util.HashMap;

public interface ScoreDAOInter {
  /**
   * 평점 추가
   * @param ScoreVO
   * @return
   */
  public int create(ScoreVO scoreVO);
  
  /**
   * 평점 조회
   * @param scoreno
   * @return
   */
  public ScoreVO read(int scoreno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<ScoreVO> list_all();
  
  /**
   * 평점 등록된 목록
   * @param scoreno
   * @return
   */
  public ArrayList<ScoreVO> list_by_scoreno(int scoreno);
  
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
  public String pagingBox(int now_page, String jumsu, int memberno, int search_count, int record_per_page, int page_per_block);  
  
  /**
   * 평점 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<ScoreVO> list_by_scoreno_search(HashMap<String, Object> hashMap);
  
  /**
   * 평점 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_scoreno_search(HashMap<String, Object> hashMap);
  
  /**
   * 평점 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<ScoreVO> list_by_scoreno_search_paging(HashMap<String, Object> map);
  
  /**
   * 평점 내용 수정
   * @param ScoreVO
   * @return
   */
  public int update_score(ScoreVO scoreVO);
  
  /**
   * 평점 삭제
   * @param scoreno
   * @return
   */
  public int delete(int scoreno);
	  
}
