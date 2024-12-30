package dev.mvc.grade;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;

public interface GradeDAOInter {
  
    /**
     * 등급 추가
     * @param gradeVO
     * @return
     */
    public int create(GradeVO gradeVO);
    
    /**
     * 등급 전체 목록
     * @return
     */
    public ArrayList<GradeVO> list_all(); 
    
    /**
     * 등급별 등록된 목록
     * @param gradeno
     * @return
     */
    public ArrayList<GradeVO> list_by_gradeno(int gradeno);
    
    /**
     * 등급 조회
     * @param gradeno
     * @return
     */
    public GradeVO read(int gradeno);
    
    /**
     * 등급종류별 검색 목록
     * @param hashMap
     * @return
     */
    public ArrayList<GradeVO> list_by_gradeno_search(HashMap<String, Object> hashMap);
    
    /**
     * 등급종류별 검색 레코드 갯수
     * @param hashMap
     * @return
     */
    public int count_by_gradeno_search(HashMap<String, Object> hashMap);
    
    /**
     * 등급종류별 검색 및 페이징
     * @param map
     * @return
     */
    public ArrayList<GradeVO> list_by_gradeno_search_paging(HashMap<String, Object> map);
    
    /**
     * 등급설명글 내용 수정
     * @param gradeVO
     * @return
     */
    public int update_text(GradeVO gradeVO);
    
    /**
     * 파일 정보 수정
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
}
