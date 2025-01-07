package dev.mvc.bannedwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.bannedwords.BannedwordsVO;
import dev.mvc.bannedwordsgood.BannedwordsgoodProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/bannedwords")
public class BannedwordsCont {
	
  @Autowired
  @Qualifier("dev.mvc.bannedwords.BannedwordsProc")
  private BannedwordsProcInter bannedwordsProc;
  
  @Autowired
  @Qualifier("dev.mvc.bannedwordsgood.BannedwordsgoodProc")
  private BannedwordsgoodProcInter bannedwordsgoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") 
  private MemberProcInter memberProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/bannedwords/list_by_wordno_search_paging";
  
  public BannedwordsCont() {
    System.out.println("-> BannedwordsCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue="") String url) {

    return url; // forward, /templates/...
  }
  
  /**
   * 금지 단어 등록 폼
   * @param model
   * @param BannedwordsVO
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model,
		  HttpSession session,
      @ModelAttribute("bannedwordsVO") BannedwordsVO bannedwordsVO) { 
	
	  int memberno =(int) session.getAttribute("memberno");
	  bannedwordsVO.setMemberno(memberno); 
	  model.addAttribute("BannedwordsVO", bannedwordsVO); // 수정된 BannedwordsVO 전달

	  return "/bannedwords/create";
  }
  
  /**
   * 금지 단어 등록 처리
   * @param request
   * @param session
   * @param model
   * @param BannedwordsVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request,
                       HttpSession session,
                       Model model,
                       @ModelAttribute("BannedwordsVO") BannedwordsVO bannedwordsVO,
                       RedirectAttributes ra) {
	  int memberno = 1; 
	  bannedwordsVO.setMemberno(memberno); 
  
	  int cnt = this.bannedwordsProc.create(bannedwordsVO);
	  if (cnt == 1) {
		  ra.addAttribute("bannedwordsVO", bannedwordsVO.getWordno()); 
		  ra.addAttribute("now_page", 1); 
		  return "redirect:/bannedwords/list_by_wordno_search_paging"; 
	  } else {
		  ra.addFlashAttribute("code", "create_fail");
		  return "redirect:/bannedwords/msg"; 
	  }
  }
    
//  /**
//   * 금지 단어 전체 목록(관리자)
//   * @param session
//   * @param model
//   * @return
//   */
//  @GetMapping(value = "/list_all")
//  public String list_all(HttpSession session, Model model) {
//    
//    ArrayList<BannedwordsVO> list = this.bannedwordsProc.list_all(); // 금지 단어 모든 목록
//    
//    model.addAttribute("list", list);
//    return "/bannedwords/list_all";
//  }

  /**
   * 유형 3
   * 금지 단어별 목록 + 검색 + 페이징
   * 
   * @return
   */
  @GetMapping(value = "/list_by_wordno_search_paging")
  public String list_by_wordno_search_paging(
      HttpSession session,
      Model model,
      @ModelAttribute("bannedwordsVO") BannedwordsVO bannedwordsVO,
      @RequestParam(name = "wordno", defaultValue = "0") int wordno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      int record_per_page = 10;
      int startRow = (now_page - 1) * record_per_page + 1;
      int endRow = now_page * record_per_page;

      int memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      if (memberVO == null) {
          memberVO = new MemberVO();
          memberVO.setMemberno(0);
          model.addAttribute("message", "회원 정보가 없습니다.");
      }
      word = Tool.checkNull(word).trim();
      model.addAttribute("memberVO", memberVO);
      model.addAttribute("wordno", wordno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      HashMap<String, Object> map = new HashMap<>();
      map.put("memberno", memberno);
      map.put("word", word);
      map.put("now_page", now_page);
      map.put("startRow", startRow);
      map.put("endRow", endRow);

      ArrayList<BannedwordsVO> list = this.bannedwordsProc.list_by_wordno_search_paging(map);
      if (list == null || list.isEmpty()) {
          model.addAttribute("message", "게시물이 없습니다.");
      } else {
          model.addAttribute("list", list);          
          model.addAttribute("word", word);
      }

      int search_count = this.bannedwordsProc.count_by_wordno_search(map);
      String paging = this.bannedwordsProc.pagingBox(now_page, word, "/bannedwords/list_by_wordno_search_paging", search_count,
          Bannedwords.RECORD_PER_PAGE, Bannedwords.PAGE_PER_BLOCK);
      model.addAttribute("paging", paging);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      model.addAttribute("search_count", search_count);

      int no = search_count - ((now_page - 1) * Bannedwords.RECORD_PER_PAGE);
      model.addAttribute("no", no);

      return "/bannedwords/list_by_wordno_search_paging"; // /templates/board/list_by_boardno_search_paging.html
 
  }
  
  /**
   * 금지 단어 조회
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model,
      @RequestParam(name = "wordno", defaultValue = "0") int wordno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    BannedwordsVO bannedwordsVO = this.bannedwordsProc.read(wordno);
   
    model.addAttribute("bannedwordsVO", bannedwordsVO);
    
    MemberVO memberVO = this.memberProc.read(bannedwordsVO.getMemberno());
    model.addAttribute("memberVO", memberVO);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    // ---------------------------------------------------------------------------------
    //추천 관련
    // ---------------------------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("wordno", wordno);
    
    int heartCnt = 0;
    if (session.getAttribute("memberno") != null) {  //회원인 경우만 카운트 처리
    	int memberno = (int)session.getAttribute("memberno");
        map.put("memberno", memberno);

        heartCnt = this.bannedwordsgoodProc.heartCnt(map);
     }
    
    model.addAttribute("heartCnt", heartCnt);
    // ---------------------------------------------------------------------------------

    return "/bannedwords/read";
  }
  
  /**
   * 금지 단어 수정 폼
   * @return
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session,
      Model model, 
      @RequestParam(name = "wordno", defaultValue = "0") int wordno,
      RedirectAttributes ra,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우

    	BannedwordsVO bannedwordsVO = this.bannedwordsProc.read(wordno);
//    	System.out.println("-> bannedwordsVO.getWordno() : " + bannedwordsVO.getWordno());
        model.addAttribute("bannedwordsVO", bannedwordsVO);
        
        MemberVO memberVO = this.memberProc.read(bannedwordsVO.getMemberno());
        model.addAttribute("memberVO", memberVO);
        
        return "/bannedwords/update_text";
    } else {
        ra.addAttribute("url", "/member/login_cookie_need"); // /templates/diary/login_cookie_need.html
//        return "redirect:/contents/msg"; // @GetMapping(value = "/read")
        return "member/login_cookie_need";
      }
  }
  
  /**
   * 금지 단어 수정 처리
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(
      HttpSession session,
      Model model,
      @ModelAttribute("BannedwordsVO") BannedwordsVO bannedwordsVO,
      RedirectAttributes ra,
      @RequestParam(name = "wordno", defaultValue = "") int wordno,
      @RequestParam(name = "search_word", defaultValue = "") String search_word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
	  
      // Redirect 시 검색어 및 현재 페이지를 유지하기 위한 파라미터 추가
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);
      
      // reason 값 검증
      if (bannedwordsVO.getReason() == null || bannedwordsVO.getReason().trim().isEmpty()) {
        ra.addFlashAttribute("message", "이유는 필수 입력 사항입니다.");
        ra.addFlashAttribute("code", "update_fail");
        return "redirect:/bannedwords/msg"; // 실패 시 msg 페이지로 이동
      }
      
      // 금지 단어 글 수정 처리
      try {
        int cnt = this.bannedwordsProc.update_text(bannedwordsVO); // 금지 단어 글 수정
        if (cnt > 0) { // 수정 성공
          ra.addAttribute("wordno", bannedwordsVO.getWordno());
          return "redirect:/bannedwords/read"; // 성공 시 게시글 조회 페이지로 이동
        } else { // 수정 실패
          ra.addFlashAttribute("message", "금지 단어 글 수정에 실패했습니다.");
          ra.addFlashAttribute("code", "update_fail");
          return "redirect:/bannedwords/msg"; // 실패 시 msg 페이지로 이동
        }
      } catch (Exception e) {
        e.printStackTrace();
        ra.addFlashAttribute("message", "금지 단어 글 수정 중 오류가 발생했습니다.");
        ra.addFlashAttribute("code", "update_fail");
        return "redirect:/bannedwords/msg"; // 오류 발생 시 msg 페이지로 이동
    }
  }
  
  /**
   * 금지 단어 삭제 폼
   * @param session
   * @param model
   * @param ra
   * @param wordno
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model,
         RedirectAttributes ra,
         @RequestParam(name = "wordno", defaultValue = "0") int wordno,
         @RequestParam(name = "memberno", defaultValue = "0") int memberno,
         @RequestParam(name = "word", defaultValue = "") String word,
         @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
		  model.addAttribute("wordno", wordno);
		  model.addAttribute("word", word);
		  model.addAttribute("now_page", now_page);
		    
		  BannedwordsVO bannedwordsVO = this.bannedwordsProc.read(wordno);
		  model.addAttribute("bannedwordsVO", bannedwordsVO);
		  
		  MemberVO  memberVO = this.memberProc.read(bannedwordsVO.getMemberno());
		  model.addAttribute("memberVO", memberVO);
		    
		  return "/bannedwords/delete"; // forward
	
  }
  
  /**
   * 금지 단어 삭제 처리
   * @param ra
   * @param wordno
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
	  @RequestParam(name="memberno", defaultValue="0") int memberno, 
      @RequestParam(name = "wordno", defaultValue = "0") int wordno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    this.bannedwordsProc.delete(wordno); // DBMS 삭제
    
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("word", word);
    
    ra.addAttribute("memberno", memberno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/bannedwords/list_by_wordno_search_paging";
  }
  
  /**
   * 금지 단어 좋아요 수 수정 폼
   * @return
   */
  @GetMapping(value = "/update_goodcnt")
  public String update_goodcnt(HttpSession session,
      Model model, 
      @RequestParam(name = "wordno", defaultValue = "0") int wordno,
      RedirectAttributes ra,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우

    	BannedwordsVO bannedwordsVO = this.bannedwordsProc.read(wordno);
//    	System.out.println("-> bannedwordsVO.getWordno() : " + bannedwordsVO.getWordno());
        model.addAttribute("bannedwordsVO", bannedwordsVO);
        
        MemberVO memberVO = this.memberProc.read(bannedwordsVO.getMemberno());
        model.addAttribute("memberVO", memberVO);
        
        return "/bannedwords/update_goodcnt";
    } else {
        ra.addAttribute("url", "/member/login_cookie_need"); // /templates/diary/login_cookie_need.html
//        return "redirect:/contents/msg"; // @GetMapping(value = "/read")
        return "member/login_cookie_need";
      }
  }
  
}