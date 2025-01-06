package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.mvc.diary.DiaryVO;


public interface IllustrationProcInter {

    int create(IllustrationVO illustrationVO);

    List<Map<String, Object>> listAllWithDiaryDetails();
    
    /**
     * 일러스트 번호로 일러스트 정보 조회
     * @param illustno
     * @return IllustrationVO
     */
    IllustrationVO read(int illustno);

    /**
     * 일기 번호로 일기 정보 조회
     * @param diaryno
     * @return DiaryVO
     */
    DiaryVO readDiary(int diaryno);
    
    int delete(int illustno);
 
    
    public int update(IllustrationVO illustrationVO);
    
    // 일기 번호에 해당하는 일러스트 썸네일을 가져오는 메서드 선언
    List<IllustrationVO> getIllustrationsByDiaryNo(int diaryno);
    
    List<Map<String, Object>> list_search_paging(String title, int now_page, int record_per_page, int start_num, int end_num);
    
    /** 
     * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
     * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
     *
     * @param now_page  현재 페이지
     * @param word 검색어
     * @param list_file_name 목록 파일명
     * @param search_count 검색 레코드수   
     * @param record_per_page 페이지당 레코드 수
     * @param page_per_block 블럭당 페이지 수
     * @return 페이징 생성 문자열
     */
    String pagingBox(int now_page, String title, String start_date, String end_date, String list_file_name, int search_count,
        int record_per_page, int page_per_block);

    int countSearchResults(String title, String start_date, String end_date);

    int cntcount(int illustno);

    int list_search_count(String title, String date);

    ArrayList<IllustrationVO> list_search(String title, String date);
}