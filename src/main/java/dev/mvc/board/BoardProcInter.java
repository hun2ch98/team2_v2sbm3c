package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

public interface BoardProcInter {
  
  /**
   * 게시글 추가
   */
  public int create(BoardVO boardVO);
  
  /**
   * 게시글 조회
   */
  public BoardVO read(int boardno);
  
  /**
   * 모든 목록
   */
  public ArrayList<BoardVO> list_all();
  
  /**
   * 게시글 등록된 목록
   */
  public ArrayList<BoardVO> list_by_boardno(int boardno);
  
  /**
   * 게시글 종류별 검색 목록
   */
  public ArrayList<BoardVO> list_by_boardno_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 레코드 갯수
   */
  public int count_by_boardno_search(HashMap<String, Object> hashMap);
  
  /**
   * 게시글 종류별 검색 및 페이징
   */
  public ArrayList<BoardVO> list_by_boardno_search_paging(HashMap<String, Object> map);
  
  /** 
   * 페이징
   */
  public String pagingBox(int memberno, int now_page, String board_cate, String list_file, int search_count, 
      int record_per_page, int page_per_block);
  
  /**
   * 게시글 내용 수정
   */
  public int update_text(BoardVO boardVO);
  
  /**
   * 파일 수정
   */
  public int update_file(BoardVO boardVO);
  
  /**
   * 게시글 삭제
   */
  public int delete(int boardno);
  
  /**
   * 추천 증가
   */
  public int increasegoodcnt(int boardno);
  
  /**
   * 추천 감소
   */
  public int decreasegoodcnt(int boardno);

  
}
