package dev.mvc.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.diary.DiaryVO;

@Component("dev.mvc.log.LogProc")
public class LogProc implements LogProcInter{

	@Autowired
	private LogDAOInter logDAO;
	
	@Autowired
  private SqlSession sqlSession;
	
	public LogProc() {
		System.out.println("-> LogProc created.");
	}
	
	@Override
	public int create(LogVO logVO) {
		int cnt = this.logDAO.create(logVO);
		return cnt;
	}
	
	@Override
	public ArrayList<LogVO> list_all() {
		ArrayList<LogVO> list = this.logDAO.list_all();
		return list;
	}
	
	@Override
	public int delete(int logno) {
		int cnt = this.logDAO.delete(logno);
		return cnt;
	}
	
	@Override
  public LogVO read(int logno) {
    LogVO logVO = this.logDAO.read(logno);
    return logVO;
  }
	
	@Override
  public int countSearchResults(String table_name, String action, String ip, String is_success, String startDate, String endDate) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("table_name", table_name != null ? "%" + table_name.trim() + "%" : null);
      paramMap.put("action", action != null ? "%" + action.trim() + "%" : null);
      paramMap.put("ip", ip != null ? "%" + ip.trim() + "%" : null);
      paramMap.put("is_success", is_success != null ? "%" + is_success.trim() + "%" : null);
      paramMap.put("start_date", startDate != null ? startDate.trim() : null);
      paramMap.put("end_date", endDate != null ? endDate.trim() : null);
      int count = sqlSession.selectOne("dev.mvc.log.LogDAOInter.countSearchResults", paramMap);
      System.out.println("Search count: " + count); // 디버깅용 출력
      return count;
  }
	
	@Override
  public ArrayList<LogVO> list_search_paging(String table_name, String action, String ip,  String is_success, int now_page, int recordPerPage, int start_num, int end_num) {

      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("table_name", table_name != null ? table_name.trim() : "");
      paramMap.put("action", action != null ? action.trim() : "");
      paramMap.put("ip", ip != null ? ip.trim() : "");
      paramMap.put("is_success", is_success != null ? is_success.trim() : "");
      paramMap.put("start_num", start_num);
      paramMap.put("end_num", end_num);

      return logDAO.list_search_paging(paramMap);
  }
	
	
	@Override
	public String pagingBox(int now_page, String table_name, String action, String ip, String is_success, String start_date, String end_date, String list_file_name,
	                        int search_count, int record_per_page, int page_per_block) {
	    // 전체 페이지 수 계산
	    int total_page = (int) Math.ceil((double) search_count / record_per_page);
	    if (total_page == 0) total_page = 1; // 검색 결과가 없을 경우 최소 1페이지로 설정

	    // 디버깅 로그
	    System.out.println("search_count: " + search_count);
	    System.out.println("record_per_page: " + record_per_page);
	    System.out.println("total_page: " + total_page);

	    // 현재 페이지가 전체 페이지 수를 초과하거나 1보다 작을 경우 조정
	    if (now_page > total_page) {
	        now_page = total_page;
	    } else if (now_page < 1) {
	        now_page = 1;
	    }

	    // 전체 그룹 수 계산
	    int total_grp = (int) Math.ceil((double) total_page / page_per_block);
	    int now_grp = (int) Math.ceil((double) now_page / page_per_block);

	    // 현재 그룹의 시작 페이지와 끝 페이지
	    int start_page = ((now_grp - 1) * page_per_block) + 1;
	    int end_page = now_grp * page_per_block;
	    end_page = Math.min(end_page, total_page); // 마지막 페이지를 전체 페이지로 제한

	    // 디버깅 로그
	    System.out.println("now_page: " + now_page);
	    System.out.println("start_page: " + start_page);
	    System.out.println("end_page: " + end_page);

	    // 페이징 HTML 생성
	    StringBuilder pagingHtml = new StringBuilder();
	    pagingHtml.append("<style type='text/css'>");
	    pagingHtml.append("#paging {text-align: center; margin-top: 5px; font-size: 1em;}");
	    pagingHtml.append("#paging A:link {text-decoration:none; color:black; font-size: 1em;}");
	    pagingHtml.append("#paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}");
	    pagingHtml.append("#paging A:visited {text-decoration:none;color:black; font-size: 1em;}");
	    pagingHtml.append(".span_box_1{text-align: center; font-size: 1em; border: 1px solid #cccccc; padding:1px 6px; margin:1px 2px;}");
	    pagingHtml.append(".span_box_2{text-align: center; background-color: #668db4; color: #FFFFFF; font-size: 1em; border: 1px solid #cccccc; padding:1px 6px; margin:1px 2px;}");
	    pagingHtml.append("</style>");
	    pagingHtml.append("<div id='paging'>");

	    // 이전 그룹으로 이동
	    if (now_grp > 1) {
	        int prev_page = (now_grp - 1) * page_per_block;
	        pagingHtml.append("<span class='span_box_1'><a href='")
      	        .append(list_file_name)
                .append("?now_page=").append(prev_page)
                .append("&table_name=").append(table_name != null ? table_name : "")
                .append("&action=").append(action != null ? action : "")
                .append("&ip=").append(ip != null ? ip : "")
                .append("&is_success=").append(is_success != null ? is_success : "")
                .append("&start_date=").append(start_date != null ? start_date : "")
                .append("&end_date=").append(end_date != null ? end_date : "")
                .append("'>이전</a></span>");
      }
	    // 페이지 번호 출력
	    for (int i = start_page; i <= end_page; i++) {
	        if (i == now_page) { // 현재 페이지 강조
	            pagingHtml.append("<span class='span_box_2'>").append(i).append("</span>");
	        } else { // 다른 페이지는 링크 출력
	          pagingHtml.append("<span class='span_box_1'><a href='")
                    .append(list_file_name)
                    .append("?now_page=").append(i)
                    .append("&table_name=").append(table_name != null ? table_name : "")
                    .append("&action=").append(action != null ? action : "")
                    .append("&ip=").append(ip != null ? ip : "")
                    .append("&is_success=").append(is_success != null ? is_success : "")
                    .append("&start_date=").append(start_date != null ? start_date : "")
                    .append("&end_date=").append(end_date != null ? end_date : "")
                    .append("'>").append(i).append("</a></span>");
        }
	    }

	    // 다음 그룹으로 이동
	    if (now_grp < total_grp) {
	        int next_page = now_grp * page_per_block + 1;
	        pagingHtml.append("<span class='span_box_1'><a href='")
	                  .append(list_file_name).append("?now_page=").append(next_page)
	                  .append("'>다음</a></span>");
	    }

	    pagingHtml.append("</div>");
	    return pagingHtml.toString();
	}

	
	
}
