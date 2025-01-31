package dev.mvc.boardgood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public interface BoardgoodProcInter {

  
  /**
   * 등록
   */
  public int create(BoardgoodVO boardgoodVO);
  
  /**
   * 목록
   */
  public ArrayList<BoardgoodVO> list_all();
  
  /**
   * 삭제
   */
  public int delete(int goodno);
  
  /**
   * 추천수
   */
  public int heart_cnt(HashMap<String, Object> map);
  
  /**
   * 조회
   */
  public BoardgoodVO read(int goodno);
  
  /**
   * 요청사항, 회원 조회
   */
  public BoardgoodVO readByboardmember(HashMap<String, Object> map);
  
  /**
   * 조인 목록
   */
  public ArrayList<BoardgoodMemberVO> list_all_join();
  
  /**
   * 검색 개수
   */
  public int count_search(HashMap<String, Object> map);
  
  /**
   * 검색 + 페이징
   */
  public ArrayList<BoardgoodMemberVO> list_search_paging(HashMap<String, Object> map);

  /**
   * 페이징 박스
   */
  public String pagingBox(int goodno, int now_page, String word, String list_file, int search_count, 
      int record_per_page, int page_per_block);



}
