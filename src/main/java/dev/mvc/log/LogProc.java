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
  public int countSearchResults(String table, String action, String ip, String startDate, String endDate) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("table", table != null ? "%" + table.trim() + "%" : null);
      paramMap.put("action", action != null ? "%" + action.trim() + "%" : null);
      paramMap.put("ip", ip != null ? "%" + ip.trim() + "%" : null);
      paramMap.put("start_date", startDate != null ? startDate.trim() : null);
      paramMap.put("end_date", endDate != null ? endDate.trim() : null);

      return sqlSession.selectOne("dev.mvc.log.LogDAOInter.countSearchResults", paramMap);
  }
	
	@Override
  public ArrayList<LogVO> list_search_paging(String table, String action, String ip, String nowPage, String recordPerPage, int startNum, int endNum) {

      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("table", table != null ? table.trim() : "");
      paramMap.put("action", action != null ? action.trim() : "");
      paramMap.put("ip", ip != null ? ip.trim() : "");
      paramMap.put("startNum", startNum);
      paramMap.put("endNum", endNum);

      return logDAO.list_search_paging(paramMap);
  }
	
	
	@Override
  public String pagingBox(int now_page, String table, String action, String ip, String start_date, String end_date, String list_file_name, 
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
                    .append("?table=").append(table)
                    .append("?action=").append(action)
                    .append("?ip=").append(ip)
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
                        .append("?table=").append(table)
                        .append("?action=").append(action)
                        .append("?ip=").append(ip)
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
                    .append("?table=").append(table)
                    .append("?action=").append(action)
                    .append("?ip=").append(ip)
                    .append("&start_date=").append(start_date)
                    .append("&end_date=").append(end_date)
                    .append("&now_page=").append(next_page)
                    .append("'>다음</a></span>");
      }

      pagingHtml.append("</div>");
      return pagingHtml.toString();
  }
	
	
}
