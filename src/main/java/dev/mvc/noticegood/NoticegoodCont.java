package dev.mvc.noticegood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.calendar.CalendarVO;
import dev.mvc.cate.CateVOMenu;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/noticegood")
public class NoticegoodCont {
  
  @Autowired
  @Qualifier("dev.mvc.noticegood.NoticegoodProc")
  NoticegoodProcInter noticegoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  MemberProcInter memberProc;
  
  public NoticegoodCont() {
    System.out.println("-> NoticegoodCont created.");
  }
  
  /**
   * 등록
   * @param session
   * @param noticegoodVO
   * @return
   */
  @PostMapping(value = "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody NoticegoodVO noticegoodVO) {
    System.out.println("-> 수신 데이터: " + noticegoodVO.toString()); // json_src: {"current_passwd":"1234"}
    
//    int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    int memberno = 1; // 테스트 용
    noticegoodVO.setMemberno(memberno);
    
    int cnt = this.noticegoodProc.create(noticegoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
  /**
   * 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9093/noticegood/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<NoticegoodVO> list = this.noticegoodProc.list_all();
    model.addAttribute("list", list);

    return "/noticegood/list_all"; // /templates/noticegood/list_all.html
  }
  
  /**
   * 삭제 폼 http://localhost:9093/noticegood/delete?noticegoodno=1
   *
   */
  @GetMapping(path = "/delete/{noticegoodno}")
  public String delete(HttpSession session, 
      Model model, 
      @PathVariable("noticegoodno") int noticegoodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      NoticegoodVO noticegoodVO = this.noticegoodProc.read(noticegoodVO);
      model.addAttribute("noticegoodVO", noticegoodVO);

      return "/noticegood/delete"; // /templates/calendar/delete.html
    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/contents/msg"; // @GetMapping(value = "/msg")
      return "/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }
  }
  
  /**
   * 삭제 처리 http://localhost:9091/calendar/delete?calendarno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="noticegoodno", defaultValue = "0") int noticegoodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.noticegoodProc.delete(noticegoodno);

      return "redirect:/calendar/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/calendar/post2get"; // @GetMapping(value = "/msg")
    }

  }
}
