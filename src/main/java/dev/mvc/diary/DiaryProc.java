package dev.mvc.diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
  public Integer list_search_count(String title, String date) {
    int cnt = this.diaryDAO.list_search_count(title, date);
    return cnt;
  }

  @Override
  public ArrayList<DiaryVO> list_search_paging(String title, String date, String sort, int now_page, int record_per_page) {
      /*
       페이지당 10개의 레코드 출력
       now_page가 1일 때: start_num = 1, end_num = 10
       now_page가 2일 때: start_num = 11, end_num = 20
      */
      int start_num = ((now_page - 1) * record_per_page) + 1;
      int end_num = (start_num + record_per_page) - 1;

      // 파라미터를 map에 넣어 전달
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("title", title.trim());   // title 검색 조건
      map.put("date", date.trim());     // date 검색 조건
      map.put("sort", sort);            // 정렬 기준 (ASC/DESC)
      map.put("start_num", start_num);  // 페이징 시작 번호
      map.put("end_num", end_num);      // 페이징 끝 번호

      // DAO에서 SQL을 실행
      ArrayList<DiaryVO> list = this.diaryDAO.list_search_paging(map);
      return list;
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
  public String pagingBox(int now_page, String title, String date, String list_file_name, int search_count, 
                                      int record_per_page, int page_per_block){    
    // 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
    int total_page = (int)(Math.ceil((double)search_count / record_per_page));
    // 전체 그룹  수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
    int total_grp = (int)(Math.ceil((double)total_page / page_per_block)); 
    // 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
    int now_grp = (int)(Math.ceil((double)now_page / page_per_block));  
    
    // 1 group: 1, 2, 3 ... 9, 10
    // 2 group: 11, 12 ... 19, 20
    // 3 group: 21, 22 ... 29, 30
    int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지  
    int end_page = (now_grp * page_per_block);               // 특정 그룹의 마지막 페이지   
     
    StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름 
    
    // style이 java 파일에 명시되는 경우는 로직에 따라 css가 영향을 많이 받는 경우에 사용하는 방법
    str.append("<style type='text/css'>"); 
    str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}"); 
    str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}"); 
    str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}"); 
    str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}"); 
    str.append("  .span_box_1{"); 
    str.append("    text-align: center;");    
    str.append("    font-size: 1em;"); 
    str.append("    border: 1px;"); 
    str.append("    border-style: solid;"); 
    str.append("    border-color: #cccccc;"); 
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("  }"); 
    str.append("  .span_box_2{"); 
    str.append("    text-align: center;");    
    str.append("    background-color: #668db4;"); 
    str.append("    color: #FFFFFF;"); 
    str.append("    font-size: 1em;"); 
    str.append("    border: 1px;"); 
    str.append("    border-style: solid;"); 
    str.append("    border-color: #cccccc;"); 
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("  }"); 
    str.append("</style>"); 
    str.append("<div id='paging'>"); 
//    str.append("현재 페이지: " + nowPage + " / " + totalPage + "  "); 
 
    // 이전 10개 페이지로 이동
    // now_grp: 1 (1 ~ 10 page)
    // now_grp: 2 (11 ~ 20 page)
    // now_grp: 3 (21 ~ 30 page) 
    // 현재 2그룹일 경우: (2 - 1) * 10 = 1그룹의 마지막 페이지 10
    // 현재 3그룹일 경우: (3 - 1) * 10 = 2그룹의 마지막 페이지 20
    int _now_page = (now_grp - 1) * page_per_block;  
    if (now_grp >= 2){ // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성 
      str.append("<span class='span_box_1'><a href='"+list_file_name+"?&word="+title+"?&date="+date+"&now_page="+_now_page+"'>이전</a></span>"); 
    } 
 
    // 중앙의 페이지 목록
    for(int i=start_page; i<=end_page; i++){ 
      if (i > total_page){ // 마지막 페이지를 넘어갔다면 페이 출력 종료
        break; 
      } 
  
      if (now_page == i){ // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
        str.append("<span class='span_box_2'>"+i+"</span>"); // 현재 페이지, 강조 
      }else{
        // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
        str.append("<span class='span_box_1'><a href='"+list_file_name+"?word="+title+"?&date="+date+"&now_page="+i+"'>"+i+"</a></span>");   
      } 
    } 
 
    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page),  nowGrp: 2 (11 ~ 20 page),  nowGrp: 3 (21 ~ 30 page) 
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * page_per_block)+1; //  최대 페이지수 + 1 
    if (now_grp < total_grp){ 
      str.append("<span class='span_box_1'><a href='"+list_file_name+"?&word="+title+"?&date="+date+"&now_page="+_now_page+"'>다음</a></span>"); 
    } 
    str.append("</div>"); 
     
    return str.toString(); 
  }
  
  @Override
  public int cntcount(int diaryno) {
      return diaryDAO.cntcount(diaryno);
  }

  @Override
  public ArrayList<DiaryVO> listSearch(String title, String startDate, String endDate) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("title", title != null ? "%" + title.trim() + "%" : null);
      paramMap.put("startDate", startDate != null ? startDate.trim() : null);
      paramMap.put("endDate", endDate != null ? endDate.trim() : null);

      return diaryDAO.listSearch(paramMap);
  }


  
}




