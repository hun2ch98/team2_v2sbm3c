package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

public interface BoardDAOInter {
  
  /**
   * 게시글 추가
   * @param boardVO
   * @return
   */
  public int create(BoardVO boardVO);
  
  /**
   * 게시글 조회
   * @param boardno
   * @return
   */
  public BoardVO read(int boardno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<BoardVO> list_all();
  
  /**
   * 게시글 등록된 목록
   * @param boardno
   * @return
   */
  public ArrayList<BoardVO> list_by_boardno(int boardno);
  
  /**
   * 게시글 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<BoardVO> list_by_boardno_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_boardno_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<BoardVO> list_by_boardno_search_paging(HashMap<String, Object> map);
  
  /**
   * 게시글 내용 수정
   * @param boardVO
   * @return
   */
  public int update_text(BoardVO boardVO);
  
  /**
   * 파일 수정
   * @param boardVO
   * @return
   */
  public int update_file(BoardVO boardVO);
  
  /**
   * 게시글 삭제
   * @param boardno
   * @return
   */
  public int delete(int boardno);
  
  /**
   * 추천수
   * @param boardno
   * @return
   */
  public int update_goodcnt(int boardno);
  
  /**
   * 비추천수
   * @param boardno
   * @return
   */
  public int update_badcnt(int boardno);
  
}
