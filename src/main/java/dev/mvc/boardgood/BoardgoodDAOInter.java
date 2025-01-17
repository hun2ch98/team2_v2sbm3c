package dev.mvc.boardgood;

import java.util.ArrayList;
import java.util.HashMap;

public interface BoardgoodDAOInter {
  
  /**
   * 등록
   * @param boardgoodVO
   * @return
   */
  public int create(BoardgoodVO boardgoodVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<BoardgoodVO> list_all();
  
  /**
   * 삭제
   * @param goodno
   * @return
   */
  public int delete(int goodno);
  
  /**
   * 추천수
   * @param map
   * @return
   */
  public int heart_cnt(HashMap<String, Object> map);
  
  /**
   * 조회
   * @param goodno
   * @return
   */
  public BoardgoodVO read(int goodno);
  
  /**
   * 요청사항, 회원 조회
   * @param map
   * @return
   */
  public BoardgoodVO readByboardmember(HashMap<String, Object> map);
  
  /**
   * 조인 목록
   * @return
   */
  public ArrayList<BoardgoodMemberVO> list_all_join();
  
  /**
   * 검색 개수
   * @param map
   * @return
   */
  public int count_search(HashMap<String, Object> map);
  
  /**
   * 검색 + 페이징
   * @param map
   * @return
   */
  public ArrayList<BoardgoodMemberVO> list_search_paging(HashMap<String, Object> map);

}
