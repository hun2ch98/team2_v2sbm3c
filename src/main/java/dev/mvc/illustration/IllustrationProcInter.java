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
     * 검색 목록
     * SQL -> DiaryVO 객체 레코드 수 만큼 생성 -> ArrayList<diaryVO> 객체 생성되어 DiaryDAOInter로 리턴 
     * select id="list_search" resultType="dev.mvc.diary.DiaryVO" parameterType="String"
     * @return
     */
    public ArrayList<IllustrationVO> list_search(String date);  
    
    int list_search_count(String date);
    
    /**
     * 
     * @param title
     * @param now_page
     * @param record_per_page
     * @param start_num
     * @param end_num
     * @return
     */
    public ArrayList<IllustrationVO> list_search_paging(String now_page, String record_per_page, int start_num, int end_num);


    /**
     * 
     * @param now_page
     * @param start_date
     * @param end_date
     * @param list_file_name
     * @param search_count
     * @param record_per_page
     * @param page_per_block
     * @return
     */
    String pagingBox(int now_page, String start_date, String end_date, String list_file_name, int search_count, int record_per_page, int page_per_block);

    public int cntcount(int illustno);

    // 검색 및 날짜 필터링 목록 조회
    ArrayList<IllustrationVO> listSearch(String startDate, String endDate);

    // 검색 결과 카운트 메서드 추가
    int countSearchResults(String startDate, String endDate);

    
    //2323
    ArrayList<IllustrationVO> listByIllustrationPaging(int illustno, int startNum, int endNum);
    int countAllIllustrations();
    String pagingBox(int nowPage, int totalCount, int recordPerPage, int pagePerBlock, String listFileName);

    
    
}
