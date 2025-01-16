package dev.mvc.score;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("dev.mvc.score.ScoreProc")
public class ScoreProc implements ScoreProcInter {
	@Autowired // ScoreVOInter를 구현한 클래스의 객체를 자동으로 생성하여 ScoreVO 객체에 할당
    private ScoreDAOInter scoreDAO;

    public ScoreProc() {
        System.out.println("-> ScoreProc created.");
    }
    
    @Override
    public int create(ScoreVO scoreVO) {
        int cnt = this.scoreDAO.create(scoreVO);
        return cnt;
    }
    
    @Override
    public ArrayList<ScoreVO> list_all() {
        ArrayList<ScoreVO> list = this.scoreDAO.list_all();
        return list;
    }
    
    @Override
    public ArrayList<ScoreVO> list_by_scoreno(int scoreno) {
      ArrayList<ScoreVO> list = this.scoreDAO.list_by_scoreno(scoreno);
      return list;
    }
    
    /**
     * 조회
     */
    @Override
    public ScoreVO read(int scoreno) {
      ScoreVO scoreVO = this.scoreDAO.read(scoreno);
      return scoreVO;
    }
    
    @Override
    public ArrayList<ScoreVO> list_by_scoreno_search_paging(HashMap<String, Object> map) {
      // `now_page`를 기반으로 `startRow`와 `endRow`를 계산합니다.
      int now_page = (int) map.get("now_page");
      int record_per_page = 5; // 페이지당 레코드 수

      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      // 계산된 값을 `HashMap`에 추가합니다.
      map.put("startRow", startRow);
      map.put("endRow", endRow);

      // 데이터베이스 쿼리 실행
      ArrayList<ScoreVO> list = this.scoreDAO.list_by_scoreno_search_paging(map);
      return list;
    }
   
    @Override
    public String pagingBox(int now_page, int scoreno, String startDate, String endDate, int search_count,
            int record_per_page, int page_per_block) {
        int total_page = (int) (Math.ceil((double) search_count / record_per_page));
        int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
        int now_grp = (int) (Math.ceil((double) now_page / page_per_block));
        
        int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지
        int end_page = (now_grp * page_per_block); // 특정 그룹의 마지막 페이지
        
        StringBuffer str = new StringBuffer();

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
        str.append("    padding:1px 6px 1px 6px;");
        str.append("    margin:1px 2px 1px 2px;");
        str.append("  }");
        str.append("  .span_box_2{");
        str.append("    text-align: center;");
        str.append("    background-color: #668db4;");
        str.append("    color: #FFFFFF;");
        str.append("    font-size: 1em;");
        str.append("    border: 1px;");
        str.append("    border-style: solid;");
        str.append("    border-color: #cccccc;");
        str.append("    padding:1px 6px 1px 6px;");
        str.append("    margin:1px 2px 1px 2px;");
        str.append("  }");
        str.append("</style>");
        str.append("<div id='paging'>");
        
     // 이전 그룹으로 이동
        if (now_grp > 1) {
            int prev_page = (now_grp - 1) * page_per_block;
            str.append("<span class='span_box_1'><a href='")
                      .append("?scoreno=").append(scoreno)
                      .append("&startDate=").append(startDate)
                      .append("&endDate=").append(endDate)
                      .append("&now_page=").append(prev_page)
                      .append("'>이전</a></span>");
        }

        // 페이지 번호 출력
        for (int i = start_page; i <= end_page; i++) {
            if (i == now_page) { // 현재 페이지 강조
            	str.append("<span class='span_box_2'>").append(i).append("</span>");
            } else { // 다른 페이지는 링크 출력
            	str.append("<span class='span_box_1'><a href='")
            				.append("?scoreno=").append(scoreno)
            				.append("&startDate=").append(startDate)
            				.append("&endDate=").append(endDate)
                        .append("&now_page=").append(i)
                        .append("'>").append(i).append("</a></span>");
            }
        }

        // 다음 그룹으로 이동
        if (now_grp < total_grp) {
            int next_page = now_grp * page_per_block + 1;
            str.append("<span class='span_box_1'><a href='")
		            .append("?scoreno=").append(scoreno)
		            .append("&startDate=").append(startDate)
		            .append("&endDate=").append(endDate)
                    .append("&now_page=").append(next_page)
                    .append("'>다음</a></span>");
        }

        
        str.append("</div>");

        return str.toString();
    }
    
    @Override
    public int count_by_scoreno_search(HashMap<String, Object> map) {
      int cnt = this.scoreDAO.count_by_scoreno_search(map);
      return cnt;
    }
    
    @Override
    public ArrayList<ScoreVO> list_by_scoreno_search(HashMap<String, Object> hashMap) {
      ArrayList<ScoreVO> list = this.scoreDAO.list_by_scoreno_search(hashMap);
      return list;
    }

    @Override
    public int update_score(ScoreVO scoreVO) {
        int cnt = this.scoreDAO.update_score(scoreVO);
        return cnt;
    }
    
    @Override
    public int delete(int scoreno) {
        int cnt = this.scoreDAO.delete(scoreno);
        return cnt;
    }
    
}
