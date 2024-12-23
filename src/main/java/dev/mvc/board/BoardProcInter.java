package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

public interface BoardProcInter {
  
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
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param cateno 카테고리 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  public String pagingBox(int memberno, int now_page, String word, String list_file, int search_count, 
      int record_per_page, int page_per_block);
}
