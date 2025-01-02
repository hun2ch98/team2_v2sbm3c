package dev.mvc.illustration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.diary.DiaryVO;

@Component("dev.mvc.illustration.IllustrationProc")
public class IllustrationProc implements IllustrationProcInter {

    @Autowired
    private IllustrationDAOInter illustrationDAO;

    @Override
    public int create(IllustrationVO illustrationVO) {
        return illustrationDAO.create(illustrationVO);
    }

    @Override
    public ArrayList<IllustrationVO> list_all() {
        return illustrationDAO.list_all();
    }

    @Override
    public ArrayList<IllustrationVO> list_by_illustno(int illustno) {
        return illustrationDAO.list_by_illustno(illustno);
    }

    @Override
    public IllustrationVO read(int illustno) {
        return illustrationDAO.read(illustno);
    }

   
    @Override
    public int update_file(IllustrationVO illustrationVO) {
        return illustrationDAO.update_file(illustrationVO);
    }

    @Override
    public int delete(int illustno) {
        return illustrationDAO.delete(illustno);
    }


    @Override
    public ArrayList<IllustrationVO> list_by_illustno_search_paging(HashMap<String, Object> map) {
        return illustrationDAO.list_by_illustno_search_paging(map);
    }

    @Override
    public int count_by_illustno(int illustno) {
        return illustrationDAO.count_by_illustno(illustno);
    }

    @Override
    public Date getDiaryDateByIllustNo(int illustno) {
        return illustrationDAO.getDiaryDateByIllustNo(illustno);
    }

    @Override
    public ArrayList<IllustrationVO> listByIllustNoSearchPaging(HashMap<String, Object> paramMap) {
        return illustrationDAO.listByIllustNoSearchPaging(paramMap);
    }

    @Override
    public int searchCount(String startDate, String endDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start_date", startDate);
        paramMap.put("end_date", endDate);
        return illustrationDAO.searchCount(paramMap);
    }

    @Override
    public ArrayList<IllustrationVO> list_search(String date) {
      ArrayList<IllustrationVO> list = this.illustrationDAO.list_search(date);
      return list;
    }

    @Override
    public int list_search_count(String date) {
        return illustrationDAO.list_search_count(date);
    }

    @Override
    public ArrayList<IllustrationVO> list_search_paging(String nowPage, String recordPerPage, int startNum, int endNum) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startNum", startNum);
        paramMap.put("endNum", endNum);

        return illustrationDAO.list_search_paging(paramMap);
    }
 
    @Override
    public String pagingBox(int now_page, String start_date, String end_date, String list_file_name, 
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
                      .append("&start_date=").append(start_date)
                      .append("&end_date=").append(end_date)
                      .append("&now_page=").append(next_page)
                      .append("'>다음</a></span>");
        }

        pagingHtml.append("</div>");
        return pagingHtml.toString();
    }

    @Override
    public String pagingBox(int nowPage, int totalCount, int recordPerPage, int pagePerBlock, String listFileName) {
        int totalPage = (int) Math.ceil((double) totalCount / recordPerPage); // 전체 페이지 수
        int startPage = ((nowPage - 1) / pagePerBlock) * pagePerBlock + 1;    // 현재 블록의 시작 페이지 번호
        int endPage = Math.min(startPage + pagePerBlock - 1, totalPage);     // 현재 블록의 마지막 페이지 번호

        StringBuilder sb = new StringBuilder();
        sb.append("<ul class='pagination'>");

        // 이전 블록으로 이동
        if (startPage > 1) {
            sb.append("<li><a href='").append(listFileName)
              .append("?now_page=").append(startPage - 1).append("'>이전</a></li>");
        }

        // 현재 블록의 페이지 목록 생성
        for (int i = startPage; i <= endPage; i++) {
            if (i == nowPage) {
                sb.append("<li class='active'><a>").append(i).append("</a></li>");
            } else {
                sb.append("<li><a href='").append(listFileName)
                  .append("?now_page=").append(i).append("'>").append(i).append("</a></li>");
            }
        }

        // 다음 블록으로 이동
        if (endPage < totalPage) {
            sb.append("<li><a href='").append(listFileName)
              .append("?now_page=").append(endPage + 1).append("'>다음</a></li>");
        }

        sb.append("</ul>");
        return sb.toString();
    }

    
    @Override
    public int cntcount(int illustno) {
        return illustrationDAO.cntcount(illustno);
    }

    @Override
    public ArrayList<IllustrationVO> listSearch(String startDate, String endDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start_date", startDate);
        paramMap.put("end_date", endDate);

        return illustrationDAO.listSearch(paramMap);
    }

    
    @Autowired
    private SqlSession sqlSession;
    
    @Override
    public int countSearchResults(String startDate, String endDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start_date", startDate != null ? startDate.trim() : null);
        paramMap.put("end_date", endDate != null ? endDate.trim() : null);

        return sqlSession.selectOne("countSearchResults", paramMap);
    }


    //2323
    @Override
    public ArrayList<IllustrationVO> listByIllustrationPaging(int startNum, int endNum) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("startNum", startNum);
        map.put("endNum", endNum);
        return illustrationDAO.listByIllustrationPaging(map);
    }

    @Override
    public int countAllIllustrations() {
        return illustrationDAO.countAllIllustrations();
    }
    
    @Override
    public int list_by_illustno_search_count(HashMap<String, Object> map) {
        return illustrationDAO.list_by_illustno_search_count(map);
    }

   
    
}
