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

import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.log.LogProcInter;
import dev.mvc.log.LogVO;
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
  
  @Autowired
  @Qualifier("dev.mvc.log.LogProc")
  private LogProcInter logProc;

  private void logAction(String action, String table, int memberno, String details, HttpServletRequest request, String is_success) {
      LogVO logVO = new LogVO();
      logVO.setMemberno(memberno);
      logVO.setTable_name(table);
      logVO.setAction(action);
      logVO.setDetails(details);
      logVO.setIp(getClientIp(request)); // IP 주소 설정
      logVO.setIs_success(is_success);
      logProc.create(logVO); // Log 테이블에 삽입
  }

  private String getClientIp(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();
      }
      return ip;
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
  public String create(HttpSession session, Model model, HttpServletRequest request,
      @ModelAttribute("noticeVO") NoticeVO noticeVO) {
    
    int memberno = (int) session.getAttribute("memberno");
    noticeVO.setMemberno(memberno);
    
    int cnt = this.noticeProc.create(noticeVO);
    
    if (cnt == 1) {
      logAction("create", "notice", memberno, "title=" + noticeVO.getTitle(), request, "Y");
      return "redirect:/notice/list_all"; // @GetMapping(value = " /list_all")
    } else {
      logAction("create", "notice", memberno, "title=" + noticeVO.getTitle(), request, "N");
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
    
    return "/notice/msg"; // /templates/notice/msg.html
  }
  
  /** 목록 */
  @GetMapping(value = "/list_all")
  public String list_all(Model model, HttpSession session,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "searchType", required = false) String searchType,
      @RequestParam(name = "keyword", defaultValue = "") String keyword) {
    if (this.memberProc.isMember(session)) {
      
      // 검색 조건
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);
      searchDTO.setSize(page * 10);
      searchDTO.setOffset((page - 1) * 10);
      
      // 전체 공지사항 수 조회
      int total = this.noticeProc.list_search_count(searchDTO);
      
      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if(total == 0 && page > 1) {
        return "redirectL/notice/list_all?searchType=" + searchType + "&keyword=" + keyword;
      }
      
      // 페이징 정보 계싼
      PageDTO pageDTO = new PageDTO(total, page);
      
      // 공지사항 목록 조회
      ArrayList<NoticeVO> list = this.noticeProc.list_search_paging(searchDTO);
      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("total", total);
      
      return "/notice/list_search"; // /templates/notice/list_all.html
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  
  /** 조회 http://localhost:9093/notice/read/1 */
  @GetMapping(path = "/read/{noticeno}")
  public String read(HttpSession session, 
      Model model, HttpServletRequest request,
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
      logAction("read", "notice", memberno, "title=" + noticeVO.getTitle(), request, "Y");
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
      Model model, HttpServletRequest request,
      @ModelAttribute("noticeVO") NoticeVO noticeVO,
      RedirectAttributes ra) {
    int memberno = (int) session.getAttribute("memberno");
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.noticeProc.update(noticeVO); // 글수정
      logAction("update", "notice", memberno, "title=" + noticeVO.getTitle(), request, "Y");
      return "redirect:/notice/read/" + noticeVO.getNoticeno();
    } else {
      logAction("update", "notice", memberno, "title=" + noticeVO.getTitle(), request, "N");
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
      Model model, HttpServletRequest request,
      @RequestParam(name = "noticeno", defaultValue = "0") int noticeno,
      RedirectAttributes ra) {
    NoticeVO noticeVO = this.noticeProc.read(noticeno);
    int memberno = (int) session.getAttribute("memberno");
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.noticegoodProc.n_delete(noticeno); // 자식
      this.noticeProc.delete(noticeno); // 부모
      logAction("delete", "notice", memberno, "title=" + noticeVO.getTitle(), request, "Y");
      return "redirect:/notice/list_all";
    } else {
      logAction("delete", "notice", memberno, "title=" + noticeVO.getTitle(), request, "N");
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/notice/post2get";
    }
  }
  
  /** 추천 처리 http://localhost:9093/notice/good */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, HttpServletRequest request, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"noticeno":"4"} 검증
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int noticeno = (int)src.get("noticeno"); // 값 가져오기
    NoticeVO noticeVO = this.noticeProc.read(noticeno);
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
        logAction("decreaseGoodcnt", "notice", memberno, "title=" + noticeVO.getTitle(), request, "Y");
      } else {
        System.out.println("-> 추천: " + noticeno + ' ' + memberno);
        NoticegoodVO noticegoodVO_new = new NoticegoodVO();
        noticegoodVO_new.setNoticeno(noticeno);
        noticegoodVO_new.setMemberno(memberno);
        this.noticegoodProc.create(noticegoodVO_new);
        this.noticeProc.increaseGoodcnt(noticeno); // 카운트 증가
        logAction("increaseGoodcnt", "notice", memberno, "title=" + noticeVO.getTitle(), request, "Y");
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
