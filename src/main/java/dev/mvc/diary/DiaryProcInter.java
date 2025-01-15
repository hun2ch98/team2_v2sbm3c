package dev.mvc.diary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.mvc.schedule.ScheduleVO;

public interface DiaryProcInter {
  /**
   * <pre>
   * 등록
   * </pre>
   * @param diaryVO
   * @return
   */
  public int create(DiaryVO diaryVO);
  int create2(DiaryVO diaryVO);
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<DiaryVO> list_all(); 
  
  /**
   * 조회
   * @param diaryno
   * @return
   */
  public DiaryVO read(Integer diaryno);
  
  
  public int increaseGoodCnt(int diaryno);
  
  
  public int decreaseGoodCnt(int diaryno);
  
  /**
   * 수정
   * @param diaryVO 수정할 내용
   * @return 수정된 레코드 갯수
   */
  public int update(DiaryVO diaryVO); 
  
  /**
   * 삭제
   * @param diaryno 삭제할 레코드 PK
   * @return 삭제된 레코드 갯수
   */
  public int delete(int diaryno);

  public int good(int diaryno);
  
  /**
   * 장르 목록
   * @return
   */
  public ArrayList<String> genreset();
  
  /**
   * 검색 목록
   * SQL -> DiaryVO 객체 레코드 수 만큼 생성 -> ArrayList<diaryVO> 객체 생성되어 DiaryDAOInter로 리턴 
   * select id="list_search" resultType="dev.mvc.diary.DiaryVO" parameterType="String"
   * @return
   */
  public ArrayList<DiaryVO> list_search(String title, String date);  
  
  int list_search_count(String title, String date);
  
  /**
   * 
   * @param title
   * @param now_page
   * @param record_per_page
   * @param start_num
   * @param end_num
   * @return
   */
  public ArrayList<DiaryVO> list_search_paging(String title, String  now_page, String record_per_page, int start_num, int end_num);

  /**
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   * @param now_page
   * @param title
   * @param start_date
   * @param end_date
   * @param list_file_name
   * @param search_count
   * @param record_per_page
   * @param page_per_block
   * @return
   */
  String pagingBox(int now_page, String title, String start_date, String end_date, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  

  public int cntcount(int diaryno);

  // 검색 및 날짜 필터링 목록 조회
  ArrayList<DiaryVO> listSearch(String title, String startDate, String endDate);

  int countSearchResults(String title, String startDate, String endDate);

  public List<Date> getAvailableDates();

  int getDiaryNoByDate(Date ddate);
  
  DiaryVO getDiaryByDiaryNo(int diaryno);

  //public List<DiaryVO> readList(int diaryno);

  int increaseCnt(int diaryno);
  
  public ArrayList<DiaryVO> list_calendar(String date);
  
  public ArrayList<DiaryVO> list_calendar_day(String date);
  
 
}






