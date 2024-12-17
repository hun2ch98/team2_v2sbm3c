package dev.mvc.diary;

import java.util.ArrayList;

public interface DiaryProcInter {
  /**
   * <pre>
   * 등록
   * </pre>
   * @param diaryVO
   * @return
   */
  public int create(DiaryVO diaryVO);
  
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
  public ArrayList<DiaryVO> list_search(String title, String date);  
  
  /**
   * 검색 갯수
   * @param title
   * @param date
   * @return
   */
  public Integer list_search_count(String title, String date);
  
  /**
   * 검색 + 페이징 목록
   * select id="list_search_paging" resultType="dev.mvc.diary.DiaryVO" parameterType="Map" 
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<DiaryVO> list_search_paging(String title, String date, String sort, int now_page, int record_per_page);

  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String title, String date, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  

  public int cntcount(int diaryno);

  
}






