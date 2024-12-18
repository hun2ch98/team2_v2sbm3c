package dev.mvc.reply;

import java.util.ArrayList;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.board.BoardProcInter;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "/reply")
@Controller
public class ReplyCont {
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc")
  private ReplyProcInter replyProc;

  @Autowired
  @Qualifier("dev.mvc.board.BoardProc")
  private BoardProcInter boardProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/reply/list_search";

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  //private Object contentsProc;

//  @Autowired
//  @Qualifier("dev.mvc.cate.CateProc2023")
//  private CateProcInter cateProc;

//  @Autowired
//  @Qualifier("dev.mvc.cate.CateProc")
//  private CateProcInter cateProc;

  public ReplyCont() {
    System.out.println("-> ReplyCont created.");
  }

//  @GetMapping(value="/create") // http://localhost:9093/reply/create
//  @ResponseBody // html 파일 내용임.
//  public String create() {
//    return "<h2>Create test</h2>";
//  }

//  @GetMapping(value="/create") // http://localhost:9093/cate/create
//  public String create() {
//    return "/cate/create"; // /templates/cate/create.html
//  }

  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  // http://localhost:9093/reply/create/ X
  // http://localhost:9093/reply/create
  @GetMapping(value = "/create")
  public String create(Model model) {
    ReplyVO replyVO = new ReplyVO();
    model.addAttribute("replyVO", replyVO);

    replyVO.setRcontent("댓글 내용을 입력하세요."); // Form으로 초기값을 전달
    return "/reply/create"; // /templates/reply/create.html
  }

// - 유형 1 (권장): 옵션 모두 표시
//  @PostMapping(value="/create") // http://localhost:9093/reply/create
//  public String create(Model model, @Valid @ModelAttribute("btcreplyVO") BtcreplyVO btcreplyVO, BindingResult bindingresult) {
//
// - 유형 2: 코드 생략형
// @PostMapping(value="/create") // http://localhost:9093/reply/create
// public String create(Model model, @Valid ReplyVO replyVO, BindingResult bindingResult) {
  /**
   * 등록 처리, http://localhost:9093/reply/create
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param replyVO        Form 태그 값 -> 검증 -> replyVO 자동 저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/create")
  public String create(Model model, @Valid @ModelAttribute("replyVO") ReplyVO replyVO, BindingResult bindingResult) {
//    System.out.println("-> create post.");
    if (bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
//      System.out.println("-> ERROR 발생");
      // model.addAttribute("replyVO", replyVO);
      return "/reply/create"; // /templates/reply/create.html
    }

//    System.out.println(cateVO.getName());
//    System.out.println(cateVO.getSeqno());
//    System.out.println(cateVO.getVisible());

    replyVO.setRcontent(replyVO.getRcontent().trim());
    
    int cnt = this.replyProc.create(replyVO);
    System.out.println("-> cnt: " + cnt);

    if (cnt == 1) {
      // model.addAttribute("code", "create_success");
      // model.addAttribute("name", replyVO.getRcontent());

      // return "redirect:/reply/list_all"; // @GetMapping(value="/list_all")
      return "redirect:/reply/list_search"; // @GetMapping(value="/list_all")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/reply/msg"; // /templates/cate/msg.html
  }
  
  /**
   * 조회 http://localhost:9091/cate/read/1
   */
  @GetMapping(value = "/read/{replyno}")
  public String read(Model model, @PathVariable("cateno") Integer cateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page) {
    CateVO cateVO = this.cateProc.read(cateno);
    model.addAttribute("cateVO", cateVO);

    // ArrayList<CateVO> list = this.cateProc.list_all();
    // ArrayList<CateVO> list = this.cateProc.list_search(word);
    ArrayList<CateVO> list = this.cateProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);

//   ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//   model.addAttribute("menu", menu);

    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);

    // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
    int search_count = this.cateProc.list_search_count(word);
    String paging = this.cateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "/cate/read";
  }
  
  /**
   * 수정폼 http://localhost:9093/reply/update/1
   */
  @GetMapping(value = "/update/{replyno}")
  public String update(HttpSession session, Model model, @PathVariable("replyno") Integer replyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      ReplyVO replyVO = this.replyProc.read(replyno);
      model.addAttribute("replyVO", replyVO);

      // ArrayList<CateVO> list = this.cateProc.list_all();
      ArrayList<ReplyVO> list = this.cateProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

//     ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//     model.addAttribute("menu", menu);

      ArrayList<CateVOMenu> menu = this.cateProc.menu();
      model.addAttribute("menu", menu);

      // 카테고리 그룹 목록
      ArrayList<String> list_genre = this.cateProc.genreset();
      model.addAttribute("list_genre", String.join("/", list_genre));

      model.addAttribute("word", word);

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.cateProc.list_search_count(word);
      String paging = this.cateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------
      return "/cate/update"; // templaes/cate/update.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 수정 처리, http://localhost:9093/cate/update
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, 
                                    @Valid @ModelAttribute("cateVO") CateVO cateVO, BindingResult bindingResult,
                                    @RequestParam(name = "word", defaultValue = "") String word,
                                    @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) {
//    System.out.println("-> update post.");
    if (bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
//      System.out.println("-> ERROR 발생");
      return "/cate/update"; // /templates/cate/update.html
    }

//    System.out.println(cateVO.getName());
//    System.out.println(cateVO.getSeqno());
//    System.out.println(cateVO.getVisible());

    int cnt = this.cateProc.update(cateVO);
    System.out.println("-> cnt: " + cnt);

    if (cnt == 1) {
//      model.addAttribute("code", "update_success");
//      model.addAttribute("genre", cateVO.getGenre());
//      model.addAttribute("name", cateVO.getName());

      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

      return "redirect:/cate/update/" + cateVO.getCateno(); // @GetMapping(value="/update/{cateno}")
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
    int search_count = this.cateProc.list_search_count(word);
    String paging = this.cateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "/cate/msg"; // /templates/cate/msg.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
    
  }

  /**
   * 삭제폼 http://localhost:9093/cate/delete/1
   */
  @GetMapping(value = "/delete/{cateno}")
  public String delete(HttpSession session, Model model, 
                                   @PathVariable("cateno") Integer cateno,
                                   @RequestParam(name = "word", defaultValue = "") String word,
                                   @RequestParam(name = "now_page", defaultValue = "") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      CateVO cateVO = this.cateProc.read(cateno);
      model.addAttribute("cateVO", cateVO);
      int cnt = this.cateProc.cntcount(cateno);

      // ArrayList<CateVO> list = this.cateProc.list_all();
      ArrayList<CateVO> list = this.cateProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

//     ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//     model.addAttribute("menu", menu);

      ArrayList<CateVOMenu> menu = this.cateProc.menu();
      model.addAttribute("menu", menu);

      model.addAttribute("cnt", cnt);  // 콘텐츠 개수 추가
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.cateProc.list_search_count(word);
      String paging = this.cateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------
      if (cnt == 0) {
        // 콘텐츠가 없을 경우 cate/delete.html로 이동
        return "/cate/delete";
      } else {
        // 콘텐츠가 있을 경우 cate/list_all_delete.html로 이동
        ArrayList<ContentsVO> contentsList = contentsProc.listByCateNo(cateno);  // 해당 카테고리의 콘텐츠 리스트 불러오기
        model.addAttribute("contentsList", contentsList);
        model.addAttribute("cnt", cnt);
        model.addAttribute("word", word);
        model.addAttribute("now_page", now_page);
        return "/cate/list_all_delete"; // cate/list_all_delete.html로 이동
      }
    } else {
      return "redirect:/member/login_cookie_need"; // 관리자 권한 필요
    }
  }
  
  /**
   * 카테고리 및 연관 자료 삭제 처리
   */
  @PostMapping(value = "/delete_all_confirm")
  public String deleteAllCategory(@RequestParam (name="cateno", defaultValue="0") int cateno,
                                                       RedirectAttributes redirectAttributes) {
    // 콘텐츠 삭제
    contentsProc.deleteByCateNo(cateno);

    // 카테고리 삭제
    cateProc.delete(cateno);

    redirectAttributes.addFlashAttribute("msg", "카테고리와 관련된 모든 자료가 삭제되었습니다.");
    return "redirect:/cate/list_search";
  }

  /**
   * 카테고리 삭제 폼
   */
  @GetMapping(value = "/delete")
  public String delete(Model model) {
    // 기본 삭제 폼
    return "/cate/delete";  // cate/delete.html로 이동
  }

  /**
   * 삭제 처리, http://localhost:9093/cate/delete?cateno=1
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_process(HttpSession session, Model model, 
                               @RequestParam(name = "cateno", defaultValue = "0") Integer cateno,
                               @RequestParam(name = "word", defaultValue = "") String word,
                               @RequestParam(name = "now_page", defaultValue = "") int now_page, 
                               RedirectAttributes ra) {
      // 관리자 권한 확인
      if (this.memberProc.isMemberAdmin(session)) {
          System.out.println("-> delete_process");

          CateVO cateVO = this.cateProc.read(cateno); // 삭제 전에 레코드 조회
          model.addAttribute("cateVO", cateVO);

          // 카테고리에 속한 콘텐츠 개수 확인
          int cnt = this.cateProc.cntcount(cateno); // 해당 카테고리 내 콘텐츠 수

          if (cnt == 0) {
              // 콘텐츠가 없으면 카테고리만 삭제
              int deleteCnt = this.cateProc.delete(cateno);
              System.out.println("-> deleteCnt: " + deleteCnt);

              if (deleteCnt == 1) {
                  ra.addAttribute("word", word); // redirect로 데이터 전송

                  // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야 함.
                  int search_cnt = this.cateProc.list_search_count(word);
                  if (search_cnt % this.record_per_page == 0) {
                      now_page = now_page - 1;
                      if (now_page < 1) {
                          now_page = 1; // 최소 시작 페이지
                      }
                  }

                  ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

                  return "redirect:/cate/list_search"; // 카테고리 목록 페이지로 리다이렉트
              } else {
                  model.addAttribute("code", "delete_fail");
                  return "/cate/msg"; // 실패 메시지 출력
              }

          } else {
              // 콘텐츠가 있을 경우 cate/list_all_delete.html로 이동하여 확인 요청
              ArrayList<ContentsVO> contentsList = contentsProc.listByCateNo(cateno); // 해당 카테고리의 콘텐츠 리스트 불러오기
              model.addAttribute("contentsList", contentsList);
              model.addAttribute("cnt", cnt);
              model.addAttribute("word", word);
              model.addAttribute("now_page", now_page);
              return "/cate/list_all_delete"; // cate/list_all_delete.html로 이동
          }
      } else {
          return "redirect:/member/login_cookie_need";  // 권한 없을 때 로그인 페이지로 리다이렉트
      }
  }


  /**
   * 우선 순위 높임, 10 등 -> 1 등, http://localhost:9093/cate/update_seqno_forward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_forward/{cateno}")
  public String update_seqno_forward(Model model, @PathVariable("cateno") Integer cateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    this.cateProc.update_seqno_forward(cateno);

    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

    return "redirect:/cate/list_search"; // @GetMapping(value="/list_search")
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등, http://localhost:9093/cate/update_seqno_backward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_backward/{cateno}")
  public String update_seqno_backward(Model model, @PathVariable("cateno") Integer cateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    this.cateProc.update_seqno_backward(cateno);

    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

    return "redirect:/cate/list_search"; // @GetMapping(value="/list_search")
  }

  /**
   * 카테고리 공개 설정, http://localhost:9093/cate/update_visible_y/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_y/{cateno}")
  public String update_visible_y(HttpSession session, Model model, @PathVariable("cateno") Integer cateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      this.cateProc.update_visible_y(cateno);

      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

      return "redirect:/cate/list_search"; // @GetMapping(value="/list_search")
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }

  /**
   * 카테고리 비공개 설정, http://localhost:9093/cate/update_visible_n/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_n/{cateno}")
  public String update_visible_n(HttpSession session, Model model, @PathVariable("cateno") Integer cateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    
    if (this.memberProc.isMemberAdmin(session)) {
      this.cateProc.update_visible_n(cateno);

      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

      return "redirect:/cate/list_search"; // @GetMapping(value="/list_search")
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
  }

//  /**
//   * 등록 폼 및 검색 목록
//   * http://localhost:9093/cate/list_search
//   * http://localhost:9093/cate/list_search?word=
//   * http://localhost:9093/cate/list_search?word=까페
//   * @param model
//   * @return
//   */
//  @GetMapping(value="/list_search") 
//  public String list_search(Model model, 
//                                   @RequestParam(name="word", defaultValue = "") String word) {
//    CateVO cateVO = new CateVO();
//    // cateVO.setGenre("분류");
//    // cateVO.setName("카테고리 이름을 입력하세요."); // Form으로 초기값을 전달
//    
//    // 카테고리 그룹 목록
//    ArrayList<String> list_genre = this.cateProc.genreset();
//    cateVO.setGenre(String.join("/", list_genre));
//    
//    model.addAttribute("cateVO", cateVO);
//    
//    word = Tool.checkNull(word);
//    
//    ArrayList<CateVO> list = this.cateProc.list_search(word);
//    model.addAttribute("list", list);
//    
////    ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
////    model.addAttribute("menu", menu);
//
//    ArrayList<CateVOMenu> menu = this.cateProc.menu();
//    model.addAttribute("menu", menu);
//    
//    int search_cnt = this.cateProc.list_search_count(word);
//    model.addAttribute("search_cnt", search_cnt);    
//    
//    model.addAttribute("word", word);  
//    
//    return "/cate/list_search";  // /templates/cate/list_search.html
//  }
//  

  /**
   * 등록 폼 및 검색 목록 + 페이징 http://localhost:9093/cate/list_search
   * http://localhost:9093/cate/list_search?word=&now_page=
   * http://localhost:9093/cate/list_search?word=까페&now_page=1
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(HttpSession session, Model model,
                                                      @RequestParam(name = "word", defaultValue = "") String word,
                                                      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      CateVO cateVO = new CateVO();
      // cateVO.setGenre("분류");
      // cateVO.setName("카테고리 이름을 입력하세요."); // Form으로 초기값을 전달

      // 카테고리 그룹 목록
      ArrayList<String> list_genre = this.cateProc.genreset();
      cateVO.setGenre(String.join("/", list_genre));

      model.addAttribute("cateVO", cateVO);

      word = Tool.checkNull(word);

      ArrayList<CateVO> list = this.cateProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

//      ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//      model.addAttribute("menu", menu);
      
      for (CateVO cate : list) {
        if (cate.getName().equals("--")) {  // 대분류인 경우
            int totalCnt = 0;

            // 중분류 카테고리에서 대분류에 속하는 자료 수를 합산
            for (CateVO subCate : list) {
                if (!subCate.getName().equals("--") && subCate.getGenre().equals(cate.getGenre())) {
                    totalCnt += this.cateProc.cntcount(subCate.getCateno());
                    }
                } 
            cate.setCnt(totalCnt); // 대분류 카테고리의 자료 수 설정
        } else {
            int contentsCount = this.cateProc.cntcount(cate.getCateno()); // 각 중분류 카테고리의 자료 수 조회
            cate.setCnt(contentsCount); // CateVO 객체에 자료 수 설정
        }
      }

      ArrayList<CateVOMenu> menu = this.cateProc.menu();
      model.addAttribute("menu", menu);

      int search_cnt = this.cateProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);

      model.addAttribute("word", word); // 검색어

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.cateProc.list_search_count(word);
      String paging = this.cateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      return "/cate/list_search"; // /templates/cate/list_search.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }

  }

}
