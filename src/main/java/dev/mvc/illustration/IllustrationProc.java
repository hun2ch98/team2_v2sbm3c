package dev.mvc.illustration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dev.mvc.diary.DiaryVO;

@Component("dev.mvc.illustration.IllustrationProc")
public class IllustrationProc implements IllustrationProcInter {

    @Autowired
    private IllustrationDAOInter illustrationDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    

    
    @Override
    public int create(IllustrationVO illustrationVO) {
        return illustrationDAO.create(illustrationVO);
    }
    

    @Override
    public IllustrationVO read(int illustno) {
        String sql = "SELECT * FROM illustration WHERE illustno = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, illustno);

        IllustrationVO illustrationVO = new IllustrationVO();
        illustrationVO.setIllustno(((BigDecimal) map.get("illustno")).intValue());
        illustrationVO.setIllust_thumb((String) map.get("illust_thumb"));
        illustrationVO.setIllust_saved((String) map.get("illust_saved"));
        illustrationVO.setIllust((String) map.get("illust"));
        illustrationVO.setIllust_size(((BigDecimal) map.get("illust_size")).intValue());
        illustrationVO.setDiaryno(((BigDecimal) map.getOrDefault("diaryno", BigDecimal.ZERO)).intValue());

        return illustrationVO;
    }


    @Override
    public DiaryVO readDiary(int diaryno) {
        String sql = "SELECT * FROM diary WHERE diaryno = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, diaryno);

        DiaryVO diaryVO = new DiaryVO();
        diaryVO.setDiaryno(((BigDecimal) map.get("diaryno")).intValue());
        diaryVO.setTitle((String) map.get("title"));
        java.sql.Timestamp timestamp = (java.sql.Timestamp) map.get("ddate");
        if (timestamp != null) {
            diaryVO.setDdate(new java.sql.Date(timestamp.getTime()));
        }
        return diaryVO;
    }
    
    @Override
    public int delete(int illustno) {
      int cnt = this.illustrationDAO.delete(illustno);
      return cnt;
    }
    
    @Override
    public int update(IllustrationVO illustrationVO) {
      int cnt = this.illustrationDAO.update(illustrationVO);
      return cnt;
    }
    
    public List<IllustrationVO> getIllustrationsByDiaryNo(int diaryno) {
      return illustrationDAO.getIllustrationsByDiaryNo(diaryno);
  }
    
    @Override
    public ArrayList<IllustrationVO> list_search(String title, String date) {
      ArrayList<IllustrationVO> list = this.illustrationDAO.list_search(title, date);
      return list;
    }
    
    @Override
    public int list_search_count(String title, String date) {
        return illustrationDAO.list_search_count(title, date);
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
      if (search_count == 0) {
        now_page = 1;
        total_page = 1;
      } else if (now_page > total_page) {
          now_page = total_page; // 마지막 페이지로 조정
      } else if (now_page < 1) {
          now_page = 1; // 최소 페이지는 1
      }
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

  public List<Map<String, Object>> listAllWithDiaryDetails() {
    return illustrationDAO.listAllWithDiaryDetails();
  }
    
  @Override
  public List<Map<String, Object>> list_search_paging(String title, int now_page, int record_per_page, int start_num, int end_num, String start_date, String end_date) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("title", title != null ? title.trim() : "");
      paramMap.put("startNum", start_num);
      paramMap.put("endNum", end_num);
      paramMap.put("start_date", start_date != null && !start_date.isEmpty() ? start_date : null);
      paramMap.put("end_date", end_date != null && !end_date.isEmpty() ? end_date : null);

      return illustrationDAO.list_search_paging(paramMap);
  }


    
    @Autowired
    private SqlSession sqlSession;
    
    @Override
    public int countSearchResults(String title, String start_date, String end_date) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", title != null ? "%" + title.trim() + "%" : null);
        paramMap.put("start_date", start_date != null ? start_date.trim() : null);
        paramMap.put("end_date", end_date != null ? end_date.trim() : null);
        int count = sqlSession.selectOne("dev.mvc.illustration.IllustrationDAOInter.countSearchResults", paramMap);
        System.out.println("Search count: " + count); // 디버깅용 출력
        return count;
    }
    
    @Override
    public int cntcount(int illustno) {
      return illustrationDAO.cntcount(illustno);
    }


    @Override
    public ArrayList<IllustrationVO> get_illust(int diaryno) {
      ArrayList <IllustrationVO> get_illust = this.illustrationDAO.get_illust(diaryno);
      return get_illust;
    }
    
    
}