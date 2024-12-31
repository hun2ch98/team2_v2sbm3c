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
     * 페이징 박스 생성 메서드
     * @param now_page 현재 페이지
     * @param list_file_name 목록 파일 이름
     * @param search_count 검색된 레코드 수
     * @param record_per_page 페이지당 레코드 수
     * @param page_per_block 블럭당 페이지 수
     * @return 페이징 HTML 문자열
     */
    String pagingBox(int now_page, String list_file_name, int search_count, int record_per_page, int page_per_block);

    
    /**
     * Diary의 ddate를 가져오는 메서드
     * @param illustno
     * @return ddate와 관련된 정보
     */
    Date getDiaryDateByIllustNo(int illustno);

    /**
     * illustno와 검색어를 기반으로 검색된 레코드 수 반환
     * @param illustno
     * @param word
     * @return 검색된 레코드 수
     */
    int searchCount(int illustno, String word);

    /**
     * 페이징 박스 생성
     * @param illustno
     * @param searchCount
     * @param nowPage
     * @param word
     * @return 페이징 HTML 문자열
     */
    String pagingBox(int illustno, int searchCount, int nowPage, String word);
    
    /**
     * 검색어와 페이지 번호에 따라 일러스트 목록을 가져옴
     * @param illustno
     * @param word
     * @param nowPage
     * @return 일러스트 목록
     */
    ArrayList<IllustrationVO> listByIllustNoSearchPaging(int illustno, String word, int nowPage);

    public List<DiaryVO> listByDateRange(String start_date, String end_date);

    
}