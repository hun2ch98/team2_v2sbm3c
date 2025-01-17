package dev.mvc.surveygood;

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

import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.participants.ItemMemberPartVO;
import dev.mvc.participants.PartVO;
import dev.mvc.survey.Survey;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/surveygood")
public class SurveygoodCont {
  @Autowired
  @Qualifier("dev.mvc.surveygood.SurveygoodProc")
  SurveygoodProcInter surveygoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public SurveygoodCont() {
    System.out.println("-> SurveygoodCont 생성됨.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  /**
   * 추천 생성
   */
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody SurveygoodVO surveygoodVO) {
    System.out.println("-> 수신 데이터: " + surveygoodVO.toString());
    
    int memberno = (int)session.getAttribute("memberno");
    surveygoodVO.setMemberno(memberno);
    
    int cnt = this.surveygoodProc.create(surveygoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
  /**
   * 목록
   * @param model
   * @return
   */
  // http://localhost:9091/cate/list_all
  @GetMapping(value = "/list_all")
  public String list_search_paging(HttpSession session, Model model,
      @ModelAttribute("surveygoodVO") SurveygoodVO surveygoodVO,
      @RequestParam(name = "goodno", defaultValue = "0") int goodno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    int record_per_page = 5;
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
    
    ArrayList<ItemMemberPartVO> list = this.surveygoodProc.list_search_paging(map);
    model.addAttribute("list", list);
    
    int search_count = this.surveygoodProc.count_search(map);
//    System.out.println("search_count: " + search_count);
    String paging = this.surveygoodProc.pagingBox(memberno, now_page, word, "/surveygood/list_all", search_count,
        Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    model.addAttribute("search_count", search_count);


    int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/surveygood/list_all"; // /templates/calendar/list_all.html
  }
  
  /**
   * 삭제 처리 http://localhost:9091/contentsgood/delete?goodno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="goodno", defaultValue = "0") int goodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.surveygoodProc.delete(goodno);   // 삭제

      return "redirect:/surveygood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/surveygood/post2get"; // @GetMapping(value = "/msg")
    }

  }

}
