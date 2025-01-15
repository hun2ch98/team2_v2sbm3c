package dev.mvc.participants;

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
import dev.mvc.survey.Survey;
import dev.mvc.survey.SurveyVO;
import dev.mvc.surveygood.SurveygoodProcInter;
import dev.mvc.surveygood.SurveygoodVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/participants")
public class PartCont {
  
  @Autowired
  @Qualifier("dev.mvc.participants.PartProc")
  PartProcInter partProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public PartCont() {
    System.out.println("-> PartCont created.");
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

    return url; 
  }
  
  /**
   * 등록
   * @param session
   * @param surveygoodVO
   * @return
   */
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody PartVO partVO) {
    System.out.println("-> 수신 데이터: " + partVO.toString());
    
    int memberno = 2;
//    int memberno = (int)session.getAttribute("memberno");
    partVO.setMemberno(memberno);
    
    int cnt = this.partProc.create(partVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_search_paging(HttpSession session, Model model,
      @ModelAttribute("partVO") PartVO partVO,
      @RequestParam(name = "pno", defaultValue = "0") int pno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인한 경우
      int record_per_page = 10;
      int start_row = (now_page - 1) * record_per_page + 1;
      int end_row = now_page * record_per_page;
//      System.out.println("Search word: " + word);
      
      int memberno = (int)session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          memberVO = new MemberVO();
          memberVO.setMemberno(1);
          model.addAttribute("message", "회원 정보가 없습니다.");
      }
      
      word = Tool.checkNull(word).trim();
      model.addAttribute("memberVO", memberVO);
      model.addAttribute("pno", pno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      HashMap<String, Object> map = new HashMap<>();
//      map.put("memberno", memberno);
//      map.put("pno", pno);
      map.put("word", word);
      map.put("now_page", now_page);
      map.put("start_row", start_row);
      map.put("end_row", end_row);
      
      ArrayList<ItemMemberPartVO> list = this.partProc.list_search_paging(map);
      model.addAttribute("list", list);
      
      int search_count = this.partProc.count_search(map);
//      System.out.println("search_count: " + search_count);
      String paging = this.partProc.pagingBox(memberno, now_page, word, "/participants/list_all", search_count,
          Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
      model.addAttribute("paging", paging);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);


      int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
      model.addAttribute("no", no);

    return "/participants/list_all"; 
    } else {
      return "member/login_cookie_need";
    }
  }
  
  /**
   * 삭제 처리 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="pno", defaultValue = "0") int pno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.partProc.delete(pno);   // 삭제

      return "redirect:/participants/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/participants/post2get"; // @GetMapping(value = "/msg")
    }

  }
  

}
