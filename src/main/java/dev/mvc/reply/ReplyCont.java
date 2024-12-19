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
   * 전체 목록
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
      ArrayList<ReplyVO> list = this.replyProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (ContentsVO contentsVO : list) {
//        String title = contentsVO.getTitle();
//        String content = contentsVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        contentsVO.setTitle(title);
//        contentsVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/reply/list_all";

    } else {
      return "redirect:/member/login_cookie_need";

    }

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
  @GetMapping(value = "/read/{boardno}")
  public String read(Model model, 
      @PathVariable(name="boardno") int boardno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    BoardVO boardVO = this.boardProc.read(boardno);
    model.addAttribute("boardVO", boardVO);

    // ArrayList<CateVO> list = this.cateProc.list_all();
    // ArrayList<CateVO> list = this.cateProc.list_search(word);
//    ArrayList<BoardVO> list = this.boardProc.list_search_paging(word, now_page, this.record_per_page);
//    model.addAttribute("list", list);

//   ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//   model.addAttribute("menu", menu);

//    ArrayList<ReplyVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
//
//    model.addAttribute("word", word);

    // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
//    int search_count = this.replyProc.list_by_boardno_search_count(map);
//    String paging = this.replyProc.pagingBox(boardno, now_page, word, "/reply/list_by_boardno", search_count,
//        Reply.RECORD_PER_PAGE, Reply.PAGE_PER_BLOCK);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    model.addAttribute("search_count", search_count);
//
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * Reply.RECORD_PER_PAGE);
//    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "/reply/read";
  }

  /**
   * 수정 폼
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @RequestParam(name="replyno", defaultValue="") int replyno, 
      RedirectAttributes ra, 
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      ReplyVO replyVO = this.replyProc.read(replyno);
      model.addAttribute("replyVO", replyVO);
      
      BoardVO boardVO = this.boardProc.read(replyVO.getBoardno());
      model.addAttribute("boardVO", boardVO);

      return "/reply/update_text"; // /templates/contents/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
//      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
//      return "redirect:/contents/msg"; // @GetMapping(value = "/read")
      return "member/login_cookie_need";
    }

  }

  /**
   * 수정 처리
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @ModelAttribute("replyVO") ReplyVO replyVO, 
      RedirectAttributes ra,
      @RequestParam(name="search_word", defaultValue="") String search_word, // replyVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue="0") int now_page) {
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("replyno", replyVO.getReplyno());
      map.put("passwd", replyVO.getPasswd());

      if (this.replyProc.password_check(map) == 1) { // 패스워드 일치
        this.replyProc.update_text(replyVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("replyno", replyVO.getReplyno());
        ra.addAttribute("boardno", replyVO.getBoardno());
        return "redirect:/reply/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/reply/msg"); // msg.html, redirect parameter 적용

        return "redirect:/reply/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/reply/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9093/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="boardno", defaultValue="") int boardno, 
      @RequestParam(name="replyno", defaultValue="0") int replyno, 
      @RequestParam(name="word", defaultValue="") String word, 
      @RequestParam(name="now_page", defaultValue="1") int now_page) {
   
    this.replyProc.delete(replyno); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("boardno", boardno);
    map.put("word", word);
    
    if (this.replyProc.list_by_boardno_search_count(map) % Reply.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("boardno", boardno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/reply/list_by_boardno";    
    
  }   
  
 
}

