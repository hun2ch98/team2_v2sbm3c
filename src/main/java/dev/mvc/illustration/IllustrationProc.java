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
    public int update_text(IllustrationVO illustrationVO) {
        return illustrationDAO.update_text(illustrationVO);
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
    public int list_by_illustno_search_count(HashMap<String, Object> hashMap) {
        return illustrationDAO.list_by_illustno_search_count(hashMap);
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
    public String pagingBox(int now_page, String start_date, String end_date, String list_file_name, int search_count, int record_per_page, int page_per_block) {
        // 페이징 HTML 생성 로직
        int total_page = (int) Math.ceil((double) search_count / record_per_page);
        int total_grp = (int) Math.ceil((double) total_page / page_per_block);
        int now_grp = (int) Math.ceil((double) now_page / page_per_block);

        int start_page = ((now_grp - 1) * page_per_block) + 1;
        int end_page = now_grp * page_per_block;
        end_page = Math.min(end_page, total_page);

        StringBuilder pagingHtml = new StringBuilder();
        pagingHtml.append("<div id='paging'>");
        if (now_grp > 1) {
            int prev_page = (now_grp - 1) * page_per_block;
            pagingHtml.append("<a href='").append(list_file_name)
                    .append("?start_date=").append(start_date)
                    .append("&end_date=").append(end_date)
                    .append("&now_page=").append(prev_page)
                    .append("'>이전</a>");
        }
        for (int i = start_page; i <= end_page; i++) {
            if (i == now_page) {
                pagingHtml.append("<span>").append(i).append("</span>");
            } else {
                pagingHtml.append("<a href='").append(list_file_name)
                        .append("?start_date=").append(start_date)
                        .append("&end_date=").append(end_date)
                        .append("&now_page=").append(i)
                        .append("'>").append(i).append("</a>");
            }
        }
        if (now_grp < total_grp) {
            int next_page = now_grp * page_per_block + 1;
            pagingHtml.append("<a href='").append(list_file_name)
                    .append("?start_date=").append(start_date)
                    .append("&end_date=").append(end_date)
                    .append("&now_page=").append(next_page)
                    .append("'>다음</a>");
        }
        pagingHtml.append("</div>");
        return pagingHtml.toString();
    }
    
    @Override
    public int countByDateRange(HashMap<String, Object> paramMap) {
        return illustrationDAO.countByDateRange(paramMap);
    }
    
}
