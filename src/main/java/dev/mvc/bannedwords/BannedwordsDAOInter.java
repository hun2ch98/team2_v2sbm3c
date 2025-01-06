package dev.mvc.bannedwords;

import java.util.ArrayList;
import java.util.HashMap;

public interface BannedwordsDAOInter {
	
/**
   * 금지 단어 추가
   * @param BannedwordsVO
   * @return
   */
  public int create(BannedwordsVO BannedwordsVO);
  
  /**
   * 금지 단어 조회
   * @param wordno
   * @return
   */
  public BannedwordsVO read(int wordno);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<BannedwordsVO> list_all();
  
  /**
   * 금지 단어 등록된 목록
   * @param wordno
   * @return
   */
  public ArrayList<BannedwordsVO> list_by_wordno(int wordno);
  
  /**
   * 금지 단어 종류별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<BannedwordsVO> list_by_wordno_search(HashMap<String, Object> hashMap);
  
  /**
   * 금지 단어 종류별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int count_by_wordno_search(HashMap<String, Object> hashMap);
  
  /**
   * 금지 단어 검색 및 페이징
   * @param map
   * @return
   */
  public ArrayList<BannedwordsVO> list_by_wordno_search_paging(HashMap<String, Object> map);
  
  /**
   * 금지 단어 내용 수정
   * @param BannedwordsVO
   * @return
   */
  public int update_text(BannedwordsVO bannedwordsVO);
  
  /**
   * 금지 단어 삭제
   * @param wordno
   * @return
   */
  public int delete(int wordno);

  
}
