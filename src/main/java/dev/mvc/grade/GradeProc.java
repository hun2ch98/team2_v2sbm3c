package dev.mvc.grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.grade.GradeProc")
public class GradeProc implements GradeProcInter{
  
  @Autowired
  Security security;
  
  @Autowired
  private GradeDAOInter gradeDAO;
  
  @Override
  public int create(GradeVO gradeVO) {
    int cnt = this.gradeDAO.create(gradeVO);
    return cnt;
  }
  
  @Override
  public GradeVO read(int gradeno) {
    GradeVO gradeVO = this.gradeDAO.read(gradeno);
    return gradeVO;
  }
  
  @Override
  public ArrayList<GradeVO> list_all() {
    ArrayList<GradeVO> list = this.gradeDAO.list_all();
    return list;
  }
  
  @Override
  public int delete(int gradeno) {
    int cnt = this.gradeDAO.delete(gradeno);
    return cnt;
  }
  
  @Override
  public ArrayList<GradeVO> list_by_gradeno_search_paging(HashMap<String, Object> map) {
    
    int now_page = (int) map.get("now_page");
    int record_per_page = 10; // 페이지당 레코드 수

    int startRow = (now_page - 1) * record_per_page + 1;
    int endRow = now_page * record_per_page;

    // 계산된 값을 `HashMap`에 추가합니다.
    map.put("startRow", startRow);
    map.put("endRow", endRow);
    
    // 데이터베이스 쿼리 실행
    ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno_search_paging(map);
    return list;
  }
  
  @Override
  public int count_by_gradeno_search(HashMap<String, Object> map) {
    int cnt = this.gradeDAO.count_by_gradeno_search(map);
    return cnt;
  }
  
  @Override
  public String pagingBox(int memberno, int now_page, String board_cate, String list_file, int search_count,
          int record_per_page, int page_per_block) {
      int total_page = (int) Math.ceil((double) search_count / record_per_page);
      int total_grp = (int) Math.ceil((double) total_page / page_per_block);
      int now_grp = (int) Math.ceil((double) now_page / page_per_block);

      int start_page = ((now_grp - 1) * page_per_block) + 1;
      int end_page = now_grp * page_per_block;

      StringBuffer str = new StringBuffer();
      str.append("<style type='text/css'>");
      str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}");
      str.append("  .span_box_1{border: 1px solid #cccccc; padding:1px 6px; margin:1px;}");
      str.append("  .span_box_2{background-color: #668db4; color: white; border: 1px solid #cccccc; padding:1px 6px; margin:1px;}");
      str.append("</style>");
      str.append("<div id='paging'>");

      // 이전 그룹 링크
      int _now_page = (now_grp - 1) * page_per_block;
      if (now_grp > 1) {
          str.append("<span class='span_box_1'><a href='" + list_file + "?memberno=" + memberno +
                  "&board_cate=" + board_cate + "&now_page=" + _now_page + "'>이전</a></span>");
      }

      // 현재 그룹의 페이지 링크
      for (int i = start_page; i <= end_page; i++) {
          if (i > total_page) break;
          if (i == now_page) {
              str.append("<span class='span_box_2'>" + i + "</span>");
          } else {
              str.append("<span class='span_box_1'><a href='" + list_file + "?memberno=" + memberno +
                      "&board_cate=" + board_cate + "&now_page=" + i + "'>" + i + "</a></span>");
          }
      }

      // 다음 그룹 링크
      _now_page = now_grp * page_per_block + 1;
      if (now_grp < total_grp) {
          str.append("<span class='span_box_1'><a href='" + list_file + "?memberno=" + memberno +
                  "&board_cate=" + board_cate + "&now_page=" + _now_page + "'>다음</a></span>");
      }

      str.append("</div>");
      return str.toString();
  }
  
  @Override
  public ArrayList<GradeVO> list_by_gradeno(int gradeno) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("gradeno", gradeno);
    ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno(gradeno);
        return list;
  }
  
  @Override
  public ArrayList<GradeVO> list_by_gradeno_search(HashMap<String, Object> hashMap) {
    ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno_search(hashMap);
    return list;
  }
  
}
