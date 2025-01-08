package dev.mvc.notice;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.noticegood.NoticegoodProcInter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/notice")
public class NoticeCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.notice.NoticeProc") // @Service("dev.mvc.notice.NoticeProc")
  private NoticeProcInter noticeProc;
  
  @Autowired
  @Qualifier("dev.mvc.noticegood.NoticegoodProc")
  private NoticegoodProcInter noticegoodProc;
  
  public NoticeCont() {
    System.out.println("-> NoticeCont created.");
  }
  
  @GetMapping(value = "/post2get")
  public String post2get(Model model,
      @RequestParam(name = "url", defaultValue = "") String url) {
    
    return url;
  }
  
  /**
   * 공지사항 등록 폼
   * http://localhost:9093/notice/create
   */
  @GetMapping(value = "/create")
  public String create(Model model) {
    
    return "/notice/create"; // /templates/notice/create.html
  }
  
  /**
   * 공지사항 등록 처리 -> http://localhost:9093/notice/create
   */
  @PostMapping(value = "/create")
  public String create(HttpSession session, Model model,
      @ModelAttribute("noticeVO") NoticeVO noticeVO) {
    
    // int memberno = (int)session.getAttribute("memberno");
    int memberno = 1; // 테스트용
    noticeVO.setMemberno(memberno);
    
    int cnt = this.noticeProc.create(noticeVO);
    
    if (cnt == 1) {
      
      return "redirect:/notice/list_all"; // @GetMapping(value = " /list_all")
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
    
    return "/notice/msg"; // /templates/notice/msg.html
  }
  
  /** 목록 */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<NoticeVO> list = this.noticeProc.list_all();
    model.addAttribute("list", list);
    
    return "/notice/list_all"; // /templates/notice/list_all.html
  }
  
  /** 조회 http://localhost:9093/notice/read/1 */
  @GetMapping(path = "/read/{noticeno}")
  public String read(HttpSession session, 
      Model model, 
      @PathVariable("noticeno") int noticeno) {
    
    this.noticeProc.increaseCnt(noticeno); // 조회수 증가
    
    NoticeVO noticeVO = this.noticeProc.read(noticeno);
    
    model.addAttribute("noticeVO", noticeVO);
    
    // --------------------------------------------------------------
    // 추천 관련
    // --------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("noticeno", noticeno);
    
    int heart_Cnt = 0;
    if (session.getAttribute("memberno") != null) { // 회원인 경우만 카운트 처리
      int memberno = (int)session.getAttribute("memberno");
      map.put("memberno", memberno);
      
      heart_Cnt = this.noticegoodProc.heart_Cnt(map);
    } 
    model.addAttribute("heart_Cnt", heart_Cnt);
    // --------------------------------------------------------------
    
    return "/notice/read";
  }
  
  /** 수정 폼 http://localhost:9093/notice/update?noticeno0=1 */
  @GetMapping(value = "/update")
  public String update_text(HttpSession session,
      Model model,
      @RequestParam(name = "noticeno", defaultValue = "0") int noticeno,
      RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      NoticeVO noticeVO = this.noticeProc.read(noticeno);
      model.addAttribute("noticeVO", noticeVO);
      
      return "/notice/update";
    } else {
      return "/member/login_cookie_need";
    }
  }
  
  /** 수정 처리 http://localhost:9093/notice/update?noticeno0=1 */
}
