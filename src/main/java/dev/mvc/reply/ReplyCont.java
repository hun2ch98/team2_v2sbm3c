package dev.mvc.reply;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.board.Board;
import dev.mvc.board.BoardProcInter;
import dev.mvc.board.BoardVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "/reply")
@Controller
public class ReplyCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.board.BoardProc") // @Component("dev.mvc.board.BoardProc")
  private BoardProcInter boardProc;

  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc") // @Component("dev.mvc.contents.ContentsProc")
  private ReplyProcInter replyProc;

  public ReplyCont() {
    System.out.println("-> ReplyCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue="") String url) {
    
//    ArrayList<BoardVOMenu> menu = this.boardProc.menu();
//    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, contents 테이블은 FK로 cateno를 사용함.
  // http://localhost:9093/contents/create X
  // http://localhost:9093/contents/create?cateno=1 // cateno 변수값을 보내는 목적
  // http://localhost:9093/contents/create?cateno=2
  // http://localhost:9093/contents/create?cateno=5
  /**
   * 댓글 생성
   * @param model
   * @param ReplyVO
   * @param replyno
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("replyVO") ReplyVO replyVO, 
      @RequestParam(name="boardno", defaultValue="0") int boardno) {
//    ArrayList<BoardVOMenu> menu = this.boardProc.menu();
//    model.addAttribute("menu", menu);

    BoardVO boardVO = this.boardProc.read(boardno); // 게시글 정보를 출력하기위한 목적
    model.addAttribute("boardVO", boardVO);

    return "/reply/create"; // /templates/reply/create.html
  }

  /**
   * 등록 처리 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
      HttpSession session, 
      Model model, 
      @ModelAttribute("replyVO") ReplyVO replyVO,
      RedirectAttributes ra) {
    
    String passwd = replyVO.getPasswd();
    if (passwd != null && passwd.length() > 10) { // 10자로 제한
        ra.addFlashAttribute("code", "passwd_length_exceeded"); // 길이 초과 오류
        ra.addFlashAttribute("cnt", 0); // 등록 실패
        ra.addFlashAttribute("url", "/reply/msg"); // msg.html, redirect parameter 적용
        return "redirect:/reply/msg"; // Post -> Get - param...
    }
    
      // 비회원의 경우 memberno를 0으로 설정
      int memberno = 0;
      if (memberProc.isMember(session)) { // 로그인한 경우
          memberno = (int) session.getAttribute("memberno"); // memberno FK
      }
      replyVO.setMemberno(memberno); // memberno를 설정 (비회원은 0)

      // 댓글 등록 처리
      int cnt = this.replyProc.create(replyVO);

      if (cnt == 1) {
          // 댓글 등록 성공 시
          ra.addAttribute("boardno", replyVO.getBoardno()); // controller -> controller
          return "redirect:/reply/list_by_boardno";
      } else {
          // 댓글 등록 실패 시
          ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
          ra.addFlashAttribute("cnt", 0); // 등록 실패
          ra.addFlashAttribute("url", "/reply/msg"); // msg.html, redirect parameter 적용
          return "redirect:/reply/msg"; // Post -> Get - param...
      }
  }
  
  /**
   * 댓글 목록 페이지
   * @param replyno
   * @param model
   * @return
   */
  @GetMapping("/list_by_replyno")
  public String listByReplyno(@RequestParam(name = "replyno", defaultValue = "0") int replyno, Model model) {
      ArrayList<ReplyVO> list = this.replyProc.list_by_replyno(replyno);
      model.addAttribute("list", list);
      return "/reply/list_by_replyno";
  }

  /**
   * 전체 목록
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
      // 댓글 목록 조회
      ArrayList<ReplyVO> list = this.replyProc.list_all(); // 모든 댓글 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // 필요한 경우, 댓글 목록을 안전하게 변환하거나 필터링하는 로직 추가 가능
      model.addAttribute("list", list);

      // 댓글 목록 페이지로 이동
      return "/reply/list_all";
  }
  

//  /**
//   * 유형 1
//   * 카테고리별 목록
//   * http://localhost:9093/contents/list_by_cateno?cateno=5
//   * http://localhost:9093/contents/list_by_cateno?cateno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_cateno")
//  public String list_by_cateno(HttpSession session, Model model, 
//      @RequestParam(name="cateno", defaultValue = "") int cateno) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
//    
//     CateVO cateVO = this.cateProc.read(cateno);
//     model.addAttribute("cateVO", cateVO);
//    
//    ArrayList<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//
//    return "/contents/list_by_cateno";
//  }

//  /**
//   * 유형 2
//   * 카테고리별 목록 + 검색
//   * http://localhost:9093/contents/list_by_cateno?cateno=5
//   * http://localhost:9093/contents/list_by_cateno?cateno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_cateno")
//  public String list_by_cateno_search(HttpSession session, Model model, 
//                                                   @RequestParam(name="cateno", defaultValue = "0" ) int cateno, 
//                                                   @RequestParam(name="word", defaultValue = "") String word) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
//    
//     CateVO cateVO = this.cateProc.read(cateno);
//     model.addAttribute("cateVO", cateVO);
//    
//     word = Tool.checkNull(word).trim(); // 검색어 공백 삭제
//     
//     HashMap<String, Object> map = new HashMap<>();
//     map.put("cateno", cateno);
//     map.put("word", word);
//     
//    ArrayList<ContentsVO> list = this.contentsProc.list_by_cateno_search(map);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//    
//    int search_count = this.contentsProc.list_by_cateno_search_count(map);
//    model.addAttribute("search_count", search_count);
//    
//    return "/contents/list_by_cateno_search"; // /templates/contents/list_by_cateno_search.html
//  }

  /**
   * 댓글 조회
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name = "replyno") int replyno, // 경로 변수로 replyno를 사용
      @RequestParam(name = "word", defaultValue = "") String word, 
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      // 댓글 데이터 조회
      ReplyVO replyVO = this.replyProc.read(replyno); // 댓글 번호로 조회
      
      model.addAttribute("replyVO", replyVO); // 댓글 데이터 추가

      // 추가적인 요청 파라미터 처리
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      return "/reply/read"; // 댓글 상세보기 페이지
  }


  /**
   * 수정 폼
   *
   */
  @GetMapping(value = "/update")
  public String update(HttpSession session,  Model model, 
      @RequestParam(name="replyno", defaultValue="0") int replyno, 
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

 //   if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      ReplyVO replyVO = this.replyProc.read(replyno);
      model.addAttribute("replyVO", replyVO);
      
      BoardVO boardVO = this.boardProc.read(replyVO.getBoardno());
      model.addAttribute("boardVO", boardVO);

      return "/reply/update"; // /templates/contents/update.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

//    } else {
////      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
////      return "redirect:/contents/msg"; // @GetMapping(value = "/read")
//      return "member/login_cookie_need";
//    }

  }

  /**
   * 수정 처리
   * @return
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, RedirectAttributes ra,
      @ModelAttribute("replyVO") ReplyVO replyVO, 
      @RequestParam(name="word", defaultValue="") String word, // replyVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
//  if (this.memberProc.isMember(session)) { // 로그인한경우
//    model.addAttribute("boardno", boardno);
//    model.addAttribute("word", word);
//    model.addAttribute("now_page", now_page);
//    
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    this.replyProc.update(replyVO); // Oracle 처리
    ra.addAttribute ("replyno", replyVO.getBoardno());
    ra.addAttribute("boardno", replyVO.getBoardno());
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/reply/list_all";
//  } else {
//    ra.addAttribute("url", "/member/login_cookie_need"); 
//    return "redirect:/board/post2get"; // GET
//  }
}
  
  /**
   * 삭제 폼
   * http://localhost:9093/reply/delete?replyno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                               @RequestParam(name="replyno", defaultValue="0") int replyno, 
                               @RequestParam(name="boardno", defaultValue="0") int boardno, 
                               @RequestParam(name="word", defaultValue="") String word, 
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    if (this.memberProc.isMember(session)) { // 로그인한경우
      model.addAttribute("boardno", boardno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
//      ArrayList<CateVOMenu> menu = this.cateProc.menu();
//      model.addAttribute("menu", menu);
      
      ReplyVO replyVO = this.replyProc.read(replyno);
      model.addAttribute("replyVO", replyVO);

      BoardVO boardVO = this.boardProc.read(replyVO.getBoardno());
      model.addAttribute("boardVO", boardVO);
      
      return "/reply/delete"; // forward
      
//    } else {
//      ra.addAttribute("url", "/member/login_cookie_need");
//      return "redirect:/board/msg"; 
//    }

  }
  
  /**
   * 삭제 처리 http://localhost:9093/reply/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,  
      @RequestParam(name="boardno", defaultValue="0") int boardno, 
      @RequestParam(name="replyno", defaultValue="0") int replyno, 
      @RequestParam(name="word", defaultValue="") String word, 
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
   
    ReplyVO replyVO_read = replyProc.read(replyno);
    String rcontent = replyVO_read.getRcontent();
    String rdate = replyVO_read.getRdate();
    
//    this.replyProc.delete(replyno); // DBMS 삭제
    System.out.println("->replyno: " + replyno);
    int cnt = this.replyProc.delete(replyno);
    System.out.println("->cnt: " + cnt);
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("boardno", boardno);
    map.put("word", word);
    
//    if (this.replyProc.list_by_boardno_search_count(map) % Reply.RECORD_PER_PAGE == 0) {
//      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
//      if (now_page < 1) {
//        now_page = 1; // 시작 페이지
//      }
//    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("boardno", boardno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/reply/list_by_replyno";    
    
  }   
  
 
}

