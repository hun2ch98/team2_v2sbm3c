package dev.mvc.diary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import dev.mvc.schedule.ScheduleVO;

public interface DiaryDAOInter {
  /**
   * <pre>
   * MyBATIS: insert id="create" parameterType="dev.mvc.diary.DiaryVO"
   * insert: int를 리턴, 등록한 레코드 갯수를 리턴
   * id="create": 메소드명으로 사용
   * parameterType="dev.mvc.diary.DiaryVO": 메소드의 파라미터
   * Spring Boot가 자동으로 구현
   * </pre>
   * @param diaryVO
   * @return
   */
  public int create(DiaryVO diaryVO);
  
  /**
   * 전체 목록
   * SQL -> DiaryVO 객체 레코드 수 만큼 생성 -> ArrayList<diaryVO> 객체 생성되어 DiaryDAOInter로 리턴 
   * select id="list_all" resultType="dev.mvc.diary.DiaryVO"
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
  public ArrayList<DiaryVO> list_search(String word);
  
  /**
   * 검색 갯수
   * @return
   */
  int list_search_count(@Param("title") String title, @Param("date") String date);

  
  /**
   * 검색 + 페이징 목록
   * select id="list_search_paging" resultType="dev.mvc.diary.DiaryVO" parameterType="Map"
   * @param map
   * @return
   */
  public ArrayList<DiaryVO> list_search_paging(Map<String, Object> map);

  

  public int cntcount(int diaryno);
  
  public ArrayList<DiaryVO> list_search(String title, String date);
  

  ArrayList<DiaryVO> listSearch(Map<String, Object> paramMap);

  public List<Date> getAvailableDates();
  
  DiaryVO getDiaryByDiaryNo(int diaryno);
  
  // public List<DiaryVO> readList(int diaryno);

  public int increaseCnt(int diaryno);
  
  public ArrayList<ScheduleVO> list_calendar(String date);
  
  public List<ScheduleVO> list_calendar_day(String date);
  
}