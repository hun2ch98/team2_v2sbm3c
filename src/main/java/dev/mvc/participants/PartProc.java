package dev.mvc.participants;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.survey.SurveyVO;
import dev.mvc.surveygood.SurveygoodVO;

@Component("dev.mvc.participants.PartProc")
public class PartProc implements PartProcInter {
  
  @Autowired
  PartDAOInter partDAO;

  @Override
  public int create(PartVO partVO) {
    int cnt = this.partDAO.create(partVO);
    return cnt;
  }
  
  @Override
  public int update_cnt(int itemno) {
    int cnt = this.partDAO.update_cnt(itemno);
    return cnt;
  }
  
  @Override
  public int updateCnt(HashMap<String, Object> map) {
    int cnt = this.partDAO.updateCnt(map);
    return cnt;
  }

  @Override
  public ArrayList<PartVO> list_all() {
    ArrayList<PartVO> list = this.partDAO.list_all();
    return list;
  }

  @Override
  public int delete(int pno) {
    int cnt = this.partDAO.delete(pno);
    return cnt;
  }
  
  @Override
  public PartVO read(int pno) {
    PartVO partVO = this.partDAO.read(pno);
    return partVO;
  }
  
  @Override
  public PartVO readByitemmember(HashMap<String, Object> map) {
    PartVO partVO = this.partDAO.readByitemmember(map);
    return partVO;
  }

  @Override
  public ArrayList<ItemMemberPartVO> list_all_join() {
    ArrayList<ItemMemberPartVO> list = this.partDAO.list_all_join();
    return list;
  }

  @Override
  public int count_search(HashMap<String, Object> Map) {
    int cnt = this.partDAO.count_search(Map);
    return cnt;
  }

  @Override
  public ArrayList<ItemMemberPartVO> list_search_paging(HashMap<String, Object> Map) {
    int now_page = 1; // 기본 페이지
    if (Map.get("now_page") != null) {
        now_page = (int) Map.get("now_page"); // 정상 값이 있을 경우 사용
    }

    ArrayList<ItemMemberPartVO> list = this.partDAO.list_search_paging(Map);

    return list;
  }

  @Override
  public String pagingBox(int pno, int now_page, String word, String list_file, int search_count, int record_per_page,
      int page_per_block) {
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
        str.append("<span class='span_box_1'><a href='" + list_file + "?pno="+pno+
                "&word=" + word + "&now_page=" + _now_page + "'>이전</a></span>");
    }

    // 현재 그룹의 페이지 링크
    for (int i = start_page; i <= end_page; i++) {
        if (i > total_page) break;
        if (i == now_page) {
            str.append("<span class='span_box_2'>" + i + "</span>");
        } else {
            str.append("<span class='span_box_1'><a href='" + list_file +"?pno="+pno+
                    "&word=" + word + "&now_page=" + i + "'>" + i + "</a></span>");
        }
    }

    // 다음 그룹 링크
    _now_page = now_grp * page_per_block + 1;
    if (now_grp < total_grp) {
        str.append("<span class='span_box_1'><a href='" + list_file + "?pno="+pno+
                "&word=" + word + "&now_page=" + _now_page + "'>다음</a></span>");
    }

    str.append("</div>");
    return str.toString();
  }



}
