package dev.mvc.bannedwords;

import java.util.ArrayList;
import java.util.HashMap;


public interface BannedwordsProcInter {
    /**
     * 등록
     * @param bannedwordsVO
     * @return
     */
    public int create(BannedwordsVO bannedwordsVO);
    
    /**
     * 전체 목록
     * @return
     */
    public ArrayList<BannedwordsVO> list_all();
    
    /**
     * 등록된 목록
     * @param wordno
     * @return
     */
    public ArrayList<BannedwordsVO> list_by_wordno(int wordno);
    
    /**
     * 조회
     * @param wordno
     * @return
     */
    public BannedwordsVO read(int wordno);
    
    /**
     * 검색 목록
     * @param map
     * @return
     */
    public ArrayList<BannedwordsVO> list_by_wordno_search(HashMap<String, Object> hashMap);
  
    /**
     * 검색 레코드 갯수
     * @param map
     * @return
     */
    public int count_by_wordno_search(HashMap<String, Object> hashMap);
  
    /**
     * 검색 및 페이징
     * @param BannedwordsVO
     * @return
     */
    public ArrayList<BannedwordsVO> list_by_wordno_search_paging(HashMap<String, Object> map);

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
    public String pagingBox(int now_page, String word, String reason, int search_count, int record_per_page, int page_per_block);
    
    /**
     * 내용 수정
     * @param BannedwordsVO
     * @return 처리된 레코드 갯수
     */
    public int update_text(BannedwordsVO bannedwordsVO);

    /**
     * 삭제
     * @param wordno 삭제할 레코드 PK
     * @return 삭제된 레코드 갯수
     */
    public int delete(int wordno);
   
    /**
     * 추천수 증가
     * @param bannedwordsVO
     * @return 처리된 레코드 갯수
     */
    public int increaseGoodcnt(int wordno);
    
    /**
     * 추천수 증가
     * @param bannedwordsVO
     * @return 처리된 레코드 갯수
     */
    public int decreaseGoodcnt(int wordno);
    
    /**
     * 추천
     * @param wordno
     * @return
     */
    public int good(int wordno);
    
}
