package dev.mvc.notice;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dev.mvc.member.MemberProcInter;
import dev.mvc.noticegood.NoticegoodProcInter;
import dev.mvc.noticegood.NoticegoodVO;
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
  public String update(HttpSession session, Model model,
      @RequestParam(name = "noticeno", defaultValue = "0") int noticeno,
      RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      NoticeVO noticeVO = this.noticeProc.read(noticeno);
      model.addAttribute("noticeVO", noticeVO);
      
      return "/notice/update";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  
  /** 수정 처리 */
  @PostMapping(value = "/update")
  public String update(HttpSession session,
      Model model,
      @ModelAttribute("noticeVO") NoticeVO noticeVO,
      RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.noticeProc.update(noticeVO); // 글수정
      
      return "redirect:/notice/read/" + noticeVO.getNoticeno();
    } else {
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/notice/post2get";
    }
  }
  
  /** 삭제 폼 */
  @GetMapping(path = "/delete/{noticeno}")
  public String delete(HttpSession session,
      Model model,
      @PathVariable("noticeno") int noticeno,
      RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한 경우
      NoticeVO noticeVO = this.noticeProc.read(noticeno);
      model.addAttribute("noticeVO", noticeVO);
      
//      NoticegoodVO noticegoodVO = this.noticegoodProc.read(noticeno)
      
      return "/notice/delete";
    } else {
      
      return "/member/login_cookie_need";
    }
  }
  
  /** 삭제 처리 */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session,
      Model model,
      @RequestParam(name = "noticeno", defaultValue = "0") int noticeno,
      RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.noticegoodProc.n_delete(noticeno); // 자식
      this.noticeProc.delete(noticeno); // 부모
      
      return "redirect:/notice/list_all";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/notice/post2get";
    }
  }
  
  /** 추천 처리 http://localhost:9093/notice/good */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"noticeno":"4"} 검증
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int noticeno = (int)src.get("noticeno"); // 값 가져오기
    System.out.println("-> noticeno: " + noticeno); // 검증
    
    if (this.memberProc.isMember(session)) { // 회원 로그인 확인
      // 추천을 한 상태인지 확인
      int memberno = (int)session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("noticeno", noticeno);
      map.put("memberno", memberno);
      
      int good_cnt = this.noticegoodProc.heart_Cnt(map);
      System.out.println("-> good_cnt: " + good_cnt); // 검증
      
      if (good_cnt == 1) {
        System.out.println("-> 추천 해제: " + noticeno + ' ' + memberno);
        NoticegoodVO noticegoodVO = this.noticegoodProc.readByNoticeMember(map);
        this.noticegoodProc.delete(noticegoodVO.getNoticegoodno()); // 추천 삭제
        this.noticeProc.decreaseGoodcnt(noticeno); // 카운트 감소
      } else {
        System.out.println("-> 추천: " + noticeno + ' ' + memberno);
        NoticegoodVO noticegoodVO_new = new NoticegoodVO();
        noticegoodVO_new.setNoticeno(noticeno);
        noticegoodVO_new.setMemberno(memberno);
        this.noticegoodProc.create(noticegoodVO_new);
        this.noticeProc.increaseGoodcnt(noticeno); // 카운트 증가
      }
      
      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴.
      int heart_Cnt = this.noticegoodProc.heart_Cnt(map);
      int goodcnt = this.noticeProc.read(noticeno).getGoodcnt();
          
      JSONObject result = new JSONObject();
      result.put("isMember", 1); // 로그인: 1, 비회원: 0
      result.put("heart_Cnt", heart_Cnt); // 추천 여부, 추천: 1, 비추천: 0
      result.put("goodcnt", goodcnt); //추천인수
      
      System.out.println("-> result.toString(): " + result.toString());
      
      return result.toString();
      
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isMember", 0); // 로그인: 1, 비회원: 0
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }
  }
}
