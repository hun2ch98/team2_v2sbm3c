package dev.mvc.scmenu;

import java.util.ArrayList;
import java.util.HashMap;

public interface ScmenuDAOInter {
  /**
   * 평점 메뉴 추가
   * @param ScmenuVO
   * @return
   */
  public int create(ScmenuVO scmenuVO);
  
  /**
   * 평점 메뉴 조회
   * @param menuno
   * @return
   */
  public ScmenuVO read(int menuno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<ScmenuVO> list_all();
  
  /**
   * 평점 메뉴 등록된 목록
   * @param menuno
   * @return
   */
  public ArrayList<ScmenuVO> list_by_menuno(int menuno);
  
  /**
   * 평점 메뉴 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<ScmenuVO> list_by_menuno_search(HashMap<String, Object> hashMap);
  
  /**
   * 평점 메뉴 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_menuno_search(HashMap<String, Object> hashMap);
  
  /**
   * 평점 메뉴 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<ScmenuVO> list_by_menuno_search_paging(HashMap<String, Object> map);
  
  /**
   * 평점 메뉴 내용 수정
   * @param ScmenuVO
   * @return
   */
  public int update_text(ScmenuVO scmenuVO);
  
  /**
   * 평점 메뉴 삭제
   * @param menuno
   * @return
   */
  public int delete(int menuno);

}
