package dev.mvc.grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.board.BoardVO;

// 알고리즘 구현
@Service("dev.mvc.grade.GradeProc")
public class GradeProc implements GradeProcInter {
  
    @Autowired // GradeDAOInter를 구현한 클래스의 객체를 자동으로 생성하여 gradeDAO 객체에 할당
    private GradeDAOInter gradeDAO;

    public GradeProc() {
        System.out.println("-> GradeProc created.");
    }
  
    @Override
    public int create(GradeVO gradeVO) {
        int cnt = this.gradeDAO.create(gradeVO);
        return cnt;
    }
    
    @Override
    public ArrayList<GradeVO> list_all() {
        ArrayList<GradeVO> list = this.gradeDAO.list_all();
        return list;
    }
    
    @Override
    public ArrayList<GradeVO> list_by_gradeno(int gradeno) {
      ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno(gradeno);
      return list;
    }
    
    /**
     * 조회
     */
    @Override
    public GradeVO read(int gradeno) {
      GradeVO gradeVO = this.gradeDAO.read(gradeno);
      return gradeVO;
    }
    
    @Override
    public ArrayList<GradeVO> list_by_gradeno_search_paging(HashMap<String, Object> map) {
      // `now_page`를 기반으로 `startRow`와 `endRow`를 계산합니다.
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
    public String pagingBox(int now_page, String grade_name, String list_file, int search_count,
            int record_per_page, int page_per_block) {
      int total_page = (int) (Math.ceil((double) search_count / record_per_page));
      int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
      int now_grp = (int) (Math.ceil((double) now_page / page_per_block));
      
      int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지
      int end_page = (now_grp * page_per_block); // 특정 그룹의 마지막 페이지
      
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
      
      // 이전 그룹 링크
      int _now_page = (now_grp - 1) * page_per_block;
      if (now_grp >= 2) { // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성

        str.append("<span class='span_box_1'><A href='" + list_file + "?grade_name=" + grade_name + "&now_page=" + _now_page
            + "'>이전</A></span>");
      }
      
      // 중앙의 페이지 목록
      for (int i = start_page; i <= end_page; i++) {
        if (i > total_page) { // 마지막 페이지를 넘어갔다면 페이 출력 종료
          break;
        }
        
        if (now_page == i) { // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
          str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지, 강조
        } else {
          // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
          str.append("<span class='span_box_1'><A href='" + list_file + "?grade_name=" + grade_name + "&now_page=" + i + "'>" + i
              + "</A></span>");
        }
      }
      
      // 10개 다음 페이지로 이동
      // nowGrp: 1 (1 ~ 10 page), nowGrp: 2 (11 ~ 20 page), nowGrp: 3 (21 ~ 30 page)
      // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
      // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
      // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
      _now_page = (now_grp * page_per_block) + 1; // 최대 페이지수 + 1
      if (now_grp < total_grp) {
        str.append("<span class='span_box_1'><A href='" + list_file + "?grade_name=" + grade_name + "&now_page=" + _now_page
            + "'>다음</A></span>");
      }
      str.append("</div>");

      return str.toString();
    }
    
    @Override
    public int count_by_gradeno_search(HashMap<String, Object> map) {
      int cnt = this.gradeDAO.count_by_gradeno_search(map);
      return cnt;
    }
    
    @Override
    public ArrayList<GradeVO> list_by_gradeno_search(HashMap<String, Object> hashMap) {
      ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno_search(hashMap);
      return list;
    }

    @Override
    public int update_text(GradeVO gradeVO) {
        int cnt = this.gradeDAO.update_text(gradeVO);
        return cnt;
    }
    
    @Override
    public int update_file(GradeVO gradeVO) {
        int cnt = this.gradeDAO.update_file(gradeVO);
        return cnt;
    }

    @Override
    public int delete(int gradeno) {
        int cnt = this.gradeDAO.delete(gradeno);
        return cnt;
    }
    
//    @Override
//    public ArrayList<GradeVO> list_by_gradeno_search(HashMap<String, Object> hashMap) {
//      ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno_search(hashMap);
//      return list;
//    }
//    
//    @Override
//    public int count_by_gradeno_search(HashMap<String, Object> map) {
//      int cnt = this.gradeDAO.count_by_gradeno_search(map);
//      return cnt;
//    }
//    
//    @Override
//    public ArrayList<GradeVO> list_by_gradeno_search_paging(HashMap<String, Object> map) {
//      /*
//       * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
//       * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
//       * 
//       * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
//       * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
//       * rownum: now_page = 3, (3 - 1) * 10 --> 20
//       */
//      int begin_of_page = ((int)map.get("now_page") - 1) * Grade.RECORD_PER_PAGE;
//      
//      // 시작 rownum 결정
//      // 1 페이지 = 0 + 1: 1
//      // 2 페이지 = 10 + 1: 11
//      // 3 페이지 = 20 + 1: 21
//      int start_num = begin_of_page + 1;
//      
//   // 종료 rownum
//      // 1 페이지 = 0 + 10: 10
//      // 2 페이지 = 10 + 10: 20
//      // 3 페이지 = 20 + 10: 30
//      int end_num = begin_of_page + Grade.RECORD_PER_PAGE;
//      /*
//       * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
//       * r >= 21 AND r <= 30
//       */
//
//      // System.out.println("begin_of_page: " + begin_of_page);
//      // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);
//      
//      // 계산된 값을 `HashMap`에 추가합니다.
//      map.put("start_num", start_num);
//      map.put("end_num", end_num);
//      
//      // 데이터베이스 쿼리 실행
//      ArrayList<GradeVO> list = this.gradeDAO.list_by_gradeno_search_paging(map);
//      
//      return list;
//    }

}
