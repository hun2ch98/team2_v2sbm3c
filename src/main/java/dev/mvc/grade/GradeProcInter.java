package dev.mvc.grade;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;

public interface GradeProcInter {
  
    /**
     * 등급 추가
     * @param gradeVO
     * @return
     */
    public int create(GradeVO gradeVO);
    
    /**
     * 조회
     * @param gradeno
     * @return
     */
    public GradeVO read(int gradeno);
    
    /**
     * 등급 전체 목록
     * @return
     */
    public ArrayList<GradeVO> list_all(); 
    
    /**
     * 등급 등록된 목록
     * @param gradeno
     * @return
     */
    public ArrayList<GradeVO> list_by_gradeno(int gradeno);
  
    /**
     * 진화기준 내용 수정
     * @param gradeVO
     * @return
     */
    public int update_text(GradeVO gradeVO);
    
    /**
     * 파일 수정
     * @param boardVO
     * @return
     */
    public int update_file(GradeVO gradeVO);
  
    /**
     * 등급 삭제
     * @param gradeno 삭제할 레코드 PK
     * @return 삭제된 레코드 갯수
     */
    public int delete(int gradeno);
  
    /**
     * 등급 종류별 검색 목록
     * @param word 검색어
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
    
    /** 
     * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
     * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
     *
     * @param gradeno 등급 번호
     * @param now_page 현재 페이지
     * @param word 검색어
     * @param list_file 목록 파일명
     * @param search_count 검색 레코드수   
     * @param record_per_page 페이지당 레코드 수
     * @param page_per_block 블럭당 페이지 수
     * @return 페이징 생성 문자열
     */
    public String pagingBox(int now_page, String word, String evo_criteria, int search_count, int record_per_page, int page_per_block);
}
