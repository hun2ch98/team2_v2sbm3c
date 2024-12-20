package dev.mvc.diary;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
  
  /**
   * 우선 순위 높임, 10 등 -> 1 등
   * @param diaryno
   * @return
   */
  public int update_seqno_forward(int diaryno);
  
  /**
   * 우선 순위 낮춤, 1 등 -> 10 등
   * @param diaryno
   * @return
   */
  public int update_seqno_backward(int diaryno);
  
  /**
   * 카테고리 공개 설정
   * @param diaryno
   * @return
   */
  public int update_visible_y(int diaryno);
  
  /**
   * 카테고리 비공개 설정
   * @param diaryno
   * @return
   */
  public int update_visible_n(int diaryno);

  /**
   * 숨긴 '카테고리 그룹'을 제외하고 접속자에게 공개할 '카테고리 그룹' 출력
   * SQL -> DiaryVO 객체 레코드 수 만큼 생성 -> ArrayList<diaryVO> 객체 생성되어 DiaryDAOInter로 리턴 
   * select id="list_all_diarygrp_y" resultType="dev.mvc.diary.DiaryVO"
   * @return
   */
  public ArrayList<DiaryVO> list_all_diarygrp_y();  

  /**
   * 숨긴 '카테고리 그룹'을 제외하고 접속자에게 공개할 '카테고리' 출력
   * SQL -> DiaryVO 객체 레코드 수 만큼 생성 -> ArrayList<diaryVO> 객체 생성되어 DiaryDAOInter로 리턴 
   * select id="list_all_diary_y" resultType="dev.mvc.diary.DiaryVO"
   * @return
   */
  public ArrayList<DiaryVO> list_all_diary_y(String genre);  
  
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

  
}






