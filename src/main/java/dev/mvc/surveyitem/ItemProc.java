package dev.mvc.surveyitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.participants.PartVO;


@Service("dev.mvc.surveyitem.ItemProc")
public class ItemProc implements ItemProcInter{
  
  @Autowired
  private ItemDAOInter itemDAO;
  
  public ItemProc() {
    System.out.println("-> ItemProc created.");
  }
  
  @Override
  public int create(ItemVO itemVO) {
    int cnt = this.itemDAO.create(itemVO);
    return cnt;
  }
  
  @Override
  public ItemVO read(int itemno) {
    ItemVO itemVO = this.itemDAO.read(itemno);
    return itemVO;
  }
  
  @Override
  public ArrayList<ItemVO> list_member(int surveyno) {
    ArrayList<ItemVO> list = this.itemDAO.list_member(surveyno);
    return list;
  }
  
  @Override
  public ArrayList<ItemVO> list_all_com(int surveyno){
    ArrayList<ItemVO> list = this.itemDAO.list_all_com(surveyno);
    return list;
  }
  
  @Override
  public int update(ItemVO itemVO) {
    int cnt = this.itemDAO.update(itemVO);
    return cnt;
  }
  
  @Override
  public int delete(int itemno) {
    int cnt = this.itemDAO.delete(itemno);
    return cnt;
  }
  
//  @Override
//  public int update_cnt(int itemno) {
//    int cnt = this.itemDAO.update_cnt(itemno);
//    return cnt;
//  }
  
  @Override
  public int update_cnt(int itemno) {
      System.out.println("update_cnt called with itemno: " + itemno); // 로그 추가
      int cnt = this.itemDAO.update_cnt(itemno);
      System.out.println("update_cnt result: " + cnt);
      return cnt;
  }

  
  @Override
  public int create(PartVO partVO) {
    int cnt = this.itemDAO.create(partVO);
    return cnt;
  }
  
  @Override
  public ArrayList<ItemVO> list_search(int surveyno, String word) {
    ArrayList<ItemVO> list = this.itemDAO.list_search(surveyno, word);
    return list;
  }

  @Override
  public int count_by_search(Map<String, Object> map) {
      int cnt = this.itemDAO.count_by_search(map);
      return cnt;
  }
  
  @Override
  public ArrayList<ItemVO> list_search_paging(int surveyno, String word, int now_page, int record_per_page) {


    int start_num = ((now_page - 1) * record_per_page) + 1;
    int end_num=(start_num + record_per_page) - 1;

    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("word", word);
    map.put("surveyno", surveyno);
    map.put("start_num", start_num);
    map.put("end_num", end_num);
    

    
    ArrayList<ItemVO> list = this.itemDAO.list_search_paging(map);
    // System.out.println("-> " + list.size());
    
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
  public String pagingBox(int surveyno, int now_page, String word, String list_file_name, int search_count, 
                          int record_per_page, int page_per_block) {    
      int total_page = (int)(Math.ceil((double)search_count / record_per_page));
      int total_grp = (int)(Math.ceil((double)total_page / page_per_block)); 
      int now_grp = (int)(Math.ceil((double)now_page / page_per_block));  

      int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지  
      int end_page = (now_grp * page_per_block);            // 특정 그룹의 마지막 페이지   
       
      StringBuffer str = new StringBuffer(); // String class보다 문자열 추가등의 편집시 속도가 빠름 
      
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

   // 모든 페이지 번호 표시
      for (int i = 1; i <= total_page; i++) {
          if (i == now_page) {
              str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지
          } else {
              str.append("<span class='span_box_1'><a href='" + list_file_name + "?surveyno=" + surveyno +
                      "&word=" + word + "&now_page=" + i + "'>" + i + "</a></span>"); // 다른 페이지
          }
      }

      str.append("</div>"); 
       
      return str.toString(); 
  }

  
  @Override
  public int count_survey(int surveyno) {
    int cnt = this.itemDAO.count_survey(surveyno);
    return cnt;
  }

  
  @Override
  public int delete_survey(int surveyno) {
    int cnt = this.itemDAO.delete_survey(surveyno);
    return cnt;
  }

  @Override
  public int count_result(int itemno) {
    int cnt = this.itemDAO.count_result(itemno);
    return cnt;
  }


}
