package dev.mvc.boardgood;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.board.Board;
import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/boardgood")
public class BoardgoodCont {
  @Autowired
  @Qualifier("dev.mvc.boardgood.BoardgoodProc")
  BoardgoodProcInter boardgoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  public BoardgoodCont() {
    System.out.println("-> BoardgoodCont created.");
  }
  
  /**
   * POST 전송
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {

    return url; 
  }
  
  /**
   * 추천 생성
   */
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody BoardgoodVO boardgoodVO) {
    System.out.println("-> 수신 데이터: " + boardgoodVO.toString());
    
    int memberno = (int)session.getAttribute("memberno");
    boardgoodVO.setMemberno(memberno);
    
    int cnt = this.boardgoodProc.create(boardgoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
  /**
   * 서치 + 페이징 목록
   */
  @GetMapping(value = "/list_all")
  public String list_search_paging(HttpSession session, Model model,
      @ModelAttribute("boardgoodVO") BoardgoodVO boardgoodVO,
      @RequestParam(name = "goodno", defaultValue = "0") int goodno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    int record_per_page = 10;
    int start_row = (now_page - 1) * record_per_page + 1;
    int end_row = now_page * record_per_page;
    
    int memberno = (int)session.getAttribute("memberno");
    MemberVO memberVO = this.memberProc.read(memberno);
    if (memberVO == null) {
        memberVO = new MemberVO();
        memberVO.setMemberno(1);
        model.addAttribute("message", "회원 정보가 없습니다.");
    }
    
    word = Tool.checkNull(word).trim();
    model.addAttribute("memberVO", memberVO);
    model.addAttribute("goodno", goodno);
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    HashMap<String, Object> map = new HashMap<>();
    map.put("word", word);
    map.put("now_page", now_page);
    map.put("start_row", start_row);
    map.put("end_row", end_row);
    
    ArrayList<BoardgoodMemberVO> list = this.boardgoodProc.list_search_paging(map);
    model.addAttribute("list", list);
    
    int search_count = this.boardgoodProc.count_search(map);
//    System.out.println("search_count: " + search_count);
    String paging = this.boardgoodProc.pagingBox(memberno, now_page, word, "/boardgood/list_all", search_count,
        Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    model.addAttribute("search_count", search_count);


    int no = search_count - ((now_page - 1) * Board.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/boardgood/list_all"; 
  }
  
  
  
  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="goodno", defaultValue = "0") int goodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.boardgoodProc.delete(goodno);   // 삭제

      return "redirect:/boardgood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/boardgood/post2get"; // @GetMapping(value = "/msg")
    }

  }

}
