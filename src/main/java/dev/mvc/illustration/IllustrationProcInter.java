package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dev.mvc.diary.DiaryVO;

public interface IllustrationProcInter {

    int create(IllustrationVO illustrationVO);

    ArrayList<IllustrationVO> list_all();

    ArrayList<IllustrationVO> list_by_illustno(int illustno);

    IllustrationVO read(int illustno);

    int update_text(IllustrationVO illustrationVO);

    int update_file(IllustrationVO illustrationVO);

    int delete(int illustno);

    int list_by_illustno_search_count(HashMap<String, Object> hashMap);

    ArrayList<IllustrationVO> list_by_illustno_search_paging(HashMap<String, Object> map);

    int count_by_illustno(int illustno);

    /**
     * Diary의 ddate를 가져오는 메서드
     * @param illustno
     * @return Diary의 ddate
     */
    Date getDiaryDateByIllustNo(int illustno);

    /**
     * 기간 기반 일러스트 검색
     * @param startDate
     * @param endDate
     * @param startNum
     * @param endNum
     * @return 검색 결과 리스트
     */
    ArrayList<IllustrationVO> listByIllustNoSearchPaging(HashMap<String, Object> paramMap);


    /**
     * 기간 기반 검색 결과 수 반환
     * @param startDate
     * @param endDate
     * @return 검색 결과 수
     */
    int searchCount(String startDate, String endDate);


    /**
     * 페이징 HTML 생성
     * @param now_page
     * @param start_date
     * @param end_date
     * @param list_file_name
     * @param search_count
     * @param record_per_page
     * @param page_per_block
     * @return 페이징 HTML 문자열
     */
    String pagingBox(int now_page, String start_date, String end_date, String list_file_name, int search_count, int record_per_page, int page_per_block);

    int countByDateRange(HashMap<String, Object> paramMap);

    
}
