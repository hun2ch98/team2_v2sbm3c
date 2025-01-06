package dev.mvc.diary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

// 알고리즘 구현
@Service("dev.mvc.diary.DiaryProc")
public class DiaryProc implements DiaryProcInter {
  @Autowired // DiaryDAOInter를 구현한 클래스의 객체를 자동으로 생성하여 diaryDAO 객체에 할당
  private DiaryDAOInter diaryDAO;
  
  public DiaryProc() {
    System.out.println("-> DiaryProc created.");
  }
  
  @Override
  public int create(DiaryVO diaryVO) {
    int cnt = this.diaryDAO.create(diaryVO);
    
    return cnt;
  }

  @Override
  public ArrayList<DiaryVO> list_all() {
    ArrayList<DiaryVO> list = this.diaryDAO.list_all();
    
    return list;
  }

  @Override
  public DiaryVO read(Integer diaryno) {
    DiaryVO diaryVO = this.diaryDAO.read(diaryno);
    
    return diaryVO;
  }

  @Override
  public int update(DiaryVO diaryVO) {
    int cnt = this.diaryDAO.update(diaryVO);
    
    return cnt;
  }

  @Override
  public int delete(int diaryno) {
      int cnt = this.diaryDAO.delete(diaryno);
      return cnt;
  }

  @Override
  public int update_seqno_forward(int diaryno) {
    int cnt = this.diaryDAO.update_seqno_forward(diaryno);
    return cnt;
  }

  @Override
  public int update_seqno_backward(int diaryno) {
    int cnt = this.diaryDAO.update_seqno_backward(diaryno);
    return cnt;
  }

  @Override
  public int update_visible_y(int diaryno) {
    int cnt = this.diaryDAO.update_visible_y(diaryno);
    return cnt;
  }

  @Override
  public int update_visible_n(int diaryno) {
    int cnt = this.diaryDAO.update_visible_n(diaryno);
    return cnt;
  }

  @Override
  public ArrayList<DiaryVO> list_all_diarygrp_y() {
    ArrayList<DiaryVO> list = this.diaryDAO.list_all_diarygrp_y();
    
    return list;
  }

  @Override
  public ArrayList<DiaryVO> list_all_diary_y(String genre) {
    ArrayList<DiaryVO> list = this.diaryDAO.list_all_diary_y(genre);
    
    return list;
  }


  @Override
  public ArrayList<String> genreset() {
    ArrayList<String> list = this.diaryDAO.genreset();
    return list;
  }

  @Override
  public ArrayList<DiaryVO> list_search(String title, String date) {
    ArrayList<DiaryVO> list = this.diaryDAO.list_search(title, date);
    return list;
  }

  @Override
  public int list_search_count(String title, String date) {
      return diaryDAO.list_search_count(title, date);
  }

  @Override
  public ArrayList<DiaryVO> list_search_paging(String title, String nowPage, String recordPerPage, int startNum, int endNum) {

      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("title", title != null ? title.trim() : "");
      paramMap.put("startNum", startNum);
      paramMap.put("endNum", endNum);

      return diaryDAO.list_search_paging(paramMap);
  }




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
  @Override
  public String pagingBox(int now_page, String title, String start_date, String end_date, String list_file_name, 
                          int search_count, int record_per_page, int page_per_block) {
      // 전체 페이지 수 계산
      int total_page = (int) Math.ceil((double) search_count / record_per_page);
      // 전체 그룹 수 계산
      int total_grp = (int) Math.ceil((double) total_page / page_per_block);
      // 현재 그룹 계산
      int now_grp = (int) Math.ceil((double) now_page / page_per_block);

      // 현재 그룹의 시작 페이지와 끝 페이지
      int start_page = ((now_grp - 1) * page_per_block) + 1;
      int end_page = now_grp * page_per_block;

      // 마지막 페이지를 전체 페이지로 제한
      end_page = Math.min(end_page, total_page);

      StringBuilder pagingHtml = new StringBuilder();
      pagingHtml.append("<style type='text/css'>");
      pagingHtml.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}");
      pagingHtml.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}");
      pagingHtml.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}");
      pagingHtml.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}");
      pagingHtml.append("  .span_box_1{");
      pagingHtml.append("    text-align: center;");
      pagingHtml.append("    font-size: 1em;");
      pagingHtml.append("    border: 1px;");
      pagingHtml.append("    border-style: solid;");
      pagingHtml.append("    border-color: #cccccc;");
      pagingHtml.append("    padding:1px 6px 1px 6px;");
      pagingHtml.append("    margin:1px 2px 1px 2px;");
      pagingHtml.append("  }");
      pagingHtml.append("  .span_box_2{");
      pagingHtml.append("    text-align: center;");
      pagingHtml.append("    background-color: #668db4;");
      pagingHtml.append("    color: #FFFFFF;");
      pagingHtml.append("    font-size: 1em;");
      pagingHtml.append("    border: 1px;");
      pagingHtml.append("    border-style: solid;");
      pagingHtml.append("    border-color: #cccccc;");
      pagingHtml.append("    padding:1px 6px 1px 6px;");
      pagingHtml.append("    margin:1px 2px 1px 2px;");
      pagingHtml.append("  }");
      pagingHtml.append("</style>");
      pagingHtml.append("<div id='paging'>");

      // 이전 그룹으로 이동
      if (now_grp > 1) {
          int prev_page = (now_grp - 1) * page_per_block;
          pagingHtml.append("<span class='span_box_1'><a href='")
                    .append(list_file_name)
                    .append("?title=").append(title)
                    .append("&start_date=").append(start_date)
                    .append("&end_date=").append(end_date)
                    .append("&now_page=").append(prev_page)
                    .append("'>이전</a></span>");
      }

      // 페이지 번호 출력
      for (int i = start_page; i <= end_page; i++) {
          if (i == now_page) { // 현재 페이지 강조
              pagingHtml.append("<span class='span_box_2'>").append(i).append("</span>");
          } else { // 다른 페이지는 링크 출력
              pagingHtml.append("<span class='span_box_1'><a href='")
                        .append(list_file_name)
                        .append("?title=").append(title)
                        .append("&start_date=").append(start_date)
                        .append("&end_date=").append(end_date)
                        .append("&now_page=").append(i)
                        .append("'>").append(i).append("</a></span>");
          }
      }

      // 다음 그룹으로 이동
      if (now_grp < total_grp) {
          int next_page = now_grp * page_per_block + 1;
          pagingHtml.append("<span class='span_box_1'><a href='")
                    .append(list_file_name)
                    .append("?title=").append(title)
                    .append("&start_date=").append(start_date)
                    .append("&end_date=").append(end_date)
                    .append("&now_page=").append(next_page)
                    .append("'>다음</a></span>");
      }

      pagingHtml.append("</div>");
      return pagingHtml.toString();
  }



  // URL 인코딩 처리 메서드
  private String encode(String value) {
      try {
          return value == null ? "" : java.net.URLEncoder.encode(value, "UTF-8");
      } catch (java.io.UnsupportedEncodingException e) {
          return ""; // 인코딩 실패 시 빈 문자열 반환
      }
  }

  
  @Override
  public int cntcount(int diaryno) {
      return diaryDAO.cntcount(diaryno);
  }

  @Override
  public ArrayList<DiaryVO> listSearch(String title, String startDate, String endDate) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("title", title != null ? "%" + title.trim() + "%" : null);
      paramMap.put("start_date", startDate);
      paramMap.put("end_date", endDate);

      return diaryDAO.listSearch(paramMap);
  }

  
  //DiaryProc 클래스 내
  @Autowired
  private SqlSession sqlSession;
  
  @Override
  public int countSearchResults(String title, String startDate, String endDate) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("title", title != null ? "%" + title.trim() + "%" : null);
      paramMap.put("start_date", startDate != null ? startDate.trim() : null);
      paramMap.put("end_date", endDate != null ? endDate.trim() : null);

      return sqlSession.selectOne("countSearchResults", paramMap);
  }


  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Date> getAvailableDates() {
      String sql = "SELECT DISTINCT ddate FROM diary";
      return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getDate("ddate"));
  }
  
  @Override
  public int getDiaryNoByDate(Date ddate) {
      String sql = "SELECT diaryno FROM diary WHERE ddate = ?";
      return jdbcTemplate.queryForObject(sql, Integer.class, ddate);
  }
  
  
}




