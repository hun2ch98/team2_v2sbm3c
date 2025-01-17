package dev.mvc.boardgood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.participants.ItemMemberPartVO;
import dev.mvc.surveygood.SurveygoodVO;

@Component("dev.mvc.boardgood.BoardgoodProc")
public class BoardgoodProc implements BoardgoodProcInter{
  
  @Autowired
  BoardgoodDAOInter boardgoodDAO;

  @Override
  public int create(BoardgoodVO boardgoodVO) {
    int cnt = this.boardgoodDAO.create(boardgoodVO);
    return cnt;
  }

  @Override
  public ArrayList<BoardgoodVO> list_all() {
    ArrayList<BoardgoodVO> list = this.boardgoodDAO.list_all();
    return list;
  }

  @Override
  public int delete(int goodno) {
    int cnt = this.boardgoodDAO.delete(goodno);
    return cnt;
  }

  @Override
  public int heart_cnt(HashMap<String, Object> map) {
    int cnt = this.boardgoodDAO.heart_cnt(map);
    return cnt;
  }

  @Override
  public BoardgoodVO read(int goodno) {
    BoardgoodVO boardgoodVO = this.boardgoodDAO.read(goodno);
    return boardgoodVO;
  }

  @Override
  public BoardgoodVO readByboardmember(HashMap<String, Object> map) {
    BoardgoodVO boardgoodVO = this.boardgoodDAO.readByboardmember(map);
    return boardgoodVO;
  }

  @Override
  public ArrayList<BoardgoodMemberVO> list_all_join() {
    ArrayList<BoardgoodMemberVO> list = this.boardgoodDAO.list_all_join();
    return list;
  }

  @Override
  public int count_search(HashMap<String, Object> map) {
    int cnt = this.boardgoodDAO.count_search(map);
    return cnt;
  }

  @Override
  public ArrayList<BoardgoodMemberVO> list_search_paging(HashMap<String, Object> map) {
    int now_page = 1; // 기본 페이지
    if (map.get("now_page") != null) {
        now_page = (int) map.get("now_page"); // 정상 값이 있을 경우 사용
    }

    ArrayList<BoardgoodMemberVO> list = this.boardgoodDAO.list_search_paging(map);

    return list;
  }
  
  @Override
  public String pagingBox(int goodno, int now_page, String word, String list_file, int search_count, int record_per_page,
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
        str.append("<span class='span_box_1'><a href='" + list_file + "?goodno="+goodno+
                "&word=" + word + "&now_page=" + _now_page + "'>이전</a></span>");
    }

    // 현재 그룹의 페이지 링크
    for (int i = start_page; i <= end_page; i++) {
        if (i > total_page) break;
        if (i == now_page) {
            str.append("<span class='span_box_2'>" + i + "</span>");
        } else {
            str.append("<span class='span_box_1'><a href='" + list_file +"?goodno="+goodno+
                    "&word=" + word + "&now_page=" + i + "'>" + i + "</a></span>");
        }
    }

    // 다음 그룹 링크
    _now_page = now_grp * page_per_block + 1;
    if (now_grp < total_grp) {
        str.append("<span class='span_box_1'><a href='" + list_file + "?goodno="+goodno+
                "&word=" + word + "&now_page=" + _now_page + "'>다음</a></span>");
    }

    str.append("</div>");
    return str.toString();
  }


}
